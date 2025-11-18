package com.progress.coolProject.Entity.Excel;

import com.progress.coolProject.Entity.User;
import com.progress.coolProject.Enums.JobStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "processing_jobs")
public class ProcessingJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private JobStatus status; // PENDING, PROCESSING, COMPLETED, FAILED

    @Column(name = "progress")
    private Integer progress = 0; // 0-100

    @Column(name = "current_step")
    private String currentStep;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}