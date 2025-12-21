package com.alper.fitnesstracker.controller;

import com.alper.fitnesstracker.entity.Category;
import com.alper.fitnesstracker.entity.Exercise;
import com.alper.fitnesstracker.entity.User;
import com.alper.fitnesstracker.repository.CategoryRepository;
import com.alper.fitnesstracker.repository.ExerciseRepository;
import com.alper.fitnesstracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final UserRepository userRepository;
    private final ExerciseRepository exerciseRepository;
    private final CategoryRepository categoryRepository;

    // =================================================================
    // PUBLIC PAGES
    // =================================================================
    @GetMapping("/about")
    public String about() {
        return "about";
    }

    // =================================================================
    // ADMIN AUTHENTICATION
    // =================================================================
    @GetMapping("/admin/login")
    public String adminLoginPage() {
        return "admin-login";
    }

    // =================================================================
    // ADMIN DASHBOARD
    // =================================================================
    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        // Fetching counts directly from repositories for better performance
        model.addAttribute("totalExercises", exerciseRepository.count());
        model.addAttribute("totalCategories", categoryRepository.count());
        model.addAttribute("totalUsers", userRepository.count());
        return "admin-dashboard";
    }

    // =================================================================
    // 1. USER MANAGEMENT (CRUD)
    // =================================================================
    @GetMapping("/admin/users")
    public String listUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "admin-users";
    }

    /**
     * Deletes a user by ID.
     * @Transactional is used to ensure all related data (workouts, logs)
     * are deleted atomically before the user is removed.
     */
    @GetMapping("/admin/users/delete/{id}")
    @Transactional
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin/users";
    }

    // =================================================================
    // 2. CATEGORY MANAGEMENT (CRUD)
    // =================================================================
    @GetMapping("/admin/categories")
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("newCategory", new Category());
        return "admin-categories";
    }

    @PostMapping("/admin/categories/save")
    public String saveCategory(@ModelAttribute Category category) {
        categoryRepository.save(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories/delete/{id}")
    @Transactional
    public String deleteCategory(@PathVariable Long id) {
        // Direct repository call to bypass service-level security checks for Admin
        categoryRepository.deleteById(id);
        return "redirect:/admin/categories";
    }

    // =================================================================
    // 3. EXERCISE MANAGEMENT (CRUD)
    // =================================================================
    @GetMapping("/admin/exercises")
    public String listExercises(Model model) {
        model.addAttribute("exercises", exerciseRepository.findAll());
        return "admin-exercises";
    }

    @GetMapping("/admin/exercises/new")
    public String showAddExerciseForm(Model model) {
        model.addAttribute("exercise", new Exercise());
        model.addAttribute("categories", categoryRepository.findAll());
        return "admin-exercise-form";
    }

    @PostMapping("/admin/exercises/save")
    public String saveExercise(@ModelAttribute Exercise exercise, @RequestParam Long categoryId) {
        // Link the exercise to the selected category before saving
        Category cat = categoryRepository.findById(categoryId).orElse(null);
        exercise.setCategory(cat);
        exerciseRepository.save(exercise);
        return "redirect:/admin/exercises";
    }

    @GetMapping("/admin/exercises/delete/{id}")
    @Transactional
    public String deleteExercise(@PathVariable Long id) {
        // Direct repository call to ensure Admin can delete any exercise
        // regardless of ownership or service-level restrictions.
        exerciseRepository.deleteById(id);
        return "redirect:/admin/exercises";
    }
}