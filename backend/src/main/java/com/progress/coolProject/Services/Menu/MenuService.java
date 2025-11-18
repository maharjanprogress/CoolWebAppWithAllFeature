package com.progress.coolProject.Services.Menu;

import com.progress.coolProject.DTO.Menu.Input.MenuDTO;
import com.progress.coolProject.Entity.Menu.Menu;
import com.progress.coolProject.Repo.menu.MenuRepo;
import com.progress.coolProject.Services.Impl.menu.IMenuService;
import com.progress.coolProject.Services.Impl.menu.IMenuTemplateService;
import com.progress.coolProject.Utils.menu.MenuUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService implements IMenuService {

    private final MenuRepo menuRepo;
    private final IMenuTemplateService menuTemplateService;
    private final MenuUtils menuUtils;

    @Override
    public MenuDTO createMenu(MenuDTO menuDTO) {
        Menu menu = menuUtils.convertToEntity(menuDTO);
        Menu savedMenu = menuRepo.save(menu);
        return menuUtils.convertToDto(savedMenu);
    }

    @Override
    public MenuDTO getMenuById(Long id) {
        Menu menu = menuRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Menu not found with id: " + id));
        return menuUtils.convertToDto(menu);
    }

    @Override
    public List<MenuDTO> getAllMenus() {
        return menuRepo.findAll().stream().map(menuUtils::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<MenuDTO> getAllSubMenus() {
        return menuRepo.findByParentMenuIsNotNull().stream().map(menuUtils::convertToDto).collect(Collectors.toList());
    }

    @Override
    public MenuDTO updateMenu(Long id, MenuDTO menuDTO) {
        if (!menuRepo.existsById(id)) {
            throw new EntityNotFoundException("Menu not found with id: " + id);
        }
        menuDTO.setId(id);
        Menu menuToUpdate = menuUtils.convertToEntity(menuDTO);
        Menu updatedMenu = menuRepo.save(menuToUpdate);
        return menuUtils.convertToDto(updatedMenu);
    }

    @Override
    @Transactional
    public void deleteMenu(Long id) {
        if (!menuRepo.existsById(id)) {
            throw new EntityNotFoundException("Menu not found with id: " + id);
        }
        menuTemplateService.deleteMenuFromAllTemplates(id);
        menuRepo.deleteById(id);
    }
}