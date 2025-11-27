package com.alper.fitnesstracker.controller;

import com.alper.fitnesstracker.dto.AuthResponse;
import com.alper.fitnesstracker.dto.LoginRequest;
import com.alper.fitnesstracker.dto.RegisterRequest;
import com.alper.fitnesstracker.entity.Role;
import com.alper.fitnesstracker.entity.User;
import com.alper.fitnesstracker.security.JwtUtils; // Paket ismi düzeltildi
import com.alper.fitnesstracker.repository.RoleRepository;
import com.alper.fitnesstracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    // -----------------------------------------
    //  REGISTER (Kayıt Ol)
    // -----------------------------------------
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {

        // 1. Email kontrolü
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email zaten kullanımda!");
        }

        // 2. Rol atama (Veritabanında yoksa oluştur)
        Role defaultRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_USER").build()));

        // 3. Kullanıcıyı oluştur
        User newUser = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Collections.singleton(defaultRole))
                .build();

        userRepository.save(newUser);

        // 4. Token üret (Kayıt olur olmaz giriş yapmış sayıyoruz)
        String token = jwtUtils.generateToken(
                org.springframework.security.core.userdetails.User.builder()
                        .username(newUser.getEmail())
                        .password(newUser.getPassword())
                        .authorities(defaultRole.getName())
                        .build()
        );

        // 5. Cevabı dön
        return new ResponseEntity<>(new AuthResponse(token, newUser.getEmail(), defaultRole.getName()), HttpStatus.CREATED);
    }

    // -----------------------------------------
    //  LOGIN (Giriş Yap)
    // -----------------------------------------
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {

        // 1. Spring Security ile şifre kontrolü
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // 2. Kullanıcıyı DB'den çek
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        String role = user.getRoles().isEmpty() ? "ROLE_USER" : user.getRoles().iterator().next().getName();

        // 3. Token üret
        String token = jwtUtils.generateToken(
                org.springframework.security.core.userdetails.User.builder()
                        .username(user.getEmail())
                        .password(user.getPassword())
                        .authorities(role)
                        .build()
        );

        // 4. Cevabı dön
        return ResponseEntity.ok(new AuthResponse(token, user.getEmail(), role));
    }
}