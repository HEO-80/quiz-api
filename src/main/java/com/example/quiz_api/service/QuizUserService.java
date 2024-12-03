package com.example.quiz_api.service;

import com.example.quiz_api.dto.QuizUserDTO;
import com.example.quiz_api.entity.QuizUser;
import com.example.quiz_api.entity.User;
import com.example.quiz_api.exception.ResourceNotFoundException;
import com.example.quiz_api.repository.QuizUserRepository;
import com.example.quiz_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizUserService {

    @Autowired
    private QuizUserRepository quizUserRepository;

    @Autowired
    private UserRepository userRepository;

    public List<QuizUserDTO> getAllQuizUsers() {
        return quizUserRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public QuizUserDTO getQuizUserById(Long id) {
        QuizUser quizUser = quizUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("QuizUser not found with id " + id));
        return convertToDTO(quizUser);
    }

    public QuizUserDTO createQuizUser(QuizUserDTO quizUserDTO) {
        QuizUser quizUser = convertToEntity(quizUserDTO);
        QuizUser savedQuizUser = quizUserRepository.save(quizUser);
        return convertToDTO(savedQuizUser);
    }

    public QuizUserDTO updateQuizUser(Long id, QuizUserDTO quizUserDTO) {
        QuizUser quizUser = quizUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("QuizUser not found with id " + id));

        quizUser.setCategory(quizUserDTO.getCategory());
        quizUser.setStartTime(quizUserDTO.getStartTime());
        quizUser.setEndTime(quizUserDTO.getEndTime());
        quizUser.setScore(quizUserDTO.getScore());

        // Actualiza el usuario solo si el ID ha cambiado
        if (!quizUser.getUser().getId().equals(quizUserDTO.getUserId())) {
            User user = userRepository.findById(quizUserDTO.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + quizUserDTO.getUserId()));
            quizUser.setUser(user);
        }

        QuizUser updatedQuizUser = quizUserRepository.save(quizUser);
        return convertToDTO(updatedQuizUser);
    }

    public void deleteQuizUser(Long id) {
        if (!quizUserRepository.existsById(id)) {
            throw new ResourceNotFoundException("QuizUser not found with id " + id);
        }
        quizUserRepository.deleteById(id);
    }

    // Métodos para convertir entre QuizUser y QuizUserDTO
    private QuizUserDTO convertToDTO(QuizUser quizUser) {
        QuizUserDTO quizUserDTO = new QuizUserDTO();
        quizUserDTO.setId(quizUser.getId());
        quizUserDTO.setUserId(quizUser.getUser().getId()); // Accede al ID del usuario a partir del objeto User
        quizUserDTO.setCategory(quizUser.getCategory());
        quizUserDTO.setStartTime(quizUser.getStartTime());
        quizUserDTO.setEndTime(quizUser.getEndTime());
        quizUserDTO.setScore(quizUser.getScore());
        return quizUserDTO;
    }

    private QuizUser convertToEntity(QuizUserDTO quizUserDTO) {
        QuizUser quizUser = new QuizUser();

        User user = userRepository.findById(quizUserDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + quizUserDTO.getUserId()));

        quizUser.setUser(user); // Establece el objeto User completo
        quizUser.setCategory(quizUserDTO.getCategory());
        quizUser.setStartTime(quizUserDTO.getStartTime());
        quizUser.setEndTime(quizUserDTO.getEndTime());
        quizUser.setScore(quizUserDTO.getScore());
        return quizUser;
    }
}
