package com.example.quiz_api.controller;

import com.example.quiz_api.dto.UserDTO;
import com.example.quiz_api.dto.UserResponseDTO;
import com.example.quiz_api.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void testCreateUser_Success() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("Carlos");
        userDTO.setPassword("password123");
        userDTO.setEmail("carlos@gmail.com");

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(1L);
        userResponseDTO.setUsername("Carlos");
        userResponseDTO.setEmail("carlos@gmail.com");

        Mockito.when(userService.createUser(Mockito.any(UserDTO.class))).thenReturn(userResponseDTO);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("Carlos"))
                .andExpect(jsonPath("$.email").value("carlos@gmail.com"));
    }

    @Test
    public void testCreateUser_Unauthorized() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("Carlos");
        userDTO.setPassword("password123");
        userDTO.setEmail("carlos@gmail.com");

        Mockito.when(userService.createUser(Mockito.any(UserDTO.class))).thenThrow(new UnauthorizedException("No autorizado"));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andExpect(status().isUnauthorized());
    }
}
