package com.alper.fitnesstracker.repository;

import com.alper.fitnesstracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Login işlemi için email ile kullanıcıyı bulma
    Optional<User> findByEmail(String email);

    // Kayıt olurken email zaten var mı?
    boolean existsByEmail(String email);


    // Kullanıcı adıyla kullanıcı arama (opsiyonel ama faydalı)
    Optional<User> findByUsername(String username);

    // Kullanıcı adı daha önce alınmış mı?
    boolean existsByUsername(String username);
}
