package com.example.quiz_api.service;

import com.example.quiz_api.dto.UserDTO;
import com.example.quiz_api.entity.User;
import com.example.quiz_api.exception.ResourceNotFoundException;
import com.example.quiz_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    /**
     * Obtener todos los usuarios en formato DTO.
     *
     * @return Lista de UserDTO.
     */
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener un usuario por ID.
     *
     * @param id Identificador del usuario.
     * @return UserDTO.
     */
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        return convertToDTO(user);
    }

    public Long getUserIdByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(User::getId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username " + username));
    }
    /**
     * Crear un nuevo usuario.
     *
     * @param userDTO Datos del usuario.
     * @return UserDTO creado.
     */
    public UserDTO createUser(UserDTO userDTO) {
        User user = convertToEntity(userDTO);
        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    /**
     * Actualizar un usuario existente.
     *
     * @param id      ID del usuario.
     * @param userDTO Datos a actualizar.
     * @return UserDTO actualizado.
     */
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

    /**
     * Eliminar un usuario por ID.
     *
     * @param id Identificador del usuario.
     */
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id " + id);
        }
        userRepository.deleteById(id);
    }

    /**
     * Validar las credenciales de un usuario.
     *
     * @param username Nombre de usuario.
     * @param password Contraseña.
     * @return true si las credenciales son válidas, false en caso contrario.
     */
    public boolean validateUser(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Comparar directamente las contraseñas sin cifrar
            return user.getPassword().equals(password);
        }
        return false; // Usuario no encontrado o credenciales inválidas
    }

    // Métodos privados para conversión entre User y UserDTO
    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword()); // Incluir la contraseña en el DTO para pruebas
        return userDTO;
    }

    private User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        return user;
    }
}
