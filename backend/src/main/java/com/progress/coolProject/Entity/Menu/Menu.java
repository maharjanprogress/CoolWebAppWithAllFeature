package com.progress.coolProject.Entity.Menu;

import com.progress.coolProject.Enums.MenuType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "menus")
@Data
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "menu_type", nullable = false)
    private MenuType menuType;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "url")
    private String url;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Menu parentMenu;
}
