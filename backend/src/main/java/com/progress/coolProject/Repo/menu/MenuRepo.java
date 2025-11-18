package com.progress.coolProject.Repo.menu;

import com.progress.coolProject.Entity.Menu.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepo extends JpaRepository<Menu, Long> {
    List<Menu> findByParentMenuIsNotNull();
}