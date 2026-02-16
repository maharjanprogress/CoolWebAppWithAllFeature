package com.progress.coolProject.Services.Excel;

import com.progress.coolProject.Entity.Excel.ProcessingJob;
import com.progress.coolProject.Utils.CurrentUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExcelServiceTest {
    @Mock
    ExcelService excelService;
    @Mock
    CurrentUser currentUser;

    @Test
    void myFirstTest(){
        ProcessingJob job = excelService.getActiveJob(currentUser.getCurrentUser());
        System.out.println(job);

    }

}