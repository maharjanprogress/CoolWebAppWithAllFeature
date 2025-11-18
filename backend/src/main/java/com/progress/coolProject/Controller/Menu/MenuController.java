package com.progress.coolProject.Controller.Menu;

import com.progress.coolProject.DTO.Menu.Input.MenuDTO;
import com.progress.coolProject.DTO.ResponseDTO;
import com.progress.coolProject.Services.Impl.menu.IMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class MenuController {

    private final IMenuService menuService;

    @PostMapping
    public ResponseEntity<ResponseDTO> createMenu(@RequestBody MenuDTO menuDTO) {
        return new ResponseEntity<>(ResponseDTO.success(menuService.createMenu(menuDTO), "Menu created"), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getMenuById(@PathVariable Long id) {
        return ResponseEntity.ok(ResponseDTO.success(menuService.getMenuById(id), "Menu retrieved"));
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> getAllMenus() {
        return ResponseEntity.ok(ResponseDTO.success(menuService.getAllMenus(), "All menus retrieved"));
    }

    @GetMapping("/sub-menus")
    public ResponseEntity<ResponseDTO> getAllSubMenus() {
        List<MenuDTO> subMenus = menuService.getAllSubMenus();
        return ResponseEntity.ok(ResponseDTO.success(subMenus, "All sub-menus retrieved"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> updateMenu(@PathVariable Long id, @RequestBody MenuDTO menuDTO) {
        return ResponseEntity.ok(ResponseDTO.success(menuService.updateMenu(id, menuDTO), "Menu updated"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteMenu(@PathVariable Long id) {
        menuService.deleteMenu(id);
        return ResponseEntity.ok(ResponseDTO.success("Menu deleted successfully"));
    }
}