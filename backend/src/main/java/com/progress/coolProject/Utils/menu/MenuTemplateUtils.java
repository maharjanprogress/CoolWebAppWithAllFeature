package com.progress.coolProject.Utils.menu;

import com.progress.coolProject.DTO.Menu.Input.MenuTemplateDTO;
import com.progress.coolProject.DTO.Menu.Output.SubMenuDTO;
import com.progress.coolProject.Entity.Menu.Menu;
import com.progress.coolProject.Entity.Menu.MenuTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MenuTemplateUtils {

    public MenuTemplateDTO convertToDto(MenuTemplate template) {
        MenuTemplateDTO dto = new MenuTemplateDTO();
        dto.setRoleId(template.getRole().getId());

        if (template.getMenus() != null) {
            dto.setMenuIds(template.getMenus().stream()
                    .map(Menu::getId)
                    .collect(Collectors.toList()));

            dto.setSubMenuDTOList(template.getMenus().stream()
                    .map(this::convertMenuToSubMenuDto)
                    .collect(Collectors.toList()));
        } else {
            dto.setMenuIds(new ArrayList<>());
            dto.setSubMenuDTOList(new ArrayList<>());
        }
        return dto;
    }

    private SubMenuDTO convertMenuToSubMenuDto(Menu menu) {
        SubMenuDTO subMenuDTO = new SubMenuDTO();
        subMenuDTO.setId(menu.getId());
        subMenuDTO.setTitle(menu.getTitle());
        subMenuDTO.setUrl(menu.getUrl());
        return subMenuDTO;
    }
}