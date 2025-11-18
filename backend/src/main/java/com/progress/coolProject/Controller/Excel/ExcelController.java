package com.progress.coolProject.Controller.Excel;

import com.progress.coolProject.DTO.ResponseDTO;
import com.progress.coolProject.Entity.Excel.ProcessingJob;
import com.progress.coolProject.Services.Impl.Excel.IExcelService;
import com.progress.coolProject.Utils.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/excel")
@RequiredArgsConstructor
public class ExcelController {
    private final CurrentUser currentUser;
    private final IExcelService excelService;

    @PostMapping("/upload")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ResponseDTO> uploadExcel(@RequestParam("file") MultipartFile file) {
        ProcessingJob job = excelService.startProcessing(file, currentUser.getCurrentUser());
        return ResponseEntity.ok(ResponseDTO.success("File uploaded successfully. Job ID: " + job.getId(), job));
    }

    @GetMapping("/status")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ResponseDTO> getStatus() {
        ProcessingJob job = excelService.getActiveJob(currentUser.getCurrentUser());
        return ResponseEntity.ok(ResponseDTO.success("Job ID: " + job.getId()+ ". Currently has status: "+job.getStatus().toString(), job));
    }
}