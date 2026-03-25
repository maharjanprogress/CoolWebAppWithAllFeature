package com.progress.coolProject.Initializer;

import com.progress.coolProject.DTO.Menu.Input.MenuDTO;
import com.progress.coolProject.DTO.Menu.Input.MenuTemplateDTO;
import com.progress.coolProject.Enums.MenuType;
import com.progress.coolProject.Services.Impl.IRoleService;
import com.progress.coolProject.Services.Impl.menu.IMenuService;
import com.progress.coolProject.Services.Impl.menu.IMenuTemplateService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuInitializer {
    private final IMenuService menuService;
    private final IMenuTemplateService menuTemplateService;
    private final IRoleService rolesService;

    private static final Logger logger = LoggerFactory.getLogger(MenuInitializer.class);


    @PostConstruct
    @Transactional
    public void createMenusAndTemplate(){
        logger.info("Creating Menus");
        createMenu();

    }

    private void  createMenu(){
        MenuDTO excelSuperMenu = new MenuDTO();
        excelSuperMenu.setMenuType(MenuType.SUPER);
        excelSuperMenu.setTitle("Excel");
        excelSuperMenu.setUrl(null);
        excelSuperMenu.setParentId(null);
        createMenu(excelSuperMenu);

        MenuDTO excelSubMenu1 = new MenuDTO();
        excelSubMenu1.setMenuType(MenuType.SUB);
        excelSubMenu1.setTitle("Generate Monthly Presentation");
        excelSubMenu1.setUrl("trialBalance");
        excelSubMenu1.setParentId(excelSuperMenu.getId());
        createMenu(excelSubMenu1);

        MenuDTO chatSuperMenu = new MenuDTO();
        chatSuperMenu.setMenuType(MenuType.SUPER);
        chatSuperMenu.setTitle("Chat");
        chatSuperMenu.setUrl(null);
        chatSuperMenu.setParentId(null);
        createMenu(chatSuperMenu);

        MenuDTO chatSubMenu1 = new MenuDTO();
        chatSubMenu1.setMenuType(MenuType.SUB);
        chatSubMenu1.setTitle("Global Chat");
        chatSubMenu1.setUrl("allChat");
        chatSubMenu1.setParentId(chatSuperMenu.getId());
        createMenu(chatSubMenu1);
//
//
//        MenuDTO experienceSuperMenu = new MenuDTO();
//        experienceSuperMenu.setMenuType(MenuType.SUPER);
//        experienceSuperMenu.setTitle("Experience");
//        experienceSuperMenu.setUrl(null);
//        experienceSuperMenu.setParentId(null);
//        createMenu(experienceSuperMenu);
//
//        MenuDTO experienceSubMenu1 = new MenuDTO();
//        experienceSubMenu1.setMenuType(MenuType.SUB);
//        experienceSubMenu1.setTitle("View Experience");
//        experienceSubMenu1.setUrl("viewExperience");
//        experienceSubMenu1.setParentId(experienceSuperMenu.getId());
//        createMenu(experienceSubMenu1);
//
//        MenuDTO aiProfileSuperMenu = new MenuDTO();
//        aiProfileSuperMenu.setMenuType(MenuType.SUPER);
//        aiProfileSuperMenu.setTitle("AI Profie");
//        aiProfileSuperMenu.setUrl(null);
//        aiProfileSuperMenu.setParentId(null);
//        createMenu(aiProfileSuperMenu);
//
//        MenuDTO aiProfileSubMenu1 = new MenuDTO();
//        aiProfileSubMenu1.setMenuType(MenuType.SUB);
//        aiProfileSubMenu1.setTitle("View AI Profie");
//        aiProfileSubMenu1.setUrl("myAIProfile");
//        aiProfileSubMenu1.setParentId(aiProfileSuperMenu.getId());
//        createMenu(aiProfileSubMenu1);
//
//        MenuDTO overviewSuperMenu = new MenuDTO();
//        overviewSuperMenu.setMenuType(MenuType.SUPER);
//        overviewSuperMenu.setTitle("Overview");
//        overviewSuperMenu.setUrl(null);
//        overviewSuperMenu.setParentId(null);
//        createMenu(overviewSuperMenu);
//
//        MenuDTO overviewSubMenu1 = new MenuDTO();
//        overviewSubMenu1.setMenuType(MenuType.SUB);
//        overviewSubMenu1.setTitle("Guide");
//        overviewSubMenu1.setUrl("guide");
//        overviewSubMenu1.setParentId(overviewSuperMenu.getId());
//        createMenu(overviewSubMenu1);


        makeMenuTemplate(
                "ADMIN",getListofMenuIds(
                        excelSubMenu1,
                        chatSubMenu1
                )
        );

        makeMenuTemplate(
                "CLIENT",getListofMenuIds(
                        chatSubMenu1
                )
        );

        makeMenuTemplate(
                "MOM",getListofMenuIds(
                        excelSubMenu1
                )
        );

    }

    private List<Long> getListofMenuIds(MenuDTO... menuDTOs) {
        return Arrays.stream(menuDTOs)
                .map(MenuDTO::getId)
                .toList();
    }

    private void makeMenuTemplate(String roleAlias, List<Long> subMenuIds) {
        logger.info("Creating menu Template for {}", roleAlias);
        MenuTemplateDTO menuTemplateDTO = new MenuTemplateDTO();
        try {
            menuTemplateDTO.setRoleId(rolesService.getRoleByRoleAlias(roleAlias).getId());
            menuTemplateDTO.setMenuIds(subMenuIds);
            menuTemplateService.createOrUpdateTemplate(menuTemplateDTO);
            logger.info("created menu for {}",roleAlias);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private void createMenu(MenuDTO menuDTO){
        try {
            logger.info("Trying to create {} menu...", menuDTO.getTitle());
            menuService.createMenu(menuDTO);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

}
