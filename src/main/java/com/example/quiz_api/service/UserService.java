package com.example.quiz_api.service;

import com.example.quiz_api.dto.UserDTO;
import com.example.quiz_api.entity.User;
import com.example.quiz_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.quiz_api.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


    @Service
    @RequiredArgsConstructor
    public class UserService {
        @Autowired
        private UserRepository userRepository;

        public List<UserDTO> getAllUsers() {
            return userRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
        }

        public UserDTO getUserById(Long id) {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
            return convertToDTO(user);
        }

        public UserDTO createUser(UserDTO userDTO) {
            User user = convertToEntity(userDTO);
            User savedUser = userRepository.save(user);
            return convertToDTO(savedUser);
        }

        public UserDTO updateUser(Long id, UserDTO userDTO) {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));

            user.setUsername(userDTO.getUsername());
            user.setEmail(userDTO.getEmail());

            // Actualizar la contraseña si se proporciona en el DTO
            if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                user.setPassword(userDTO.getPassword());
            }

            User updatedUser = userRepository.save(user);
            return convertToDTO(updatedUser);
        }



        public void deleteUser(Long id) {
            if (!userRepository.existsById(id)) {
                throw new ResourceNotFoundException("User not found with id " + id);
            }
            userRepository.deleteById(id);
        }

        // Métodos para convertir entre User y UserDTO
        private UserDTO convertToDTO(User user) {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setUsername(user.getUsername());
            userDTO.setEmail(user.getEmail());
            userDTO.setPassword(user.getPassword()); // Incluir la contraseña en el DTO
            return userDTO;
        }

        private User convertToEntity(UserDTO userDTO) {
            User user = new User();
            user.setUsername(userDTO.getUsername());
            user.setEmail(userDTO.getEmail());
            user.setPassword(userDTO.getPassword()); // Asignar la contraseña desde el DTO
            return user;
        }
    }
