package com.alper.fitnesstracker.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore; // 👈 EKLENDİ

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

    // 👇 BURASI KRİTİK: @JsonIgnore eklendi.
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    private Double weight;

    private LocalDateTime date;

    @PrePersist
    public void onCreate() {
        this.date = LocalDateTime.now();
    }
}