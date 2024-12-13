package com.example.quiz_api.dto;

import lombok.Data;

@Data
public class QuizQuestionDTO {
    private Long id;
    private Long quizId;
    private Long questionId;
    private String userAnswer;
    private Boolean isCorrect;
}
