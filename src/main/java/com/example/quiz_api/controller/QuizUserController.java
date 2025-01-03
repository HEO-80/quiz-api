package com.example.quiz_api.controller;

import com.example.quiz_api.dto.QuizUserDTO;
import com.example.quiz_api.service.QuizUserService;
import com.example.quiz_api.exception.BadRequestException; // Import necesario
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para manejar operaciones relacionadas con QuizUser.
 */
@RestController
@RequestMapping("/api/quiz-users")
public class QuizUserController {

    @Autowired
    private QuizUserService quizUserService;

    /**
     * Obtiene todos los registros de QuizUser.
     */
    @GetMapping
    public ResponseEntity<List<QuizUserDTO>> getAllQuizzes() {
        List<QuizUserDTO> quizzes = quizUserService.getAllQuizzes();
        return new ResponseEntity<>(quizzes, HttpStatus.OK);
    }

    /**
     * Obtiene un registro de QuizUser por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<QuizUserDTO> getQuizById(@PathVariable Long id) {
        QuizUserDTO quizUser = quizUserService.getQuizById(id);
        return new ResponseEntity<>(quizUser, HttpStatus.OK);
    }

    /**
     * Crea un nuevo registro de QuizUser.
     */
    @PostMapping
    public ResponseEntity<QuizUserDTO> createQuiz(@RequestBody QuizUserDTO quizUserDTO) {
        if (quizUserDTO.getUserId() == null || quizUserDTO.getUserId() <= 0) {
            throw new BadRequestException("El ID de usuario es inválido.");
        }
        QuizUserDTO createdQuiz = quizUserService.createQuiz(quizUserDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdQuiz);
    }

    /**
     * Actualiza un registro de QuizUser existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<QuizUserDTO> updateQuiz(@PathVariable Long id, @RequestBody QuizUserDTO quizUserDTO) {
        QuizUserDTO updatedQuizUser = quizUserService.updateQuiz(id, quizUserDTO);
        return new ResponseEntity<>(updatedQuizUser, HttpStatus.OK);
    }

    /**
     * Elimina un registro de QuizUser por ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Long id) {
        quizUserService.deleteQuiz(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
