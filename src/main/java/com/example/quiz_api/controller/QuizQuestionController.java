package com.example.quiz_api.controller;

import com.example.quiz_api.dto.QuizQuestionDTO;
import com.example.quiz_api.service.QuizQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz-questions")
public class QuizQuestionController {
    @Autowired
    private QuizQuestionService quizQuestionService;

    @GetMapping
    public ResponseEntity<List<QuizQuestionDTO>> getAllQuizQuestions() {
        List<QuizQuestionDTO> quizQuestions = quizQuestionService.getAllQuizQuestions();
        return new ResponseEntity<>(quizQuestions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizQuestionDTO> getQuizQuestionById(@PathVariable Long id) {
        QuizQuestionDTO quizQuestion = quizQuestionService.getQuizQuestionById(id);
        return new ResponseEntity<>(quizQuestion, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<QuizQuestionDTO> createQuizQuestion(@RequestBody QuizQuestionDTO quizQuestionDTO) {
        QuizQuestionDTO createdQuizQuestion = quizQuestionService.createQuizQuestion(quizQuestionDTO);
        return new ResponseEntity<>(createdQuizQuestion, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuizQuestionDTO> updateQuizQuestion(@PathVariable Long id, @RequestBody QuizQuestionDTO quizQuestionDTO) {
        QuizQuestionDTO updatedQuizQuestion = quizQuestionService.updateQuizQuestion(id, quizQuestionDTO);
        return new ResponseEntity<>(updatedQuizQuestion, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuizQuestion(@PathVariable Long id) {
        quizQuestionService.deleteQuizQuestion(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
