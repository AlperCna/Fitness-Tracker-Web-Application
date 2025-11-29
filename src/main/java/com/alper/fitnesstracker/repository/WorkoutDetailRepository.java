package com.alper.fitnesstracker.repository;

import com.alper.fitnesstracker.entity.WorkoutDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutDetailRepository extends JpaRepository<WorkoutDetails, Long> {
}
