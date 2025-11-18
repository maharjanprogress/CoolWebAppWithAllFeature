package com.progress.coolProject.Services.Excel;

import com.progress.coolProject.DTO.Excel.ProgressUpdate;
import com.progress.coolProject.Entity.Excel.ProcessingJob;
import com.progress.coolProject.Entity.User;
import com.progress.coolProject.Enums.JobStatus;
import com.progress.coolProject.Repo.Excel.ProcessingJobRepository;
import com.progress.coolProject.Services.Impl.Excel.IExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExcelService implements IExcelService {
    private final ProcessingJobRepository jobRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public ProcessingJob startProcessing(MultipartFile file, User user) {
        // Check if user already has an active job
        Optional<ProcessingJob> existingJob = jobRepository.findFirstByUserAndStatusInOrderByCreatedAtDesc(
                user,
                List.of(JobStatus.PENDING, JobStatus.PROCESSING)
        );

        if (existingJob.isPresent()) {
            throw new RuntimeException("You already have a file being processed. Please wait.");
        }

        // Validate file
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        String fileName = file.getOriginalFilename();
//        if (fileName == null || !fileName.endsWith(".xlsx")) {
//            throw new RuntimeException("Only .xlsx files are allowed");
//        }

        // Create job record
        ProcessingJob job = new ProcessingJob();
        job.setUser(user);
        job.setFileName(fileName);
        job.setStatus(JobStatus.PENDING);
        job.setStartedAt(LocalDateTime.now());
        job = jobRepository.save(job);

        // Process asynchronously
        processExcelAsync(job, file);

        return job;
    }

    @Override
    public ProcessingJob getActiveJob(User user) {
        return jobRepository.findFirstByUserAndStatusInOrderByCreatedAtDesc(
                user,
                List.of(JobStatus.PENDING, JobStatus.PROCESSING)
        ).orElse(null);
    }

    @Async
    public void processExcelAsync(ProcessingJob job, MultipartFile file) {
        try {
            // Update status to processing
            job.setStatus(JobStatus.PROCESSING);
            jobRepository.save(job);
            sendProgress(job, 0, "Starting processing...");

            // YOUR ACTUAL EXCEL PROCESSING LOGIC HERE
            // Example:
            sendProgress(job, 10, "Reading Excel file...");
            // Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Thread.sleep(20000); // Simulate work

            sendProgress(job, 30, "Validating data...");
            // Your validation logic
            Thread.sleep(2000);

            sendProgress(job, 50, "Processing rows...");
            // Your row processing logic
            Thread.sleep(3000);

            sendProgress(job, 80, "Saving to database...");
            // Your database save logic
            Thread.sleep(10000);

            sendProgress(job, 100, "Completed!");

            // Mark as completed
            job.setStatus(JobStatus.COMPLETED);
            job.setProgress(100);
            job.setCompletedAt(LocalDateTime.now());
            jobRepository.save(job);

            // Send completion message
            sendStatusUpdate(job, "Processing completed successfully!");

        } catch (Exception e) {
            job.setStatus(JobStatus.FAILED);
            job.setErrorMessage(e.getMessage());
            job.setCompletedAt(LocalDateTime.now());
            jobRepository.save(job);

            sendStatusUpdate(job, "Processing failed: " + e.getMessage());
        }
    }

    private void sendProgress(ProcessingJob job, int progress, String message) {
        job.setProgress(progress);
        job.setCurrentStep(message);
        jobRepository.save(job);

        sendStatusUpdate(job, message);
    }

    private void sendStatusUpdate(ProcessingJob job, String message) {
        ProgressUpdate update = new ProgressUpdate(
                job.getId(),
                job.getProgress(),
                message,
                job.getStatus()
        );

        // IMPORTANT: Use the USERNAME (not userId) to send to specific user
        messagingTemplate.convertAndSendToUser(
                job.getUser().getUserName(),  // This must match the username in JWT
                "/queue/excel-status",
                update
        );

        System.out.println("Sent progress to user: " + job.getUser().getUserName()
                + " - " + job.getProgress() + "%");
    }
}