package com.alper.fitnesstracker.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List; // List importu eklendi
import java.util.Set;
import java.util.ArrayList; // ArrayList importu eklendi

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // --- MEVCUT ROL İLİŞKİSİ ---
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();


    // Kullanıcı silinirse, ona ait Antrenmanlar da silinsin
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkoutSession> workouts = new ArrayList<>();

    // Kullanıcı silinirse, ona ait Gelişim Logları da silinsin
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProgressLog> progressLogs = new ArrayList<>();

    // =================================================================

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}