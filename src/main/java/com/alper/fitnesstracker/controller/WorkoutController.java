package com.alper.fitnesstracker.controller;

import com.alper.fitnesstracker.dto.WorkoutRequest;
import com.alper.fitnesstracker.entity.WorkoutSession;
import com.alper.fitnesstracker.security.JwtUtils;
import com.alper.fitnesstracker.service.WorkoutService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/workouts")
@RequiredArgsConstructor
public class WorkoutController {

    private final WorkoutService workoutService;
    private final JwtUtils jwtUtils;

    // -------------------------------------------------------
    // 1) Antrenman Oluştur (POST)
    // -------------------------------------------------------
    @PostMapping
    public ResponseEntity<WorkoutSession> createWorkout(
            @RequestBody WorkoutRequest request,
            HttpServletRequest httpRequest
    ) {
        // --- JWT Token'dan email çek ---
        String token = extractToken(httpRequest);
        String email = jwtUtils.extractUsername(token);

        WorkoutSession session = workoutService.createWorkout(email, request);
        return ResponseEntity.ok(session);
    }

    // -------------------------------------------------------
    // 2) Kullanıcının geçmiş antrenmanlarını getir (GET)
    // -------------------------------------------------------
    @GetMapping
    public ResponseEntity<List<WorkoutSession>> getMyWorkouts(HttpServletRequest httpRequest) {

        String token = extractToken(httpRequest);
        String email = jwtUtils.extractUsername(token);

        List<WorkoutSession> sessions = workoutService.getMyWorkouts(email);

        return ResponseEntity.ok(sessions);
    }

    // -------------------------------------------------------
    //  Yardımcı Metot: Header'dan token çıkar
    // -------------------------------------------------------
    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            throw new RuntimeException("Token bulunamadı!");
        }

        return header.substring(7); // "Bearer " sonrası
    }
}
