package com.progress.coolProject.Utils.menu;

import com.progress.coolProject.DTO.Menu.Input.MenuDTO;
import com.progress.coolProject.Entity.Menu.Menu;
import com.progress.coolProject.Enums.MenuType;
import com.progress.coolProject.Repo.menu.MenuRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MenuUtils {

    private final MenuRepo menuRepo;

    public Menu convertToEntity(MenuDTO dto) {
        Menu menu = new Menu();
        menu.setId(dto.getId());
        menu.setTitle(dto.getTitle());
        menu.setMenuType(dto.getMenuType());
        menu.setUrl(dto.getUrl());
        if (dto.getMenuType() == MenuType.SUPER) {
            dto.setParentId(null);
        }
        if (dto.getParentId() != null) {
            Menu parentMenu = menuRepo.findById(dto.getParentId()).orElseThrow(()-> new IllegalArgumentException("Parent menu not found"));
            menu.setParentMenu(parentMenu);
        } else {
            if (dto.getMenuType() == MenuType.SUB) {
                throw new IllegalArgumentException("Sub-menu must have a parent menu.");
            }
            menu.setParentMenu(null);
        }
        return menu;
    }

    public MenuDTO convertToDto(Menu menu) {
        MenuDTO dto = new MenuDTO();
        dto.setId(menu.getId());
        dto.setTitle(menu.getTitle());
        dto.setMenuType(menu.getMenuType());
        dto.setUrl(menu.getUrl());
        if (menu.getParentMenu() != null) {
            dto.setParentId(menu.getParentMenu().getId());
        }
        return dto;
    }
}