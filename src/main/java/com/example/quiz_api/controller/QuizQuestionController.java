package com.example.quiz_api.controller;

import com.example.quiz_api.dto.QuizQuestionDTO;
import com.example.quiz_api.service.QuizQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

/**
 * Controlador para manejar operaciones relacionadas con QuizQuestion.
 */
@RestController
@RequestMapping("/api/quiz-questions")
public class QuizQuestionController {

    private static final Logger logger = Logger.getLogger(QuizQuestionController.class.getName());

    @Autowired
    private QuizQuestionService quizQuestionService;

    /**
     * Obtiene todas las preguntas de los cuestionarios.
     */
    @GetMapping
    public ResponseEntity<List<QuizQuestionDTO>> getAllQuizQuestions() {
        logger.info("Solicitud recibida: GET /api/quiz-questions");
        List<QuizQuestionDTO> quizQuestions = quizQuestionService.getAllQuizQuestions();
        logger.info("Datos enviados: " + quizQuestions);
        return new ResponseEntity<>(quizQuestions, HttpStatus.OK);
    }

    /**
     * Obtiene una pregunta específica por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<QuizQuestionDTO> getQuizQuestionById(@PathVariable Long id) {
        logger.info("Solicitud recibida: GET /api/quiz-questions/" + id);
        QuizQuestionDTO quizQuestion = quizQuestionService.getQuizQuestionById(id);
        logger.info("Datos enviados: " + quizQuestion);
        return new ResponseEntity<>(quizQuestion, HttpStatus.OK);
    }

    /**
     * Crea una nueva pregunta en el cuestionario.
     */
    @PostMapping
    public ResponseEntity<QuizQuestionDTO> createQuizQuestion(@RequestBody QuizQuestionDTO quizQuestionDTO) {
        logger.info("Solicitud recibida: POST /api/quiz-questions");
        logger.info("Datos recibidos: " + quizQuestionDTO);
        QuizQuestionDTO createdQuizQuestion = quizQuestionService.createQuizQuestion(quizQuestionDTO);
        logger.info("Datos guardados: " + createdQuizQuestion);
        return new ResponseEntity<>(createdQuizQuestion, HttpStatus.CREATED);
    }


    /**
     * Actualiza una pregunta existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<QuizQuestionDTO> updateQuizQuestion(@PathVariable Long id, @RequestBody QuizQuestionDTO quizQuestionDTO) {
        logger.info("Solicitud recibida: PUT /api/quiz-questions/" + id);
        logger.info("Datos recibidos: " + quizQuestionDTO);
        QuizQuestionDTO updatedQuizQuestion = quizQuestionService.updateQuizQuestion(id, quizQuestionDTO);
        logger.info("Datos actualizados: " + updatedQuizQuestion);
        return new ResponseEntity<>(updatedQuizQuestion, HttpStatus.OK);
    }

    /**
     * Elimina una pregunta por ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuizQuestion(@PathVariable Long id) {
        logger.info("Solicitud recibida: DELETE /api/quiz-questions/" + id);
        quizQuestionService.deleteQuizQuestion(id);
        logger.info("Registro con id " + id + " eliminado");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
