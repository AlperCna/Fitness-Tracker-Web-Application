package com.alper.fitnesstracker.controller;

import com.alper.fitnesstracker.entity.Category;
import com.alper.fitnesstracker.entity.Exercise;
import com.alper.fitnesstracker.entity.User;
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
    private final UserRepository userRepository; // Kullanıcıları yönetmek için

    // --- PUBLIC PAGES ---
    @GetMapping("/about")
    public String about() { return "about"; }

    // --- ADMIN AUTH ---
    @GetMapping("/admin/login")
    public String adminLoginPage() { return "admin-login"; }

    // --- ADMIN DASHBOARD ---
    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("totalExercises", exerciseService.getAllExercises().size());
        model.addAttribute("totalCategories", categoryService.getAllCategories().size());
        model.addAttribute("totalUsers", userRepository.count());
        return "admin-dashboard";
    }

    // =================================================================
    // 👥 1. KULLANICI YÖNETİMİ (USERS CRUD)
    // =================================================================
    @GetMapping("/admin/users")
    public String listUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "admin-users"; // templates/admin-users.html
    }

    @GetMapping("/admin/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin/users";
    }

    // =================================================================
    // 🏷️ 2. KATEGORİ YÖNETİMİ (CATEGORIES CRUD)
    // =================================================================
    @GetMapping("/admin/categories")
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("newCategory", new Category()); // Form için boş nesne
        return "admin-categories"; // templates/admin-categories.html
    }

    @PostMapping("/admin/categories/save")
    public String saveCategory(@ModelAttribute Category category) {
        categoryService.addCategory(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return "redirect:/admin/categories";
    }

    // =================================================================
    // 🏋️ 3. EGZERSİZ YÖNETİMİ (EXERCISES CRUD)
    // =================================================================
    @GetMapping("/admin/exercises")
    public String listExercises(Model model) {
        model.addAttribute("exercises", exerciseService.getAllExercises());
        return "admin-exercises"; // templates/admin-exercises.html
    }

    @GetMapping("/admin/exercises/new")
    public String showAddExerciseForm(Model model) {
        model.addAttribute("exercise", new Exercise());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin-exercise-form"; // templates/admin-exercise-form.html
    }

    @PostMapping("/admin/exercises/save")
    public String saveExercise(@ModelAttribute Exercise exercise, @RequestParam Long categoryId) {
        exerciseService.addExercise(exercise, categoryId);
        return "redirect:/admin/exercises";
    }

    @GetMapping("/admin/exercises/delete/{id}")
    public String deleteExercise(@PathVariable Long id) {
        exerciseService.deleteExercise(id);
        return "redirect:/admin/exercises";
    }
}