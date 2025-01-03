package com.example.quiz_api.dto;

import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;


@Data
public class QuizQuestionDTO {
    private Long id;
    private Long quizId;
    private Long questionId;
    private String userAnswer;
    private Boolean isCorrect;
}
