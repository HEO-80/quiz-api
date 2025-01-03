package com.example.quiz_api.controller;

import com.example.quiz_api.dto.UserAnswerDTO;
import com.example.quiz_api.service.UserAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para manejar operaciones relacionadas con UserAnswer.
 */
@RestController
@RequestMapping("/api/user-answers")
public class UserAnswerController {

    @Autowired
    private UserAnswerService userAnswerService;

    /**
     * Obtiene todas las respuestas de los usuarios.
     */
    @GetMapping
    public ResponseEntity<List<UserAnswerDTO>> getAllUserAnswers() {
        List<UserAnswerDTO> userAnswers = userAnswerService.getAllUserAnswers();
        return new ResponseEntity<>(userAnswers, HttpStatus.OK);
    }

    /**
     * Obtiene una respuesta específica de un usuario por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserAnswerDTO> getUserAnswerById(@PathVariable Long id) {
        UserAnswerDTO userAnswer = userAnswerService.getUserAnswerById(id);
        return new ResponseEntity<>(userAnswer, HttpStatus.OK);
    }

    /**
     * Crea una nueva respuesta de usuario.
     */
    @PostMapping
    public ResponseEntity<UserAnswerDTO> createUserAnswer(@RequestBody UserAnswerDTO userAnswerDTO) {
        UserAnswerDTO createdUserAnswer = userAnswerService.createUserAnswer(userAnswerDTO);
        return new ResponseEntity<>(createdUserAnswer, HttpStatus.CREATED);
    }

    /**
     * Actualiza una respuesta existente de un usuario.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserAnswerDTO> updateUserAnswer(@PathVariable Long id, @RequestBody UserAnswerDTO userAnswerDTO) {
        UserAnswerDTO updatedUserAnswer = userAnswerService.updateUserAnswer(id, userAnswerDTO);
        return new ResponseEntity<>(updatedUserAnswer, HttpStatus.OK);
    }

    /**
     * Elimina una respuesta de usuario por ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserAnswer(@PathVariable Long id) {
        userAnswerService.deleteUserAnswer(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
