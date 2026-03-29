package com.progress.coolProject.Utils.menu;

import com.progress.coolProject.DTO.Menu.Input.MenuTemplateDTO;
import com.progress.coolProject.DTO.Menu.Output.SubMenuDTO;
import com.progress.coolProject.Entity.Menu.Menu;
import com.progress.coolProject.Entity.Menu.MenuTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class MenuTemplateUtils {

    public MenuTemplateDTO convertToDto(MenuTemplate template) {
        MenuTemplateDTO dto = new MenuTemplateDTO();
        dto.setRoleId(template.getRole().getId());
        dto.setPrimaryMenuId(template.getPrimaryMenu() != null ? template.getPrimaryMenu().getId() : null);

        if (template.getMenus() != null) {
            dto.setMenuIds(template.getMenus().stream()
                    .map(Menu::getId)
                    .collect(Collectors.toList()));

            dto.setSubMenuDTOList(template.getMenus().stream()
                    .map(menu -> convertMenuToSubMenuDto(menu, dto.getPrimaryMenuId()))
                    .collect(Collectors.toList()));
        } else {
            dto.setMenuIds(new ArrayList<>());
            dto.setSubMenuDTOList(new ArrayList<>());
        }
        return dto;
    }

    private SubMenuDTO convertMenuToSubMenuDto(Menu menu, Long primaryMenuId) {
        SubMenuDTO subMenuDTO = new SubMenuDTO();
        subMenuDTO.setId(menu.getId());
        subMenuDTO.setTitle(menu.getTitle());
        subMenuDTO.setUrl(menu.getUrl());
        subMenuDTO.setPrimaryMenu(primaryMenuId != null && primaryMenuId.equals(menu.getId()));
        if (menu.getParentMenu() != null) {
            subMenuDTO.setParentMenuId(menu.getParentMenu().getId());
            subMenuDTO.setParentMenuTitle(menu.getParentMenu().getTitle());
        }
        return subMenuDTO;
    }
}
