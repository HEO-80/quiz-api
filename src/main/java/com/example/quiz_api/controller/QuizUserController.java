package com.example.quiz_api.controller;

import com.example.quiz_api.entity.QuizUser;
import com.example.quiz_api.service.QuizUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
public class QuizUserController {
    @Autowired
    private QuizUserService quizUserService;

    @GetMapping
    public ResponseEntity<List<QuizUser>> getAllQuizzes() {
        List<QuizUser> quizzes = quizUserService.getAllQuizzes();
        return new ResponseEntity<>(quizzes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizUser> getQuizById(@PathVariable Long id) {
        QuizUser quiz = quizUserService.getQuizById(id);
        return new ResponseEntity<>(quiz, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<QuizUser> createQuiz(@RequestBody QuizUser quizUser) {
        QuizUser createdQuiz = quizUserService.createQuiz(quizUser);
        return new ResponseEntity<>(createdQuiz, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuizUser> updateQuiz(@PathVariable Long id, @RequestBody QuizUser quizUser) {
        QuizUser updatedQuiz = quizUserService.updateQuiz(id, quizUser);
        return new ResponseEntity<>(updatedQuiz, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Long id) {
        quizUserService.deleteQuiz(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
