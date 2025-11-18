package com.progress.coolProject.Services.Impl.menu;


import com.progress.coolProject.DTO.Menu.Input.MenuDTO;

import java.util.List;

public interface IMenuService {
    MenuDTO createMenu(MenuDTO menuDTO);
    MenuDTO getMenuById(Long id);
    List<MenuDTO> getAllMenus();
    List<MenuDTO> getAllSubMenus();
    MenuDTO updateMenu(Long id, MenuDTO menuDTO);
    void deleteMenu(Long id);
}