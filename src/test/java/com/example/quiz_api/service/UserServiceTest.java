package com.example.quiz_api.service;

import com.example.quiz_api.dto.UserDTO;
import com.example.quiz_api.dto.UserResponseDTO;
import com.example.quiz_api.entity.User;
import com.example.quiz_api.exception.ResourceNotFoundException;
import com.example.quiz_api.exception.UserAlreadyExistsException;
import com.example.quiz_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setPassword("plainpassword");

        userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setEmail("testuser@example.com");
        userDTO.setPassword("plainpassword");
    }

    @Test
    void createUser_whenUsernameDoesNotExist_shouldCreateUser() {
        // GIVEN
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("testuser@example.com")).thenReturn(false);
        when(passwordEncoder.encode("plainpassword")).thenReturn("hashedpassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(1L); // Simula que la base de datos asigna ID 1
            return u;
        });

        // WHEN
        UserResponseDTO responseDTO = userService.createUser(userDTO);

        // THEN
        assertNotNull(responseDTO);
        assertEquals(1L, responseDTO.getId());
        assertEquals("testuser", responseDTO.getUsername());
        assertEquals("testuser@example.com", responseDTO.getEmail());

        verify(userRepository, times(1)).existsByUsername("testuser");
        verify(userRepository, times(1)).existsByEmail("testuser@example.com");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_whenUsernameExists_shouldThrowException() {
        // GIVEN
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        // WHEN & THEN
        assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(userDTO));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getUserById_whenUserExists_shouldReturnUser() {
        // GIVEN
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // WHEN
        UserResponseDTO responseDTO = userService.getUserById(1L);

        // THEN
        assertNotNull(responseDTO);
        assertEquals("testuser", responseDTO.getUsername());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserById_whenUserNotFound_shouldThrowException() {
        // GIVEN
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(999L));
    }

    @Test
    void validateUser_whenCredentialsAreValid_shouldReturnTrue() {
        // GIVEN
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("plainpassword", "plainpassword")).thenReturn(true);

        // WHEN
        boolean isValid = userService.validateUser("testuser", "plainpassword");

        // THEN
        assertTrue(isValid);
    }

    @Test
    void validateUser_whenCredentialsAreInvalid_shouldReturnFalse() {
        // GIVEN
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpassword", "plainpassword")).thenReturn(false);

        // WHEN
        boolean isValid = userService.validateUser("testuser", "wrongpassword");

        // THEN
        assertFalse(isValid);
    }
}
