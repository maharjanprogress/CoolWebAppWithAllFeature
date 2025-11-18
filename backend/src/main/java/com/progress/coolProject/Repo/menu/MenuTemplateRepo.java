package com.progress.coolProject.Repo.menu;

import com.progress.coolProject.Entity.Menu.MenuTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuTemplateRepo extends JpaRepository<MenuTemplate, Long> {
    Optional<MenuTemplate> findByRoleId(Long roleId);

    List<MenuTemplate> findByMenus_Id(Long menuId);
}