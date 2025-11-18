package com.progress.coolProject.DTO.Menu.Input;

import com.progress.coolProject.DTO.Menu.Output.SubMenuDTO;
import lombok.Data;

import java.util.List;

@Data
public class MenuTemplateDTO {
    private Long roleId;
    private List<Long> menuIds;
    private List<SubMenuDTO> subMenuDTOList;
}
