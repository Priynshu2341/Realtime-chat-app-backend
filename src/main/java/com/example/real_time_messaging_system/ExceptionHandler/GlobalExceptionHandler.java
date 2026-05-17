package com.example.real_time_messaging_system.ExceptionHandler;

import com.example.real_time_messaging_system.dto.response.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(
            EntityNotFoundException ex
    ) {

        ErrorResponse response =
                ErrorResponse.builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .content(ex.getMessage())
                        .localDateTime(LocalDateTime.now())
                        .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex
    ) {

        ErrorResponse response =
                ErrorResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .content(ex.getMessage())
                        .localDateTime(LocalDateTime.now())
                        .build();

        return ResponseEntity.badRequest()
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex
    ) {

        String message =
                ex.getBindingResult()
                        .getFieldError()
                        .getDefaultMessage();

        ErrorResponse response =
                ErrorResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .content(message)
                        .localDateTime(LocalDateTime.now())
                        .build();

        return ResponseEntity.badRequest()
                .body(response);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleFileTooLarge(
            MaxUploadSizeExceededException ex
    ) {

        ErrorResponse response =
                ErrorResponse.builder()
                        .status(HttpStatus.PAYLOAD_TOO_LARGE.value())
                        .content("File size exceeds limit")
                        .localDateTime(LocalDateTime.now())
                        .build();

        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobal(
            Exception ex
    ) {

        ErrorResponse response =
                ErrorResponse.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .content(ex.getMessage())
                        .localDateTime(LocalDateTime.now())
                        .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}