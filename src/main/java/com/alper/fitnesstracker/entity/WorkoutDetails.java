package com.alper.fitnesstracker.entity;

import jakarta.persistence.*;
import lombok.*;

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

    // Bu set hangi antrenman gününe ait?
    @ManyToOne
    @JoinColumn(name = "session_id")
    private WorkoutSession session;

    // Hangi egzersiz?
    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    // Kaç set?
    private Integer sets;

    // Her sette kaç tekrar?
    private Integer reps;

    // Opsiyonel: kullanılan ağırlık (kg)
    private Double weight;
}
