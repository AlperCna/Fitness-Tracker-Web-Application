package com.alper.fitnesstracker.controller;

import com.alper.fitnesstracker.service.ExternalApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/external")
public class ExternalApiController {

    private final ExternalApiService externalApiService;

    @PostMapping("/sync")
    public ResponseEntity<String> syncExercises() {
        externalApiService.syncExercises();
        return ResponseEntity.ok("ExerciseDB sync completed!");
    }
}
