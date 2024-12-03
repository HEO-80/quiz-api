package com.example.quiz_api.service;

import com.example.quiz_api.entity.Question;
import com.example.quiz_api.repository.QuestionRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.quiz_api.exception.ResourceNotFoundException;

import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public Question getQuestionById(Long id) {
        return questionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Question not found with id " + id));
    }

    public Question createQuestion(Question question) {
        return questionRepository.save(question);
    }

    public Question updateQuestion(Long id, Question questionDetails) {
        Question question = getQuestionById(id);
        question.setQuestionText(questionDetails.getQuestionText());
        question.setCategory(questionDetails.getCategory());
        question.setCorrectAnswer(questionDetails.getCorrectAnswer());
        question.setOption1(questionDetails.getOption1());
        question.setOption2(questionDetails.getOption2());
        question.setOption3(questionDetails.getOption3());
        question.setOption4(questionDetails.getOption4());
        return questionRepository.save(question);
    }

    public void deleteQuestion(Long id) {
        Question question = getQuestionById(id);
        questionRepository.delete(question);
    }
}