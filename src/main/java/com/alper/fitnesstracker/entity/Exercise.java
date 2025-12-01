package com.alper.fitnesstracker.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "exercises")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- ExerciseDB fields ---
    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "LONGTEXT") // <--- BU ÇOK ÖNEMLİ
    private String description;

    @Column(columnDefinition = "TEXT")     // <--- BU DA GARANTİ OLSUN
    private String gifUrl;

    @Column(name = "equipment")
    private String equipment;

    @Column(name = "target_muscle")
    private String target; // pectorals, hamstrings, biceps...

    @Column(name = "body_part")
    private String bodyPart; // chest, back, legs...

    @Column(name = "api_reference_id")
    private String apiReferenceId; // ExerciseDB → id (0025)

    // --- CATEGORY RELATION (your project requirement) ---
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
