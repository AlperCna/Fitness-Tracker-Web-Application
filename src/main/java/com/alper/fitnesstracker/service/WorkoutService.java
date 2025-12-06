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
import java.util.Set;

@Service
@RequiredArgsConstructor
public class WorkoutService {

    private final WorkoutSessionRepository workoutSessionRepository;
    private final WorkoutDetailRepository workoutDetailRepository;
    private final UserRepository userRepository;
    private final ExerciseRepository exerciseRepository;

    // --- YENİ ANTRENMAN KAYDET ---
    @Transactional
    public WorkoutSession createWorkout(String email, WorkoutRequest request) {

        // 1. Kullanıcıyı bul
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));

        // 2. Ana Session'ı oluştur
        WorkoutSession session = WorkoutSession.builder()
                .user(user)
                // 🔥 KRİTİK GÜNCELLEME BURADA:
                // Frontend'den gelen LocalDate'i -> LocalDateTime'a çeviriyoruz (.atStartOfDay())
                .date(request.getDate() != null ? request.getDate().atStartOfDay() : LocalDateTime.now())
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

        return workoutSessionRepository.findByUserId(user.getId());
    }
// ... diğer metodlar ...

    // --- ANTRENMAN SİLME METODU ---
    public void deleteWorkout(Long workoutId, String email) {
        // 1. İşlemi yapan kullanıcıyı bul
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));

        // 2. Silinmek istenen antrenmanı bul
        WorkoutSession session = workoutSessionRepository.findById(workoutId)
                .orElseThrow(() -> new RuntimeException("Antrenman bulunamadı!"));

        // 3. GÜVENLİK KONTROLÜ: Bu antrenman gerçekten bu kullanıcıya mı ait?
        if (!session.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bu antrenmanı silmeye yetkiniz yok!");
        }

        // 4. Sil (WorkoutDetails tablosundaki kayıtlar Cascade sayesinde otomatik silinir)
        workoutSessionRepository.delete(session);
    }

    // --- ID İLE TEK ANTRENMAN GETİR ---
    public WorkoutSession getWorkoutById(Long id, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));

        WorkoutSession session = workoutSessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Antrenman bulunamadı!"));

        // Güvenlik: Başkasının antrenmanını göremezsin
        if (!session.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Erişim yetkiniz yok!");
        }
        return session;
    }

    // --- ANTRENMAN GÜNCELLE ---
    @Transactional
    public WorkoutSession updateWorkout(Long id, WorkoutRequest request, String email) {
        WorkoutSession session = getWorkoutById(id, email); // Önce bul ve yetkiyi kontrol et

        // 1. Ana Bilgileri Güncelle
        // Frontend'den gelen LocalDate'i LocalDateTime'a çeviriyoruz
        session.setDate(request.getDate().atStartOfDay());
        session.setDuration(request.getDuration());

        // 2. Eski Detayları Temizle
        // HATA DÜZELTİLDİ: List yerine Set kullanıyoruz.
        // session.getDetails() Set döndürür, deleteAll Set kabul eder. Çevirmeye gerek yok.
        Set<WorkoutDetails> oldDetails = session.getDetails();
        workoutDetailRepository.deleteAll(oldDetails);
        session.getDetails().clear(); // Hafızayı da temizle

        // 3. Yeni Detayları Ekle
        if (request.getItems() != null) {
            for (WorkoutItemDTO item : request.getItems()) {
                Exercise exercise = exerciseRepository.findById(item.getExerciseId())
                        .orElseThrow(() -> new RuntimeException("Egzersiz bulunamadı!"));

                WorkoutDetails details = WorkoutDetails.builder()
                        .session(session)
                        .exercise(exercise)
                        .sets(item.getSets())
                        .reps(item.getReps())
                        .weight(item.getWeight())
                        .build();

                // İlişkiyi kur ve kaydet
                workoutDetailRepository.save(details);
            }
        }

        return workoutSessionRepository.save(session);
    }
}