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
        
        List<Menu> newMenusToAdd = menuRepo.findAllById(menuTemplateDTO.getMenuIds());

        // Validation: Ensure all selected menus are actual sub-menus
        for (Menu menu : newMenusToAdd) {
            if (menu.getParentMenu() == null) {
                throw new IllegalArgumentException("Menu '" + menu.getTitle() + "' is not a sub-menu and cannot be added to a template.");
            }
        }

        MenuTemplate menuTemplate = menuTemplateRepo.findByRoleId(menuTemplateDTO.getRoleId())
                .orElse(new MenuTemplate());
        
        if (menuTemplate.getId() == null) {
            menuTemplate.setRole(role);
        }

        // Safely initialize the set to avoid NullPointerException if the template is new
        Set<Menu> currentMenus = menuTemplate.getMenus() == null ? new HashSet<>() : new HashSet<>(menuTemplate.getMenus());
        currentMenus.addAll(newMenusToAdd);
        menuTemplate.setMenus(new ArrayList<>(currentMenus));
        
        MenuTemplate savedTemplate = menuTemplateRepo.save(menuTemplate);

        // Use the new utility class for conversion
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
        if (template == null || template.getMenus().isEmpty()) {
            return new ArrayList<>();
        }

        // Group the allowed sub-menus by their parent menu
        Map<Menu, List<Menu>> parentToSubMenusMap = template.getMenus().stream()
                .filter(menu -> menu.getParentMenu() != null)
                .collect(Collectors.groupingBy(Menu::getParentMenu));

        // Transform the grouped map into the desired DTO structure
        return parentToSubMenusMap.entrySet().stream()
                .map(entry -> {
                    Menu parent = entry.getKey();
                    List<Menu> allowedSubMenus = entry.getValue();

                    SuperMenuDTO superMenuDTO = new SuperMenuDTO();
                    superMenuDTO.setId(parent.getId());
                    superMenuDTO.setTitle(parent.getTitle());

                    List<SubMenuDTO> subMenuDTOs = allowedSubMenus.stream().map(sm -> {
                        SubMenuDTO subMenuDTO = new SubMenuDTO();
                        subMenuDTO.setId(sm.getId());
                        subMenuDTO.setTitle(sm.getTitle());
                        subMenuDTO.setUrl(sm.getUrl());
                        return subMenuDTO;
                    }).collect(Collectors.toList());

                    superMenuDTO.setSubMenuDTOList(subMenuDTOs);
                    return superMenuDTO;
                }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteMenuFromAllTemplates(Long menuId) {
        List<MenuTemplate> templatesToUpdate = menuTemplateRepo.findByMenus_Id(menuId);
        if (templatesToUpdate.isEmpty()) {
            return;
        }

        for (MenuTemplate template : templatesToUpdate) {
            template.getMenus().removeIf(menu -> menu.getId().equals(menuId));
        }

        menuTemplateRepo.saveAll(templatesToUpdate);
    }
}