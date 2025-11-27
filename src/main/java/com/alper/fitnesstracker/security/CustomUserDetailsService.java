package com.alper.fitnesstracker.security;

import com.alper.fitnesstracker.entity.Role;
import com.alper.fitnesstracker.entity.User;
import com.alper.fitnesstracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // Login sırasında Spring Security burayı çağırır
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Email ile kullanıcıyı buluyoruz
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı: " + username));

        // Roller (ROLE_USER, ROLE_ADMIN) -> Spring Security formatına çevriliyor
        Set<SimpleGrantedAuthority> authorities =
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toSet());

        // Spring'in kendi UserDetails objesini döndürüyoruz
        return new UserDetails() {

            @Override
            public Set<? extends SimpleGrantedAuthority> getAuthorities() {
                return authorities;
            }

            @Override
            public String getPassword() {
                return user.getPassword();
            }

            @Override
            public String getUsername() {
                return user.getEmail(); // email ile login
            }

            @Override
            public boolean isAccountNonExpired() { return true; }

            @Override
            public boolean isAccountNonLocked() { return true; }

            @Override
            public boolean isCredentialsNonExpired() { return true; }

            @Override
            public boolean isEnabled() { return true; }
        };
    }
}
