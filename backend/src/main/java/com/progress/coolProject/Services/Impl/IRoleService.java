package com.progress.coolProject.Services.Impl;


import com.progress.coolProject.DTO.Roles.RoleDTO;
import com.progress.coolProject.Entity.Role;

import java.util.List;

public interface IRoleService {
    void createRole(RoleDTO roleDTO);
    List<Role> getAllRoles();
}
