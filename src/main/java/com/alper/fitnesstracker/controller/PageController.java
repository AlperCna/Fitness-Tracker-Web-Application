package com.alper.fitnesstracker.controller;

import com.alper.fitnesstracker.entity.Category;
import com.alper.fitnesstracker.entity.Exercise;
import com.alper.fitnesstracker.entity.User;
import com.alper.fitnesstracker.repository.CategoryRepository; // EKLENDİ
import com.alper.fitnesstracker.repository.ExerciseRepository; // EKLENDİ
import com.alper.fitnesstracker.repository.UserRepository;
import com.alper.fitnesstracker.service.CategoryService;
import com.alper.fitnesstracker.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final ExerciseService exerciseService;
    private final CategoryService categoryService;
    private final UserRepository userRepository;

    // 🔥 YENİ EKLENENLER: Admin doğrudan veritabanı ile konuşsun
    private final ExerciseRepository exerciseRepository;
    private final CategoryRepository categoryRepository;

    // --- PUBLIC PAGES ---
    @GetMapping("/about")
    public String about() { return "about"; }

    // --- ADMIN AUTH ---
    @GetMapping("/admin/login")
    public String adminLoginPage() { return "admin-login"; }

    // --- ADMIN DASHBOARD ---
    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("totalExercises", exerciseRepository.count()); // Servis yerine Repository (Daha hızlı)
        model.addAttribute("totalCategories", categoryRepository.count());
        model.addAttribute("totalUsers", userRepository.count());
        return "admin-dashboard";
    }

    // =================================================================
    // 👥 1. KULLANICI YÖNETİMİ
    // =================================================================
    @GetMapping("/admin/users")
    public String listUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "admin-users";
    }

    @GetMapping("/admin/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id); // ✅ BU ZATEN ÇALIŞIYORDU
        return "redirect:/admin/users";
    }

    // =================================================================
    // 🏷️ 2. KATEGORİ YÖNETİMİ
    // =================================================================
    @GetMapping("/admin/categories")
    public String listCategories(Model model) {
        // Listelemede service kullanabilirsin (sorun yoksa), ama repository daha garanti
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("newCategory", new Category());
        return "admin-categories";
    }

    @PostMapping("/admin/categories/save")
    public String saveCategory(@ModelAttribute Category category) {
        categoryRepository.save(category); // 🔥 Service yerine Repository
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        // 🔥 KRİTİK DÜZELTME: Service yerine Repository kullandık
        categoryRepository.deleteById(id);
        return "redirect:/admin/categories";
    }

    // =================================================================
    // 🏋️ 3. EGZERSİZ YÖNETİMİ
    // =================================================================
    @GetMapping("/admin/exercises")
    public String listExercises(Model model) {
        model.addAttribute("exercises", exerciseRepository.findAll()); // Repository
        return "admin-exercises";
    }

    @GetMapping("/admin/exercises/new")
    public String showAddExerciseForm(Model model) {
        model.addAttribute("exercise", new Exercise());
        model.addAttribute("categories", categoryRepository.findAll()); // Repository
        return "admin-exercise-form";
    }

    @PostMapping("/admin/exercises/save")
    public String saveExercise(@ModelAttribute Exercise exercise, @RequestParam Long categoryId) {
        // Egzersiz kaydederken kategori set etmemiz lazım
        Category cat = categoryRepository.findById(categoryId).orElse(null);
        exercise.setCategory(cat);
        exerciseRepository.save(exercise); // 🔥 Repository ile kayıt
        return "redirect:/admin/exercises";
    }

    @GetMapping("/admin/exercises/delete/{id}")
    public String deleteExercise(@PathVariable Long id) {
        // 🔥 KRİTİK DÜZELTME: Service yerine Repository kullandık
        // Service katmanındaki güvenlik kontrolünü atlamış olduk.
        exerciseRepository.deleteById(id);
        return "redirect:/admin/exercises";
    }
}