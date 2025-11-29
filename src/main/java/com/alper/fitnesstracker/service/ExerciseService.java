package com.alper.fitnesstracker.service;

import com.alper.fitnesstracker.entity.Exercise;
import com.alper.fitnesstracker.entity.Category;
import com.alper.fitnesstracker.repository.ExerciseRepository;
import com.alper.fitnesstracker.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final CategoryRepository categoryRepository;

    // Tüm hareketleri listele
    public List<Exercise> getAllExercises() {
        return exerciseRepository.findAll();
    }

    // Belirli kategoriye göre hareketleri listele
    public List<Exercise> getExercisesByCategory(Long categoryId) {
        return exerciseRepository.findByCategoryId(categoryId);
    }

    // Yeni exercise ekle
    public Exercise addExercise(Exercise exercise, Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Kategori bulunamadı!"));

        exercise.setCategory(category);

        return exerciseRepository.save(exercise);
    }

    // Exercise sil
    public void deleteExercise(Long id) {
        exerciseRepository.deleteById(id);
    }
}
