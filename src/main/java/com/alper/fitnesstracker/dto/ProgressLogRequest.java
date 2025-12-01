package com.alper.fitnesstracker.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ProgressLogRequest {
    private Double weight;      // Örnek: 85.5
    private LocalDate date;     // Örnek: "2024-12-02" (opsiyonel)
}
