package com.alper.fitnesstracker.config;

import com.alper.fitnesstracker.security.CustomUserDetailsService;
import com.alper.fitnesstracker.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity // Admin tarafında metod bazlı güvenlik için
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // ========================================================================
    // 1. ZİNCİR: ADMIN PANELİ (Thymeleaf + Session / Cookie)
    // ========================================================================
    @Bean
    @Order(1) // Önce bu kurallar kontrol edilir
    public SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/admin/**") // Sadece /admin ile başlayan istekleri yakalar
                .csrf(AbstractHttpConfigurer::disable) // Admin paneli için şimdilik kapalı
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/login", "/css/**", "/js/**", "/images/**").permitAll() // Login sayfası ve statik dosyalar açık
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN") // Diğer her yer SADECE ADMIN'e
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/admin/login") // Birazdan oluşturacağımız özel login sayfası
                        .loginProcessingUrl("/admin/perform_login") // Formun POST edileceği adres (Spring halleder)
                        .defaultSuccessUrl("/admin/dashboard", true) // Başarılı girişte buraya git
                        .failureUrl("/admin/login?error=true") // Hata olursa buraya git
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/admin/logout")
                        .logoutSuccessUrl("/admin/login?logout=true")
                        .deleteCookies("JSESSIONID") // Çıkışta session çerezini sil
                        .permitAll()
                );

        return http.build();
    }

    // ========================================================================
    // 2. ZİNCİR: API & REACT (JWT + Stateless) - MEVCUT SİSTEMİN
    // ========================================================================
    @Bean
    @Order(2) // Admin değilse buraya düşer (Geri kalan her şey)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/**") // Geriye kalan tüm istekler
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Session YOK, JWT var
                .authorizeHttpRequests(auth -> auth
                        // Public Alanlar (Landing Page, About vb.)
                        .requestMatchers("/", "/index", "/about", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/exercises/**", "/categories/**").permitAll()

                        // Korumalı Alanlar (JWT Şart)
                        .requestMatchers("/workouts/**", "/analytics/**").authenticated()

                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // --- ORTAK AYARLAR (Aynı kalıyor) ---
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}