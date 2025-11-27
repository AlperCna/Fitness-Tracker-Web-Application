package com.alper.fitnesstracker.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "workout_sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Bu session'ı hangi kullanıcı yaptı
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Antrenman tarihi
    private LocalDateTime date;

    // Opsiyonel: Antrenmanın süresi
    private Integer duration; // dakika

    @PrePersist
    public void onCreate() {
        if (this.date == null) {
            this.date = LocalDateTime.now();
        }
    }
}
