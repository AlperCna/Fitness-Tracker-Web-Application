package com.alper.fitnesstracker.dto;

import lombok.Data;
import java.time.LocalDate; // DİKKAT: LocalDateTime DEĞİL, LocalDate
import java.util.List;

@Data
public class WorkoutRequest {

    private LocalDate date;     // 🔥 GÜNCELLEME: Sadece tarih tutacak
    private Integer duration;

    private List<WorkoutItemDTO> items;
}