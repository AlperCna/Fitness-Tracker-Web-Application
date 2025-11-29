package com.alper.fitnesstracker.service;

import com.alper.fitnesstracker.entity.Category;
import com.alper.fitnesstracker.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // Tüm kategorileri listele
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Yeni kategori ekle (Aynı isimde varsa hata ver)
    public Category addCategory(Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new RuntimeException("Bu kategori zaten mevcut: " + category.getName());
        }
        return categoryRepository.save(category);
    }

    // Kategori sil
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kategori bulunamadı! ID: " + id));
    }

}