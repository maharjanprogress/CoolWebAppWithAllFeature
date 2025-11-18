package com.progress.coolProject.Entity.Menu;

import com.progress.coolProject.Entity.Role;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "menu_templates")
@Data
public class MenuTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ManyToMany
    @JoinTable(
            name = "menu_template_menus",
            joinColumns = @JoinColumn(name = "menu_template_id"),
            inverseJoinColumns = @JoinColumn(name = "menu_id")
    )
    private List<Menu> menus;
}
