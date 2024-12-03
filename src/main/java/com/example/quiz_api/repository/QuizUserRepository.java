package com.example.quiz_api.repository;

import com.example.quiz_api.entity.QuizUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizUserRepository extends JpaRepository<QuizUser, Long> {
}