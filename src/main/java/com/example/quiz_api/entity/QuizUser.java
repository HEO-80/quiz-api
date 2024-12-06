package com.example.quiz_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "quiz_users")
public class QuizUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quiz_users_id_seq")
    @SequenceGenerator(name = "quiz_users_id_seq", sequenceName = "quiz_users_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private Integer score;
}
