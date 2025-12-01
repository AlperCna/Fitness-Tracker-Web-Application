package com.alper.fitnesstracker.service;

import com.alper.fitnesstracker.dto.ProgressLogRequest;
import com.alper.fitnesstracker.entity.ProgressLog;
import com.alper.fitnesstracker.entity.User;
import com.alper.fitnesstracker.repository.ProgressLogRepository;
import com.alper.fitnesstracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgressService {

    private final ProgressLogRepository progressLogRepository;
    private final UserRepository userRepository;

    // --- Kilo Kaydı Ekle ---
    public ProgressLog addLog(String email, ProgressLogRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        // Tarih gönderilmediyse: şu an
        // Tarih gönderildiyse: o günün sabahı
        LocalDateTime logDate = (request.getDate() != null)
                ? request.getDate().atStartOfDay()
                : LocalDateTime.now();

        ProgressLog log = ProgressLog.builder()
                .user(user)
                .weight(request.getWeight())
                .date(logDate)
                .build();

        return progressLogRepository.save(log);
    }

    // --- Kullanıcının kendi geçmişini getir ---
    public List<ProgressLog> getMyProgress(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        return progressLogRepository.findByUserIdOrderByDateDesc(user.getId());
    }

    // --- Kayıt sil ---
    public void deleteLog(Long id) {
        progressLogRepository.deleteById(id);
    }
}
