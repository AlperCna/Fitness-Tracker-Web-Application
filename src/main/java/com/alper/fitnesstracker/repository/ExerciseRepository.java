package com.alper.fitnesstracker.repository;

import com.alper.fitnesstracker.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    // Belirli bir kategoriye ait egzersizleri getir
    // SQL Karşılığı: SELECT * FROM exercises WHERE category_id = ?
    List<Exercise> findByCategoryId(Long categoryId);

    Optional<Exercise> findByApiReferenceId(String apiReferenceId);
}