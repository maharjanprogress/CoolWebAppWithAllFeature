package com.progress.coolProject.Services.Role;

import com.progress.coolProject.DTO.Roles.RoleDTO;
import com.progress.coolProject.Entity.Role;
import com.progress.coolProject.Repo.RoleRepo;
import com.progress.coolProject.Services.Impl.IRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RoleService implements IRoleService {

    @Autowired
    private RoleRepo roleRepo;

    @Override
    public void createRole(RoleDTO roleDTO) {
        log.info("Creating role with alias: {}", roleDTO.getRoleAlias());
        Role roleEntity = new Role();
        if (roleRepo.existsByRoleAlias(roleDTO.getRoleAlias())) {
            throw new IllegalArgumentException("Role alias already exists");
        }
        roleEntity.setRoleAlias(roleDTO.getRoleAlias());
        roleEntity.setRoleName(roleDTO.getRoleName());
        roleEntity.setRemarks(roleDTO.getRemarks());
        roleRepo.save(roleEntity);
        log.info("Role created successfully: {}", roleEntity);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepo.findAll();
    }
}
