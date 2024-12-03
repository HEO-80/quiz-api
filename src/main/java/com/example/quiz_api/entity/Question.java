package com.example.quiz_api.entity;

import jakarta.persistence.*;
import lombok.Data;

    @Entity
    @Data
    @Table(name = "questions")
    public class Question {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "question_text", nullable = false)
        private String questionText;

        private String category;

        @Column(name = "correct_answer")
        private String correctAnswer;

        private String option1;
        private String option2;
        private String option3;
        private String option4;

}
