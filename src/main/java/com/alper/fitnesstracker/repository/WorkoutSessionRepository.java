package com.alper.fitnesstracker.repository;

import com.alper.fitnesstracker.entity.WorkoutSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkoutSessionRepository extends JpaRepository<WorkoutSession, Long> {

    List<WorkoutSession> findByUserId(Long userId);
}
