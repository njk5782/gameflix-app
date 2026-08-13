package com.example.gameflix.controller;

import com.example.gameflix.dto.AuthResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AuthResponse handleBadRequest(IllegalArgumentException exception) {
        return new AuthResponse(exception.getMessage());
    }
}
