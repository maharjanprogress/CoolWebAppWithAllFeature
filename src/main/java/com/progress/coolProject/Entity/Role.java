package com.progress.coolProject.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name ="roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_alias", length = 20, nullable = false, unique = true)
    private String roleAlias;

    @Column(name = "role_name", length = 100, nullable = false, unique = true)
    private String roleName;

    @Column(name = "remarks")
    private String remarks;
}
