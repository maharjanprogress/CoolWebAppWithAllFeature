package com.progress.coolProject.Services.Impl.Excel;

import com.progress.coolProject.Entity.Excel.ProcessingJob;
import com.progress.coolProject.Entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface IExcelService {
    ProcessingJob startProcessing(
            MultipartFile trialBalance,
            MultipartFile profitAndLoss,
            MultipartFile balanceSheet,
            MultipartFile loanAgeingSheet,
            MultipartFile loanSummary,
            MultipartFile savingSummary,
            MultipartFile previousTrialBalance,
            User user
    );
    ProcessingJob getActiveJob(User user);
}
