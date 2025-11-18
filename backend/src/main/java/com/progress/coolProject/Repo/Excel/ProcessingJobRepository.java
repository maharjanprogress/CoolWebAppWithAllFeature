package com.progress.coolProject.Repo.Excel;

import com.progress.coolProject.Entity.Excel.ProcessingJob;
import com.progress.coolProject.Entity.User;
import com.progress.coolProject.Enums.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProcessingJobRepository extends JpaRepository<ProcessingJob, Long> {
    Optional<ProcessingJob> findFirstByUserAndStatusInOrderByCreatedAtDesc(User user, List<JobStatus> status);
}
