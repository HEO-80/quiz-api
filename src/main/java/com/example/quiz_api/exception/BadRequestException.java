package com.example.quiz_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción personalizada para solicitudes incorrectas.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    /**
     * Constructor de la excepción con un mensaje.
     * @param message Mensaje descriptivo del error.
     */
    public BadRequestException(String message) {
        super(message);
    }
}
