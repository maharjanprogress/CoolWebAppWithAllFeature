package com.progress.coolProject.Controller.Menu;

import com.progress.coolProject.DTO.Menu.Input.MenuTemplateDTO;
import com.progress.coolProject.DTO.Menu.Output.SuperMenuDTO;
import com.progress.coolProject.DTO.ResponseDTO;
import com.progress.coolProject.Entity.User;
import com.progress.coolProject.Services.Impl.menu.IMenuTemplateService;
import com.progress.coolProject.Utils.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu-templates")
@RequiredArgsConstructor
public class MenuTemplateController {

    private final IMenuTemplateService menuTemplateService;
    private final CurrentUser currentUser;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ResponseDTO> createOrUpdateTemplate(@RequestBody MenuTemplateDTO menuTemplateDTO) {
        MenuTemplateDTO result = menuTemplateService.createOrUpdateTemplate(menuTemplateDTO);
        return new ResponseEntity<>(ResponseDTO.success(result, "Menu template saved successfully"), HttpStatus.OK);
    }

    @GetMapping("/role")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ResponseDTO> getTemplateByRoleId() {
        User user = currentUser.getCurrentUser();
        Long roleId = user.getRole().getId();
        return ResponseEntity.ok(ResponseDTO.success(menuTemplateService.getTemplateByRoleId(roleId), "Template retrieved"));
    }

    @GetMapping("/role/formatted")
    public ResponseEntity<ResponseDTO> getFormattedMenuForRole() {
        User user = currentUser.getCurrentUser();
        Long roleId = user.getRole().getId();
        List<SuperMenuDTO> formattedMenu = menuTemplateService.getFormattedMenusByRoleId(roleId);
        return ResponseEntity.ok(ResponseDTO.success(formattedMenu, "Formatted menu for role retrieved successfully"));
    }

    @DeleteMapping("/menu/{menuId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ResponseDTO> deleteMenuFromAllTemplates(@PathVariable Long menuId) {
        menuTemplateService.deleteMenuFromAllTemplates(menuId);
        return ResponseEntity.ok(ResponseDTO.success("Menu removed from all templates successfully"));
    }
}