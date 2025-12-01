package com.alper.fitnesstracker.controller;

import com.alper.fitnesstracker.dto.ProgressLogRequest;
import com.alper.fitnesstracker.entity.ProgressLog;
import com.alper.fitnesstracker.service.ProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/progress")
@RequiredArgsConstructor
public class ProgressController {

    private final ProgressService progressService;

    // 1. Kilo Ekle (POST /progress)
    @PostMapping
    public ResponseEntity<ProgressLog> addLog(@RequestBody ProgressLogRequest request, Principal principal) {
        return ResponseEntity.ok(progressService.addLog(principal.getName(), request));
    }

    // 2. Geçmişimi Gör (GET /progress)
    @GetMapping
    public ResponseEntity<List<ProgressLog>> getMyProgress(Principal principal) {
        return ResponseEntity.ok(progressService.getMyProgress(principal.getName()));
    }

    // 3. Kayıt Sil (DELETE /progress/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLog(@PathVariable Long id) {
        progressService.deleteLog(id);
        return ResponseEntity.noContent().build();
    }
}