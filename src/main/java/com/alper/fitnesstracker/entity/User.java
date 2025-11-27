package com.alper.fitnesstracker.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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

    // --- EKSİK OLAN KISIM BURASIYDI ---
    // Kullanıcı ile Rolleri birbirine bağlıyoruz (Çoka-Çok İlişki)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
    // ----------------------------------

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}