package com.alper.fitnesstracker.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIgnore; // 👈 EKLENDİ

import java.time.LocalDateTime;
import java.util.Set;

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

    // 👇 BURASI KRİTİK: @JsonIgnore eklendi.
    // Artık Workout -> User -> Workout döngüsü oluşmayacak.
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    private LocalDateTime date;

    private Integer duration;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonManagedReference
    private Set<WorkoutDetails> details;

    @PrePersist
    public void onCreate() {
        if (this.date == null) {
            this.date = LocalDateTime.now();
        }
    }
}