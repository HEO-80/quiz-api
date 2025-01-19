package com.example.quiz_api.controller;

import com.example.quiz_api.dto.LoginResponseDTO;
import com.example.quiz_api.dto.UserDTO;
import com.example.quiz_api.dto.UserResponseDTO;
import com.example.quiz_api.service.UserService;
import com.example.quiz_api.util.JwtUtil;
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
    private JwtUtil jwtUtil; // Inyectar JwtUtil para generar tokens

    @Autowired
    private UserService userService;

    @PostMapping
        public ResponseEntity<LoginResponseDTO> loginUser(@RequestBody UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();

        logger.info("Intento de inicio de sesión para usuario: {}", username);

            // (1) Verificar credenciales
            boolean isValid = userService.validateUser(username, password);
            if (!isValid) {
                // Error: credenciales inválidas -> 401
                // En lugar de devolver un String, devolvemos un LoginResponseDTO con mensaje de error.
                logger.warn("Fallo en la autenticación para usuario: {}", username);

                // Construimos un DTO de respuesta con mensaje de error
                LoginResponseDTO errorDto = new LoginResponseDTO(
                        "Credenciales inválidas",  // message
                        null,                      // username
                        null,                      // userId
                        null                       // accessToken
                );

                // Error: credenciales inválidas -> 401
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(errorDto);
            }

            // Obtener los detalles del usuario para la respuesta
//            UserResponseDTO userResponseDTO = userService.getUserById(userId);

            // (2) Obtener userId (opcional, si deseas devolverlo)
            Long userId = userService.getUserIdByUsername(username);

            // Generar token JWT
            String token = jwtUtil.generateToken(username);

            // Construir la respuesta con UN SOLO DTO
            LoginResponseDTO loginResponse = new LoginResponseDTO();
            loginResponse.setMessage("Inicio de sesión exitoso");
            loginResponse.setUsername(username);
            loginResponse.setUserId(userId); // Devuelve el ID del usuario
            loginResponse.setAccessToken(token);

            // Retornar
            return ResponseEntity.ok(loginResponse);
    }
}
