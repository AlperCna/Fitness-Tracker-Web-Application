package com.alper.fitnesstracker.dto;

import lombok.Data;

@Data
public class WorkoutItemDTO {
    private Long exerciseId;
    private Integer sets;
    private Integer reps;
    private Double weight;
}
