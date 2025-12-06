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

    // -------------------------------------------------------
    // 3) Antrenman Sil (DELETE)
    // -------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWorkout(@PathVariable Long id, HttpServletRequest httpRequest) {
        // Token'dan kullanıcıyı tanı
        String token = extractToken(httpRequest);
        String email = jwtUtils.extractUsername(token);

        // Servisi çağır
        workoutService.deleteWorkout(id, email);

        return ResponseEntity.ok("Antrenman başarıyla silindi.");
    }


    // -------------------------------------------------------
    // 4) Tek Bir Antrenmanı Getir (GET /{id}) - Edit Sayfası İçin
    // -------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<WorkoutSession> getWorkoutById(
            @PathVariable Long id,
            HttpServletRequest httpRequest
    ) {
        String token = extractToken(httpRequest);
        String email = jwtUtils.extractUsername(token);

        return ResponseEntity.ok(workoutService.getWorkoutById(id, email));
    }

    // -------------------------------------------------------
    // 5) Antrenmanı Güncelle (PUT /{id})
    // -------------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<WorkoutSession> updateWorkout(
            @PathVariable Long id,
            @RequestBody WorkoutRequest request,
            HttpServletRequest httpRequest
    ) {
        String token = extractToken(httpRequest);
        String email = jwtUtils.extractUsername(token);

        WorkoutSession updatedSession = workoutService.updateWorkout(id, request, email);
        return ResponseEntity.ok(updatedSession);
    }
}
