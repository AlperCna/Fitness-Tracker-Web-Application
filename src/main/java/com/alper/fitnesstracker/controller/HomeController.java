package com.alper.fitnesstracker.controller;

import com.alper.fitnesstracker.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ExerciseRepository exerciseRepository;

    @GetMapping("/")
    public String home(Model model) {
        // 1. Veritabanından gerçek egzersiz sayısını al
        long count = exerciseRepository.count();

        // 2. Bu sayıyı HTML'e gönder
        model.addAttribute("exerciseCount", count + "+");

        return "index"; // templates/index.html
    }
}