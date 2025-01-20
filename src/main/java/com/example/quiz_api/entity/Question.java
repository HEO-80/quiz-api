package com.example.quiz_api.entity;

import jakarta.persistence.*;
import lombok.Data;

    @Entity
    @Data
    @Table(name = "questions")
    public class Question {
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "questions_id_seq")
        @SequenceGenerator(name = "questions_id_seq", sequenceName = "questions_id_seq", allocationSize = 1)
        private Long id;

        @Column(nullable = false)
        private String questionText;

        @Column(nullable = false)
        private String category;

        @Column(nullable = false)
        private String correctAnswer;

        @Column(nullable = false)
        private String option1;

        @Column(nullable = false)
        private String option2;

        @Column(nullable = false)
        private String option3;

        @Column(nullable = false)
        private String option4;

    }
