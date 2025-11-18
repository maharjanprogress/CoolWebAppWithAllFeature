package com.progress.coolProject.Services.Impl.menu;


import com.progress.coolProject.DTO.Menu.Input.MenuTemplateDTO;
import com.progress.coolProject.DTO.Menu.Output.SuperMenuDTO;

import java.util.List;

public interface IMenuTemplateService {
    MenuTemplateDTO createOrUpdateTemplate(MenuTemplateDTO menuTemplateDTO);
    MenuTemplateDTO getTemplateByRoleId(Long roleId);
    List<SuperMenuDTO> getFormattedMenusByRoleId(Long roleId);
    void deleteMenuFromAllTemplates(Long menuId);
}