package com.alper.fitnesstracker.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore; // 👈 EKLENDİ

@Entity
@Table(name = "workout_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "session_id")
    @JsonBackReference
    // Ekstra güvenlik için opsiyonel olarak @JsonIgnore da eklenebilir ama
    // JsonBackReference genelde yeterlidir. Şimdilik böyle kalsın.
    private WorkoutSession session;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    private Integer sets;

    private Integer reps;

    private Double weight;
}