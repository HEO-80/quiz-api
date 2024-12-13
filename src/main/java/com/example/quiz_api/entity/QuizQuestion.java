package com.example.quiz_api.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "quiz_questions")
public class QuizQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quiz_questions_id_seq")
    @SequenceGenerator(name = "quiz_questions_id_seq", sequenceName = "quiz_questions_id_seq", allocationSize = 1)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    private QuizUser quiz;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    private String userAnswer;

    private Boolean isCorrect;
}
