package com.progress.coolProject.DTO.Menu.Output;

import lombok.Data;

import java.util.List;

@Data
public class SuperMenuDTO {
    private Long id;
    private String title;
    private List<SubMenuDTO> subMenuDTOList;
}
