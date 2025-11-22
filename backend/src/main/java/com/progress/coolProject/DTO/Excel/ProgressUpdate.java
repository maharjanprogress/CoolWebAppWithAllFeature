package com.progress.coolProject.DTO.Excel;

import com.progress.coolProject.Enums.JobStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProgressUpdate {
    private Long jobId;
    private Integer progress;
    private String message;
    private JobStatus status;
    private Boolean excelComplete;
    private Boolean powerpointComplete;
}
