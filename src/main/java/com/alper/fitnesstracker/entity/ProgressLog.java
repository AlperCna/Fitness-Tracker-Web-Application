package com.alper.fitnesstracker.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "progress_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgressLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Bu gelişim bilgisi hangi kullanıcıya ait?
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Kullanıcının o günkü kilosu
    private Double weight;

    // Giriş tarihi (otomatik atanır)
    private LocalDateTime date;

    @PrePersist
    public void onCreate() {
        this.date = LocalDateTime.now();
    }
}
