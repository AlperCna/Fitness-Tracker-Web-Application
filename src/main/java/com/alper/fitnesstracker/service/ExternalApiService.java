package com.alper.fitnesstracker.service;

import com.alper.fitnesstracker.dto.ExternalExerciseDto;
import com.alper.fitnesstracker.entity.Category;
import com.alper.fitnesstracker.entity.Exercise;
import com.alper.fitnesstracker.repository.CategoryRepository;
import com.alper.fitnesstracker.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExternalApiService {

    private final ExerciseRepository exerciseRepository;
    private final CategoryRepository categoryRepository;

    private static final String API_KEY = "b9cdc94ab0mshd64a95ed6a7e02ep170351jsn0144ca40b497";
    private static final String API_HOST = "exercisedb.p.rapidapi.com";

    private final RestTemplate restTemplate = new RestTemplate();

    // --- AKILLI ÇEKİM METODU ---
    // --- AKILLI ÇEKİM METODU (DÜZELTİLDİ) ---
    public List<ExternalExerciseDto> fetchAllExercises() {
        List<ExternalExerciseDto> allExercises = new ArrayList<>();
        int limitRequest = 50; // API'den 50 isteyelim (O 10 verse bile sorun değil)
        int offset = 0;
        boolean keepFetching = true;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-RapidAPI-Key", API_KEY);
        headers.set("X-RapidAPI-Host", API_HOST);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        while (keepFetching) {
            String url = "https://exercisedb.p.rapidapi.com/exercises?limit=" + limitRequest + "&offset=" + offset;

            try {
                ResponseEntity<ExternalExerciseDto[]> response =
                        restTemplate.exchange(url, HttpMethod.GET, entity, ExternalExerciseDto[].class);

                if (response.getBody() != null && response.getBody().length > 0) {
                    List<ExternalExerciseDto> batch = Arrays.asList(response.getBody());
                    allExercises.addAll(batch);

                    // --- KRİTİK DÜZELTME BURADA ---
                    // "limit" kadar değil, "gelen veri sayısı" kadar ilerle!
                    offset += batch.size();
                    // -----------------------------

                    System.out.println("Çekilen: " + batch.size() + " | Toplam: " + allExercises.size() + " | Sıradaki Offset: " + offset);

                    if (allExercises.size() >= 1300) keepFetching = false; // Hedefe ulaştık
                } else {
                    keepFetching = false; // Veri bitti
                }
            } catch (Exception e) {
                System.out.println("Hata: " + e.getMessage());
                keepFetching = false;
            }
        }
        return allExercises;
    }

    @Transactional
    public void syncExercises() {
        // Artık akıllı metodu çağırıyoruz
        List<ExternalExerciseDto> apiExercises = fetchAllExercises();
        System.out.println("VERİTABANINA YAZILIYOR... Toplam: " + apiExercises.size());

        for (ExternalExerciseDto api : apiExercises) {
            // ... (Kayıt kodları aynen kalacak) ...
            if (exerciseRepository.findByApiReferenceId(api.getId()).isPresent()) {
                continue;
            }

            String categoryName = capitalize(api.getBodyPart());
            Category category = categoryRepository.findByName(categoryName)
                    .orElseGet(() -> {
                        Category newCat = new Category();
                        newCat.setName(categoryName);
                        return categoryRepository.save(newCat);
                    });

            String descriptionText = (api.getInstructions() != null)
                    ? String.join("\n", api.getInstructions())
                    : "No description available.";

            Exercise exercise = Exercise.builder()
                    .name(capitalize(api.getName()))
                    .description(descriptionText)
                    .gifUrl(api.getGifUrl())
                    .equipment(capitalize(api.getEquipment()))
                    .target(capitalize(api.getTarget()))
                    .bodyPart(capitalize(api.getBodyPart()))
                    .apiReferenceId(api.getId())
                    .category(category)
                    .build();

            exerciseRepository.save(exercise);
        }
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}