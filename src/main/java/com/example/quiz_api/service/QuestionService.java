package com.example.quiz_api.service;

import com.example.quiz_api.dto.QuestionDTO;
import com.example.quiz_api.entity.Question;
import com.example.quiz_api.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.quiz_api.exception.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public List<QuestionDTO> getAllQuestions() {
        return questionRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public QuestionDTO getQuestionById(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id " + id));
        return convertToDTO(question);
    }

    public QuestionDTO createQuestion(QuestionDTO questionDTO) {
        Question question = convertToEntity(questionDTO);
        Question savedQuestion = questionRepository.save(question);
        return convertToDTO(savedQuestion);
    }

    public QuestionDTO updateQuestion(Long id, QuestionDTO questionDTO) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id " + id));

        question.setQuestionText(questionDTO.getQuestionText());
        question.setCategory(questionDTO.getCategory());
        question.setCorrectAnswer(questionDTO.getCorrectAnswer());
        question.setOption1(questionDTO.getOption1());
        question.setOption2(questionDTO.getOption2());
        question.setOption3(questionDTO.getOption3());
        question.setOption4(questionDTO.getOption4());

        Question updatedQuestion = questionRepository.save(question);
        return convertToDTO(updatedQuestion);
    }

    public void deleteQuestion(Long id) {
        if (!questionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Question not found with id " + id);
        }
        questionRepository.deleteById(id);
    }

    // Métodos para convertir entre Question y QuestionDTO
    private QuestionDTO convertToDTO(Question question) {
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setId(question.getId());
        questionDTO.setQuestionText(question.getQuestionText());
        questionDTO.setCategory(question.getCategory());
        questionDTO.setCorrectAnswer(question.getCorrectAnswer());
        questionDTO.setOption1(question.getOption1());
        questionDTO.setOption2(question.getOption2());
        questionDTO.setOption3(question.getOption3());
        questionDTO.setOption4(question.getOption4());
        return questionDTO;
    }

    private Question convertToEntity(QuestionDTO questionDTO) {
        Question question = new Question();
        question.setQuestionText(questionDTO.getQuestionText());
        question.setCategory(questionDTO.getCategory());
        question.setCorrectAnswer(questionDTO.getCorrectAnswer());
        question.setOption1(questionDTO.getOption1());
        question.setOption2(questionDTO.getOption2());
        question.setOption3(questionDTO.getOption3());
        question.setOption4(questionDTO.getOption4());
        return question;
    }
}
