package com.example.quiz_api.dto;

import lombok.Data;


import java.time.LocalDateTime;

@Data
public class QuizUserDTO {
    private Long id;
    private Long userId; // Solo almacenamos el ID del usuario aquí
    private String category;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer score;

}
