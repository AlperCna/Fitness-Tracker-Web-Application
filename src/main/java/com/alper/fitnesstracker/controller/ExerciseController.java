package com.alper.fitnesstracker.controller;

import com.alper.fitnesstracker.entity.Exercise;
import com.alper.fitnesstracker.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exercises")
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;

    // Tüm hareketleri listele
    @GetMapping
    public ResponseEntity<List<Exercise>> getAllExercises() {
        return ResponseEntity.ok(exerciseService.getAllExercises());
    }

    // Kategoriye göre hareketleri listele
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Exercise>> getExercisesByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(exerciseService.getExercisesByCategory(categoryId));
    }

    // Yeni exercise ekle (kategori ID parametre olarak alınır)
    @PostMapping("/{categoryId}")
    public ResponseEntity<Exercise> addExercise(
            @RequestBody Exercise exercise,
            @PathVariable Long categoryId
    ) {
        return ResponseEntity.ok(exerciseService.addExercise(exercise, categoryId));
    }

    // Exercise sil
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExercise(@PathVariable Long id) {
        exerciseService.deleteExercise(id);
        return ResponseEntity.noContent().build();
    }
}
