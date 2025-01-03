package com.example.quiz_api.controller;

import com.example.quiz_api.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/api/login")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        logger.info("Intento de inicio de sesión para usuario: {}", username);

        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            logger.warn("Parámetros inválidos: username o password vacío.");
            return ResponseEntity.badRequest().body("Username y password son obligatorios.");
        }

        boolean isAuthenticated = userService.validateUser(username, password);
        if (isAuthenticated) {
            logger.info("Usuario autenticado: {}", username);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Inicio de sesión exitoso");
            response.put("username", username);
            response.put("userId", userService.getUserIdByUsername(username)); // Devuelve el ID del usuario


            return ResponseEntity.ok(response);
        } else {
            logger.warn("Fallo en la autenticación para usuario: {}", username);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }
}
