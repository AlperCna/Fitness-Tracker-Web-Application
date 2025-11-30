package com.alper.fitnesstracker.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class WorkoutRequest {

    private LocalDateTime date;     // kullanıcı tarih vermezse service içinde now() yapılacak
    private Integer duration;       // opsiyonel

    private List<WorkoutItemDTO> items; // Birden çok set/egzersiz
}
