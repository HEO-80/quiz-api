package com.example.quiz_api.service;

import com.example.quiz_api.dto.QuizUserDTO;
import com.example.quiz_api.entity.QuizUser;
import com.example.quiz_api.entity.User;
import com.example.quiz_api.repository.QuizUserRepository;
import com.example.quiz_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.quiz_api.exception.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuizUserService {

    @Autowired
    private QuizUserRepository quizUserRepository;

    @Autowired
    private UserRepository userRepository;

    public List<QuizUserDTO> getAllQuizzes() {
        return quizUserRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public QuizUserDTO getQuizById(Long id) {
        QuizUser quizUser = quizUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found with id " + id));
        return convertToDTO(quizUser);
    }

    /**
     * Obtiene todos los quizzes realizados por un usuario específico.
     */
    public List<QuizUserDTO> getQuizzesByUserId(Long userId) {
        List<QuizUser> quizzes = quizUserRepository.findByUserId(userId);
        return quizzes.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    public QuizUserDTO createQuiz(QuizUserDTO quizUserDTO) {
        QuizUser quizUser = convertToEntity(quizUserDTO);
        QuizUser savedQuizUser = quizUserRepository.save(quizUser);
        return convertToDTO(savedQuizUser);
    }

    public QuizUserDTO updateQuiz(Long id, QuizUserDTO quizUserDTO) {
        QuizUser quizUser = quizUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found with id " + id));

        User user = userRepository.findById(quizUserDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + quizUserDTO.getUserId()));

        quizUser.setCategory(quizUserDTO.getCategory());
        quizUser.setUser(user);
        quizUser.setStartTime(quizUserDTO.getStartTime());
        quizUser.setEndTime(quizUserDTO.getEndTime());
        quizUser.setScore(quizUserDTO.getScore());

        QuizUser updatedQuizUser = quizUserRepository.save(quizUser);
        return convertToDTO(updatedQuizUser);
    }

    public void deleteQuiz(Long id) {
        if (!quizUserRepository.existsById(id)) {
            throw new ResourceNotFoundException("Quiz not found with id " + id);
        }
        quizUserRepository.deleteById(id);
    }

    // Métodos para convertir entre QuizUser y QuizUserDTO
    private QuizUserDTO convertToDTO(QuizUser quizUser) {
        QuizUserDTO quizUserDTO = new QuizUserDTO();
        quizUserDTO.setId(quizUser.getId());
        quizUserDTO.setUserId(quizUser.getUser().getId());
        quizUserDTO.setCategory(quizUser.getCategory());
        quizUserDTO.setStartTime(quizUser.getStartTime());
        quizUserDTO.setEndTime(quizUser.getEndTime());
        quizUserDTO.setScore(quizUser.getScore());
        return quizUserDTO;
    }

    private QuizUser convertToEntity(QuizUserDTO quizUserDTO) {
        QuizUser quizUser = new QuizUser();
        quizUser.setCategory(quizUserDTO.getCategory());
        quizUser.setStartTime(quizUserDTO.getStartTime());
        quizUser.setEndTime(quizUserDTO.getEndTime());
        quizUser.setScore(quizUserDTO.getScore());

        // Obtenemos el objeto User a partir del userId proporcionado en el DTO
        User user = userRepository.findById(quizUserDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + quizUserDTO.getUserId()));
        quizUser.setUser(user);

        return quizUser;
    }
}
