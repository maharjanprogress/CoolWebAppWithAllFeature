package com.progress.coolProject.DTO.Menu.Input;

import com.progress.coolProject.Enums.MenuType;
import lombok.Data;

@Data
public class MenuDTO {
    private Long id;
    private MenuType menuType;
    private String title;
    private String url;
    private Long parentId; // Use ID for simplicity in transfer
}