package com.example.quiz_api.dto;

public class LoginResponseDTO {
    private String message;
    private String username;
    private Long userId;
    private String accessToken; // Nuevo campo para el token JWT
    private String tokenType = "Bearer"; // Tipo de token

    public LoginResponseDTO() {
    }

    // Constructor para mensajes de error rápido si deseas
    public LoginResponseDTO(String message, String username, Long userId, String accessToken) {
        this.message = message;
        this.username = username;
        this.userId = userId;
        this.accessToken = accessToken;
    }

    // Getters y Setters

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}
