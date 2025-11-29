package com.alper.fitnesstracker.repository;

import com.alper.fitnesstracker.entity.ProgressLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgressLogRepository extends JpaRepository<ProgressLog, Long> {
    List<ProgressLog> findByUserId(Long userId);
}
