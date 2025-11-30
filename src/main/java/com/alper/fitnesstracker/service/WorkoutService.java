package com.alper.fitnesstracker.service;

import com.alper.fitnesstracker.dto.WorkoutItemDTO;
import com.alper.fitnesstracker.dto.WorkoutRequest;
import com.alper.fitnesstracker.entity.Exercise;
import com.alper.fitnesstracker.entity.User;
import com.alper.fitnesstracker.entity.WorkoutDetails;
import com.alper.fitnesstracker.entity.WorkoutSession;
import com.alper.fitnesstracker.repository.ExerciseRepository;
import com.alper.fitnesstracker.repository.UserRepository;
import com.alper.fitnesstracker.repository.WorkoutDetailRepository;
import com.alper.fitnesstracker.repository.WorkoutSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkoutService {

    private final WorkoutSessionRepository workoutSessionRepository;
    private final WorkoutDetailRepository workoutDetailRepository;
    private final UserRepository userRepository;
    private final ExerciseRepository exerciseRepository;

    // --- YENİ ANTRENMAN KAYDET ---
    @Transactional // Hata olursa her şeyi geri al (Rollback)
    public WorkoutSession createWorkout(String email, WorkoutRequest request) {

        // 1. Kullanıcıyı bul
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));

        // 2. Ana Session'ı oluştur (Tarih yoksa şu anı al)
        WorkoutSession session = WorkoutSession.builder()
                .user(user)
                .date(request.getDate() != null ? request.getDate() : LocalDateTime.now())
                .duration(request.getDuration())
                .build();

        // Session'ı kaydet ki ID oluşsun
        WorkoutSession savedSession = workoutSessionRepository.save(session);

        // 3. Listeyi dön ve Detayları (Egzersizleri) kaydet
        if (request.getItems() != null) {
            for (WorkoutItemDTO item : request.getItems()) {

                Exercise exercise = exerciseRepository.findById(item.getExerciseId())
                        .orElseThrow(() -> new RuntimeException("Egzersiz bulunamadı! ID: " + item.getExerciseId()));

                WorkoutDetails details = WorkoutDetails.builder()
                        .session(savedSession) // Hangi session'a bağlı?
                        .exercise(exercise)    // Hangi hareket?
                        .sets(item.getSets())
                        .reps(item.getReps())
                        .weight(item.getWeight())
                        .build();

                workoutDetailRepository.save(details);
            }
        }

        return savedSession;
    }

    // --- GEÇMİŞ ANTRENMANLARI GETİR ---
    public List<WorkoutSession> getMyWorkouts(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));

        // Kullanıcının ID'sine göre sessionları getir
        return workoutSessionRepository.findByUserId(user.getId());
    }
}