package com.example.quiz_api.controller;

import com.example.quiz_api.dto.UserDTO;
import com.example.quiz_api.dto.UserResponseDTO;
import com.example.quiz_api.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;


    /**
     * Obtiene todos los usuarios.
     *
     * @return Lista de usuarios en formato JSON.
     */
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        logger.info("Solicitud para obtener todos los usuarios.");
        List<UserResponseDTO> users = userService.getAllUsers();
        logger.info("Usuarios recuperados: {}", users.size());
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * Obtiene un usuario por su ID.
     *
     * @param id ID del usuario.
     * @return Detalles del usuario en formato JSON.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        logger.info("Solicitud para obtener usuario con ID: {}", id);
        UserResponseDTO user = userService.getUserById(id);
        if (user != null) {
            logger.info("Usuario encontrado: {}", user.getUsername());
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            logger.warn("Usuario con ID: {} no encontrado.", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Crea un nuevo usuario.
     *
     * @param userDTO Datos del usuario a crear.
     * @return Usuario creado en formato JSON.
     */
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserDTO userDTO) {
        logger.info("Solicitud para crear un nuevo usuario: {}", userDTO.getUsername());
        UserResponseDTO createdUser = userService.createUser(userDTO);
        logger.info("Usuario creado con éxito: {}", createdUser.getUsername());
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    /**
     * Actualiza un usuario existente.
     *
     * @param id      ID del usuario a actualizar.
     * @param userDTO Datos actualizados del usuario.
     * @return Usuario actualizado en formato JSON.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        logger.info("Solicitud para actualizar usuario con ID: {}", id);
        UserResponseDTO updatedUser = userService.updateUser(id, userDTO);
        logger.info("Usuario actualizado con éxito: {}", updatedUser.getUsername());
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Elimina un usuario por su ID.
     *
     * @param id ID del usuario a eliminar.
     * @return Respuesta HTTP con estado NO_CONTENT.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        logger.info("Solicitud para eliminar usuario con ID: {}", id);
        userService.deleteUser(id);
        logger.info("Usuario con ID: {} eliminado con éxito.", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
