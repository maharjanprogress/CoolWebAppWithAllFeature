package com.progress.coolProject.Services.Menu;

import com.progress.coolProject.DTO.Menu.Input.MenuTemplateDTO;
import com.progress.coolProject.DTO.Menu.Output.SubMenuDTO;
import com.progress.coolProject.DTO.Menu.Output.SuperMenuDTO;
import com.progress.coolProject.Entity.Menu.Menu;
import com.progress.coolProject.Entity.Menu.MenuTemplate;
import com.progress.coolProject.Entity.Role;
import com.progress.coolProject.Repo.RoleRepo;
import com.progress.coolProject.Repo.menu.MenuRepo;
import com.progress.coolProject.Repo.menu.MenuTemplateRepo;
import com.progress.coolProject.Services.Impl.menu.IMenuTemplateService;
import com.progress.coolProject.Utils.menu.MenuTemplateUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuTemplateService implements IMenuTemplateService {

    private final MenuTemplateRepo menuTemplateRepo;
    private final MenuRepo menuRepo;
    private final RoleRepo roleRepo; // Assuming RoleRepo exists
    private final MenuTemplateUtils menuTemplateUtils;

    @Override
    @Transactional
    public MenuTemplateDTO createOrUpdateTemplate(MenuTemplateDTO menuTemplateDTO) {
        Role role = roleRepo.findById(menuTemplateDTO.getRoleId())
                .orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + menuTemplateDTO.getRoleId()));

        List<Long> requestedMenuIds = menuTemplateDTO.getMenuIds() == null
                ? Collections.emptyList()
                : menuTemplateDTO.getMenuIds();
        List<Long> distinctMenuIds = requestedMenuIds.stream().distinct().toList();

        Map<Long, Menu> menuMap = menuRepo.findAllById(distinctMenuIds).stream()
                .collect(Collectors.toMap(Menu::getId, Function.identity()));

        if (menuMap.size() != distinctMenuIds.size()) {
            throw new EntityNotFoundException("One or more selected menus were not found.");
        }

        List<Menu> selectedMenus = distinctMenuIds.stream()
                .map(menuMap::get)
                .toList();

        for (Menu menu : selectedMenus) {
            if (menu.getParentMenu() == null) {
                throw new IllegalArgumentException("Menu '" + menu.getTitle() + "' is not a sub-menu and cannot be added to a template.");
            }
        }

        MenuTemplate menuTemplate = menuTemplateRepo.findByRoleId(menuTemplateDTO.getRoleId())
                .orElse(new MenuTemplate());
        
        if (menuTemplate.getId() == null) {
            menuTemplate.setRole(role);
        }

        Menu primaryMenu = resolvePrimaryMenu(menuTemplateDTO.getPrimaryMenuId(), selectedMenus, menuTemplate.getPrimaryMenu());

        menuTemplate.setMenus(new ArrayList<>(selectedMenus));
        menuTemplate.setPrimaryMenu(primaryMenu);

        MenuTemplate savedTemplate = menuTemplateRepo.save(menuTemplate);

        return menuTemplateUtils.convertToDto(savedTemplate);
    }

    @Override
    public MenuTemplateDTO getTemplateByRoleId(Long roleId) {
        MenuTemplate template = menuTemplateRepo.findByRoleId(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Menu template not found for role id: " + roleId));

        // Use the new utility class for conversion
        return menuTemplateUtils.convertToDto(template);
    }

    @Override
    public List<SuperMenuDTO> getFormattedMenusByRoleId(Long roleId) {
        MenuTemplate template = menuTemplateRepo.findByRoleId(roleId).orElse(null);
        if (template == null || template.getMenus() == null || template.getMenus().isEmpty()) {
            return new ArrayList<>();
        }

        LinkedHashMap<Long, SuperMenuDTO> superMenuMap = new LinkedHashMap<>();
        Long effectivePrimarySubMenuId = resolvePrimarySubMenuId(template);

        for (Menu subMenu : template.getMenus()) {
            if (subMenu.getParentMenu() == null) {
                continue;
            }

            Menu parentMenu = subMenu.getParentMenu();
            SuperMenuDTO superMenuDTO = superMenuMap.computeIfAbsent(parentMenu.getId(), id -> {
                SuperMenuDTO dto = new SuperMenuDTO();
                dto.setId(parentMenu.getId());
                dto.setTitle(parentMenu.getTitle());
                dto.setPrimaryMenu(false);
                dto.setSubMenuDTOList(new ArrayList<>());
                return dto;
            });

            SubMenuDTO subMenuDTO = new SubMenuDTO();
            subMenuDTO.setId(subMenu.getId());
            subMenuDTO.setTitle(subMenu.getTitle());
            subMenuDTO.setUrl(subMenu.getUrl());
            subMenuDTO.setParentMenuId(parentMenu.getId());
            subMenuDTO.setParentMenuTitle(parentMenu.getTitle());
            subMenuDTO.setPrimaryMenu(subMenu.getId().equals(effectivePrimarySubMenuId));
            superMenuDTO.getSubMenuDTOList().add(subMenuDTO);
        }

        List<SuperMenuDTO> formattedMenus = new ArrayList<>(superMenuMap.values());
        if (effectivePrimarySubMenuId != null) {
            formattedMenus.forEach(menu -> menu.setPrimaryMenu(menu.getSubMenuDTOList().stream()
                    .anyMatch(subMenu -> subMenu.getId().equals(effectivePrimarySubMenuId))));
        }

        return formattedMenus;
    }

    @Override
    @Transactional
    public void deleteMenuFromAllTemplates(Long menuId) {
        List<MenuTemplate> templatesToUpdate = menuTemplateRepo.findByMenus_Id(menuId);
        for (MenuTemplate template : templatesToUpdate) {
            template.getMenus().removeIf(menu -> menu.getId().equals(menuId));
            if (template.getPrimaryMenu() != null && template.getMenus().stream()
                    .noneMatch(menu -> menu.getId().equals(template.getPrimaryMenu().getId()))) {
                template.setPrimaryMenu(null);
            }
        }

        List<MenuTemplate> templatesWithPrimaryMenu = menuTemplateRepo.findByPrimaryMenu_Id(menuId);
        for (MenuTemplate template : templatesWithPrimaryMenu) {
            template.setPrimaryMenu(null);
        }

        Set<MenuTemplate> affectedTemplates = new LinkedHashSet<>();
        affectedTemplates.addAll(templatesToUpdate);
        affectedTemplates.addAll(templatesWithPrimaryMenu);

        if (!affectedTemplates.isEmpty()) {
            menuTemplateRepo.saveAll(affectedTemplates);
        }
    }

    private Menu resolvePrimaryMenu(Long primaryMenuId, List<Menu> selectedMenus, Menu existingPrimaryMenu) {
        if (primaryMenuId == null) {
            if (existingPrimaryMenu != null && selectedMenus.stream()
                    .anyMatch(menu -> menu.getId().equals(existingPrimaryMenu.getId()))) {
                return existingPrimaryMenu;
            }

            return null;
        }

        if (selectedMenus.isEmpty()) {
            throw new IllegalArgumentException("A primary menu cannot be set when no sub-menus are selected.");
        }

        Menu primaryMenu = menuRepo.findById(primaryMenuId)
                .orElseThrow(() -> new EntityNotFoundException("Primary menu not found with id: " + primaryMenuId));

        if (primaryMenu.getParentMenu() == null) {
            throw new IllegalArgumentException("Primary menu must be a sub-menu.");
        }

        boolean matchesSelectedMenus = selectedMenus.stream()
                .anyMatch(menu -> menu.getId().equals(primaryMenuId));

        if (!matchesSelectedMenus) {
            throw new IllegalArgumentException("Primary menu must match one of the selected sub-menus in the template.");
        }

        return primaryMenu;
    }

    private Long resolvePrimarySubMenuId(MenuTemplate template) {
        if (template.getMenus() == null || template.getMenus().isEmpty()) {
            return null;
        }

        if (template.getPrimaryMenu() != null) {
            boolean primaryExistsInTemplate = template.getMenus().stream()
                    .anyMatch(menu -> menu.getId().equals(template.getPrimaryMenu().getId()));
            if (primaryExistsInTemplate) {
                return template.getPrimaryMenu().getId();
            }
        }

        return template.getMenus().stream()
                .filter(menu -> menu.getParentMenu() != null)
                .map(Menu::getId)
                .findFirst()
                .orElse(null);
    }
}
