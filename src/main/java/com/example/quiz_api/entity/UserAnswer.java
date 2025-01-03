package com.example.quiz_api.entity;

import jakarta.persistence.*;
import lombok.Data;

// Respuestas Realizadas del usuario en el test
@Entity
@Data
@Table(name = "user_answers")
public class UserAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_answers_id_seq")
    @SequenceGenerator(name = "user_answers_id_seq", sequenceName = "user_answers_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    private QuizUser quiz;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String userAnswer;

    @Column(name = "is_correct")
    private Boolean isCorrect; // Usamos Boolean para manejar el valor 0 y 1 como falso/verdadero
}
