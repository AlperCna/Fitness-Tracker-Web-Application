package com.alper.fitnesstracker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List; // <--- EKLENDİ

@Data
public class ExternalExerciseDto {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("bodyPart")
    private String bodyPart;

    @JsonProperty("equipment")
    private String equipment;

    @JsonProperty("gifUrl")
    private String gifUrl;

    @JsonProperty("target")
    private String target;

    // --- DÜZELTİLEN KISIM ---
    // API'den ["Adım 1", "Adım 2"] şeklinde liste gelir.
    // String yaparsan hata verir!
    @JsonProperty("instructions")
    private List<String> instructions;
}