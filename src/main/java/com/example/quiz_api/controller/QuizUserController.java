package com.example.quiz_api.controller;

import com.example.quiz_api.dto.QuizUserDTO;
import com.example.quiz_api.service.QuizUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz-users")
public class QuizUserController {
    @Autowired
    private QuizUserService quizUserService;

    @GetMapping
    public ResponseEntity<List<QuizUserDTO>> getAllQuizzes() {
        List<QuizUserDTO> quizzes = quizUserService.getAllQuizzes();
        return new ResponseEntity<>(quizzes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizUserDTO> getQuizById(@PathVariable Long id) {
        QuizUserDTO quizUser = quizUserService.getQuizById(id);
        return new ResponseEntity<>(quizUser, HttpStatus.OK);
    }

//    @PostMapping
//    public ResponseEntity<QuizUserDTO> createQuiz(@RequestBody QuizUserDTO quizUserDTO) {
//        QuizUserDTO createdQuizUser = quizUserService.createQuiz(quizUserDTO);
//        return new ResponseEntity<>(createdQuizUser, HttpStatus.CREATED);
//    }

    @PostMapping
    public ResponseEntity<QuizUserDTO> createQuiz(@RequestBody QuizUserDTO quizUserDTO) {
        QuizUserDTO createdQuiz = quizUserService.createQuiz(quizUserDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdQuiz);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuizUserDTO> updateQuiz(@PathVariable Long id, @RequestBody QuizUserDTO quizUserDTO) {
        QuizUserDTO updatedQuizUser = quizUserService.updateQuiz(id, quizUserDTO);
        return new ResponseEntity<>(updatedQuizUser, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Long id) {
        quizUserService.deleteQuiz(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
