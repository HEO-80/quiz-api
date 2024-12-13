package com.example.quiz_api.service;

import com.example.quiz_api.dto.QuizQuestionDTO;
import com.example.quiz_api.entity.Question;
import com.example.quiz_api.entity.QuizQuestion;
import com.example.quiz_api.entity.QuizUser;
import com.example.quiz_api.exception.ResourceNotFoundException;
import com.example.quiz_api.repository.QuizQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import com.example.quiz_api.repository.QuizUserRepository;
import com.example.quiz_api.repository.QuestionRepository;

@Service
public class QuizQuestionService {

    @Autowired
    private QuizQuestionRepository quizQuestionRepository;

    @Autowired
    private QuizUserRepository quizUserRepository;

    @Autowired
    private QuestionRepository questionRepository;

    public List<QuizQuestionDTO> getAllQuizQuestions() {
        return quizQuestionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public QuizQuestionDTO getQuizQuestionById(Long id) {
        QuizQuestion quizQuestion = quizQuestionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("QuizQuestion not found with id " + id));
        return convertToDTO(quizQuestion);
    }

    public QuizQuestionDTO createQuizQuestion(QuizQuestionDTO quizQuestionDTO) {
        QuizQuestion quizQuestion = convertToEntity(quizQuestionDTO);
        QuizQuestion savedQuizQuestion = quizQuestionRepository.save(quizQuestion);
        return convertToDTO(savedQuizQuestion);
    }

//    public QuizQuestionDTO createQuizQuestion(QuizQuestionDTO quizQuestionDTO) {
//        QuizQuestion quizQuestion = convertToEntity(quizQuestionDTO);
//
//        // Asegúrate de que el `id` no se asigna en `convertToEntity` si usas el trigger
//        quizQuestion.setId(null); // Garantiza que el trigger o @GeneratedValue asigne el `id`
//
//        QuizQuestion savedQuizQuestion = quizQuestionRepository.save(quizQuestion);
//        return convertToDTO(savedQuizQuestion);
//    }

    public QuizQuestionDTO updateQuizQuestion(Long id, QuizQuestionDTO quizQuestionDTO) {
        QuizQuestion quizQuestion = quizQuestionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("QuizQuestion not found with id " + id));

        // Actualizamos los valores según el DTO
        QuizUser quizUser = quizUserRepository.findById(quizQuestionDTO.getQuizId())
                .orElseThrow(() -> new ResourceNotFoundException("QuizUser not found with id " + quizQuestionDTO.getQuizId()));
        quizQuestion.setQuiz(quizUser);

        Question question = questionRepository.findById(quizQuestionDTO.getQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id " + quizQuestionDTO.getQuestionId()));
        quizQuestion.setQuestion(question);

        quizQuestion.setUserAnswer(quizQuestionDTO.getUserAnswer());
        quizQuestion.setIsCorrect(quizQuestionDTO.getIsCorrect());

        QuizQuestion updatedQuizQuestion = quizQuestionRepository.save(quizQuestion);
        return convertToDTO(updatedQuizQuestion);
    }

    public void deleteQuizQuestion(Long id) {
        if (!quizQuestionRepository.existsById(id)) {
            throw new ResourceNotFoundException("QuizQuestion not found with id " + id);
        }
        quizQuestionRepository.deleteById(id);
    }

    // Métodos para convertir entre entidad y DTO
    private QuizQuestionDTO convertToDTO(QuizQuestion quizQuestion) {
        QuizQuestionDTO dto = new QuizQuestionDTO();
        dto.setId(quizQuestion.getId());
        dto.setQuizId(quizQuestion.getQuiz().getId());
        dto.setQuestionId(quizQuestion.getQuestion().getId());
        dto.setUserAnswer(quizQuestion.getUserAnswer());
        dto.setIsCorrect(quizQuestion.getIsCorrect());
        return dto;
    }

    private QuizQuestion convertToEntity(QuizQuestionDTO quizQuestionDTO) {
        QuizQuestion quizQuestion = new QuizQuestion();

        // Cargar entidades relacionadas usando los repositorios
        QuizUser quizUser = quizUserRepository.findById(quizQuestionDTO.getQuizId())
                .orElseThrow(() -> new ResourceNotFoundException("QuizUser not found with id " + quizQuestionDTO.getQuizId()));
        quizQuestion.setQuiz(quizUser);

        Question question = questionRepository.findById(quizQuestionDTO.getQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id " + quizQuestionDTO.getQuestionId()));
        quizQuestion.setQuestion(question);

        quizQuestion.setUserAnswer(quizQuestionDTO.getUserAnswer());
        quizQuestion.setIsCorrect(quizQuestionDTO.getIsCorrect());
        return quizQuestion;
    }
}
