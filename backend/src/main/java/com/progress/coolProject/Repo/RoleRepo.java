package com.progress.coolProject.Repo;

import com.progress.coolProject.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {

    boolean existsByRoleAlias(String roleAlias);

    Optional<Role> findByRoleAlias(String roleAlias);
}
