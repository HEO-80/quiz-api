package com.example.quiz_api.repository;

import com.example.quiz_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Método para encontrar usuario por nombre de usuario
    Optional<User> findByUsername(String username);

    // Método para encontrar usuario por correo electrónico
    Optional<User> findByEmail(String email);

    // Método para verificar existencia por nombre de usuario
    boolean existsByUsername(String username);

    // Método para verificar existencia por correo electrónico
    boolean existsByEmail(String email);

}
