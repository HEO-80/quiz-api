package com.example.quiz_api.repository;

import com.example.quiz_api.entity.QuizQuestion;
import com.example.quiz_api.entity.QuizUser;
import com.example.quiz_api.entity.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {

}
