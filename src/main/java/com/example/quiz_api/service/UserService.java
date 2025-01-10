package com.example.quiz_api.service;

import com.example.quiz_api.controller.LoginController;
import com.example.quiz_api.dto.UserDTO;
import com.example.quiz_api.dto.UserResponseDTO;
import com.example.quiz_api.entity.User;
import com.example.quiz_api.exception.ResourceNotFoundException;
import com.example.quiz_api.exception.UserAlreadyExistsException;
import com.example.quiz_api.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

//    @Autowired
//    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Obtener todos los usuarios en formato DTO.
     *
     * @return Lista de UserDTO.
     */
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener un usuario por ID.
     *
     * @param id Identificador del usuario.
     * @return UserDTO.
     */
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        return convertToResponseDTO(user);
    }

    public Long getUserIdByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(User::getId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username " + username));
    }
    /**
     * Crear un nuevo usuario después de validar que el nombre de usuario y el email son únicos.
     *
     * @param userDTO Datos del usuario.
     * @return UserDTO creado.
     */
    public UserResponseDTO createUser(UserDTO userDTO) {
        logger.info("Intentando crear un nuevo usuario: {}", userDTO.getUsername());

        // Verificar si el nombre de usuario ya existe
        if(userRepository.existsByUsername(userDTO.getUsername())) {
            logger.warn("El nombre de usuario {} ya está en uso.", userDTO.getUsername());
            throw new UserAlreadyExistsException("El nombre de usuario " + userDTO.getUsername() + " ya está en uso.");

        }


        // Verificar si el correo electrónico ya está registrado
        if(userRepository.existsByEmail(userDTO.getEmail())) {
            logger.warn("El correo electrónico {} ya está en uso.", userDTO.getEmail());
            throw new UserAlreadyExistsException("El correo electrónico " + userDTO.getEmail() + " ya está registrado.");
        }

        // **Hash de la contraseña antes de guardar**
        String hashedPassword = passwordEncoder.encode(userDTO.getPassword());
        logger.debug("Hash generado para la contraseña: {}", hashedPassword);
        logger.debug("Longitud del hash generado: {}", hashedPassword.length());

        User user = convertToEntity(userDTO);
        user.setPassword(hashedPassword); // Establecer la contraseña hasheada
        logger.debug("Entidad de usuario antes de guardar: {}", user);

        User savedUser = userRepository.save(user);
        logger.info("Usuario creado con éxito con ID: {}", savedUser.getId());


//        User user = convertToEntity(userDTO);
//        User savedUser = userRepository.save(user);
        return convertToResponseDTO(savedUser);
    }

    /**
     * Actualizar un usuario existente.
     *
     * @param id      ID del usuario.
     * @param userDTO Datos a actualizar.
     * @return UserDTO actualizado.
     */
    public UserResponseDTO  updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));

        // Verificar si el nuevo nombre de usuario está disponible
        if(!user.getUsername().equals(userDTO.getUsername()) && userRepository.existsByUsername(userDTO.getUsername())) {
            throw new UserAlreadyExistsException("Username is already taken.");
        }

        // Verificar si el nuevo correo electrónico está disponible
        if(!user.getEmail().equals(userDTO.getEmail()) && userRepository.existsByEmail(userDTO.getEmail())) {
            throw new UserAlreadyExistsException("Email is already in use.");
        }

        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());

        // Actualizar la contraseña si se proporciona en el DTO
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            String hashedPassword = passwordEncoder.encode(userDTO.getPassword());
            user.setPassword(hashedPassword); // Establecer la contraseña hasheada
        }

        User updatedUser = userRepository.save(user);
        return convertToResponseDTO(updatedUser);
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
            // **Usar passwordEncoder para comparar contraseñas**
            logger.debug("Contraseña almacenada para usuario {}: {}", username, user.getPassword());
            boolean matches = passwordEncoder.matches(password, user.getPassword());
            logger.debug("¿La contraseña proporcionada coincide? {}", matches);
            return matches;
        }
        logger.debug("Usuario {} no encontrado.", username);
        return false; // Usuario no encontrado o credenciales inválidas
    }

    private UserResponseDTO convertToResponseDTO(User user) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(user.getId());
        userResponseDTO.setUsername(user.getUsername());
        userResponseDTO.setEmail(user.getEmail());
        return userResponseDTO;
    }



    /**
     * Convertir un UserDTO a una entidad User.
     *
     * @param userDTO Objeto UserDTO.
     * @return Entidad User.
     */
    private User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        // La contraseña se establece en el metodo createUser y updateUser después de hashear
//        user.setPassword(userDTO.getPassword());
        return user;
    }

    /**
     * Método temporal para hashear contraseñas de usuarios existentes que no están hasheadas.
     * **Usar con cuidado y eliminar después de su uso.**
     */
    public void hashExistingPasswords() {
        List<User> users = userRepository.findAll();
        for(User user : users){
            if(!isPasswordHashed(user.getPassword())){
                // Asumiendo que las contraseñas están en texto plano
                String hashedPassword = passwordEncoder.encode(user.getPassword());
                user.setPassword(hashedPassword);
                userRepository.save(user);
                logger.info("Contraseña del usuario {} ha sido hasheada.", user.getUsername());
            }
        }
    }

    /**
     * Verifica si una contraseña ya está hasheada con BCrypt.
     *
     * @param password Contraseña a verificar.
     * @return true si está hasheada, false en caso contrario.
     */
    private boolean isPasswordHashed(String password) {
        return password.startsWith("$2a$") || password.startsWith("$2b$") || password.startsWith("$2y$");
    }
}
