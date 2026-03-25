package com.progress.coolProject.Repo.menu;

import com.progress.coolProject.Entity.Menu.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepo extends JpaRepository<Menu, Long> {
    List<Menu> findByParentMenuIsNotNull();

    @Query("SELECT m FROM Menu m WHERE m.title = :title AND " +
            "(:parentId IS NULL AND m.parentMenu IS NULL OR m.parentMenu.id = :parentId)")
    Optional<Menu> findDistinctFirstByTitleAndParentMenu_Id(
            @Param("title") String title,
            @Param("parentId") Long parentId
    );
}