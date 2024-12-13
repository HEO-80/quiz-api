package com.example.quiz_api.dto;

import lombok.Data;

@Data
public class UserAnswerDTO {
    private Long id;
    private Long quizId;
    private Long questionId;
    private Long userId;
    private String userAnswer;
    private Boolean isCorrect;  // Usamos Boolean en lugar de Integer para mayor claridad
}
