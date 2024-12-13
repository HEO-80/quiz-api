package com.example.quiz_api.service;

import com.example.quiz_api.dto.UserAnswerDTO;
import com.example.quiz_api.entity.UserAnswer;
import com.example.quiz_api.exception.ResourceNotFoundException;
import com.example.quiz_api.repository.UserAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import com.example.quiz_api.entity.QuizUser;
import com.example.quiz_api.entity.Question;
import com.example.quiz_api.entity.User;
import com.example.quiz_api.repository.QuizUserRepository;
import com.example.quiz_api.repository.QuestionRepository;
import com.example.quiz_api.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserAnswerService {
    @Autowired
    private UserAnswerRepository userAnswerRepository;

    @Autowired
    private QuizUserRepository quizUserRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    public List<UserAnswerDTO> getAllUserAnswers() {
        List<UserAnswer> userAnswers = userAnswerRepository.findAll();
        System.out.println("Registros recuperados: " + userAnswers.size());
        return userAnswerRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

//    public List<UserAnswerDTO> getAllUserAnswers() {
//        return userAnswerRepository.findAll()
//                .stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }

    public UserAnswerDTO getUserAnswerById(Long id) {
        UserAnswer userAnswer = userAnswerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User answer not found with id " + id));
        return convertToDTO(userAnswer);
    }

    public UserAnswerDTO createUserAnswer(UserAnswerDTO userAnswerDTO) {
        UserAnswer userAnswer = convertToEntity(userAnswerDTO);
        UserAnswer savedUserAnswer = userAnswerRepository.save(userAnswer);
        return convertToDTO(savedUserAnswer);
    }

    public UserAnswerDTO updateUserAnswer(Long id, UserAnswerDTO userAnswerDTO) {
        UserAnswer userAnswer = userAnswerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User answer not found with id " + id));

        // Actualizamos las relaciones con entidades relacionadas
        QuizUser quizUser = quizUserRepository.findById(userAnswerDTO.getQuizId())
                .orElseThrow(() -> new ResourceNotFoundException("QuizUser not found with id " + userAnswerDTO.getQuizId()));
        userAnswer.setQuiz(quizUser);

        Question question = questionRepository.findById(userAnswerDTO.getQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id " + userAnswerDTO.getQuestionId()));
        userAnswer.setQuestion(question);

        User user = userRepository.findById(userAnswerDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userAnswerDTO.getUserId()));
        userAnswer.setUser(user);

        userAnswer.setUserAnswer(userAnswerDTO.getUserAnswer());
        userAnswer.setIsCorrect(userAnswerDTO.getIsCorrect());

        UserAnswer updatedUserAnswer = userAnswerRepository.save(userAnswer);
        return convertToDTO(updatedUserAnswer);
    }

    public void deleteUserAnswer(Long id) {
        if (!userAnswerRepository.existsById(id)) {
            throw new ResourceNotFoundException("User answer not found with id " + id);
        }
        userAnswerRepository.deleteById(id);
    }

    private UserAnswerDTO convertToDTO(UserAnswer userAnswer) {
        UserAnswerDTO dto = new UserAnswerDTO();
        dto.setId(userAnswer.getId());

        // Asegurarse de que las relaciones no son nulas antes de acceder
        if (userAnswer.getQuiz() != null) {
            dto.setQuizId(userAnswer.getQuiz().getId());
        }

        if (userAnswer.getQuestion() != null) {
            dto.setQuestionId(userAnswer.getQuestion().getId());
        }

        if (userAnswer.getUser() != null) {
            dto.setUserId(userAnswer.getUser().getId());
        }

//        dto.setQuizId(userAnswer.getQuiz().getId());
//        dto.setQuestionId(userAnswer.getQuestion().getId());
//        dto.setUserId(userAnswer.getUser().getId());

        dto.setUserAnswer(userAnswer.getUserAnswer());
        dto.setIsCorrect(userAnswer.getIsCorrect());
        return dto;
    }


//    private UserAnswerDTO convertToDTO(UserAnswer userAnswer) {
//        UserAnswerDTO dto = new UserAnswerDTO();
//        dto.setId(userAnswer.getId());
//        dto.setQuizId(userAnswer.getQuiz().getId()); // Verifica el acceso correcto a las relaciones
//        dto.setQuestionId(userAnswer.getQuestion().getId());
//        dto.setUserId(userAnswer.getUser().getId());
//        dto.setUserAnswer(userAnswer.getUserAnswer());
//        dto.setIsCorrect(userAnswer.getIsCorrect());
//        return dto;
//    }

    private UserAnswer convertToEntity(UserAnswerDTO dto) {
        UserAnswer userAnswer = new UserAnswer();

        // Cargar entidades relacionadas usando los repositorios
        QuizUser quizUser = quizUserRepository.findById(dto.getQuizId())
                .orElseThrow(() -> new ResourceNotFoundException("QuizUser not found with id " + dto.getQuizId()));
        userAnswer.setQuiz(quizUser);

        Question question = questionRepository.findById(dto.getQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id " + dto.getQuestionId()));
        userAnswer.setQuestion(question);

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + dto.getUserId()));
        userAnswer.setUser(user);

        userAnswer.setUserAnswer(dto.getUserAnswer());
        userAnswer.setIsCorrect(dto.getIsCorrect());
        return userAnswer;
    }
}
