package com.progress.coolProject.Controller.RoleController;

import com.progress.coolProject.DTO.ResponseDTO;
import com.progress.coolProject.DTO.Roles.RoleDTO;
import com.progress.coolProject.Entity.Role;
import com.progress.coolProject.Services.Impl.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    @Autowired
    private IRoleService roleService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> createRole(@RequestBody RoleDTO roleDTO) {
        roleService.createRole(roleDTO);
        return ResponseEntity.ok(ResponseDTO.success("Role created successfully"));
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseDTO> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        if (roles.isEmpty()) {
            return ResponseEntity.ok(ResponseDTO.error("No roles found"));
        }
        return ResponseEntity.ok(ResponseDTO.success(roles, "Roles fetched successfully"));
    }
}
