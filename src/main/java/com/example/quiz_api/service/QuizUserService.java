package com.example.quiz_api.service;

import com.example.quiz_api.entity.QuizUser;
import com.example.quiz_api.repository.QuizUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.quiz_api.exception.ResourceNotFoundException;

import java.util.List;

@Service
public class QuizUserService {
    @Autowired
    private QuizUserRepository quizUserRepository;

    public List<QuizUser> getAllQuizzes() {
        return quizUserRepository.findAll();
    }

    public QuizUser getQuizById(Long id) {
        return quizUserRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Quiz not found with id " + id));
    }

    public QuizUser createQuiz(QuizUser quizUser) {
        return quizUserRepository.save(quizUser);
    }

    public QuizUser updateQuiz(Long id, QuizUser quizUserDetails) {
        QuizUser quizUser = getQuizById(id);
        quizUser.setUserId(quizUserDetails.getUserId());
        quizUser.setCategory(quizUserDetails.getCategory());
        quizUser.setStartTime(quizUserDetails.getStartTime());
        quizUser.setEndTime(quizUserDetails.getEndTime());
        quizUser.setScore(quizUserDetails.getScore());
        return quizUserRepository.save(quizUser);
    }

    public void deleteQuiz(Long id) {
        QuizUser quizUser = getQuizById(id);
        quizUserRepository.delete(quizUser);
    }
}
