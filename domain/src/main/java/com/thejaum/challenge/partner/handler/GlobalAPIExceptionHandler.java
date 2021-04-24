package com.thejaum.challenge.partner.handler;

import com.thejaum.challenge.partner.dto.ErrorDetailsDTO;
import com.thejaum.challenge.partner.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalAPIExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDetailsDTO> resourceNotFoundException(final NotFoundException ex, WebRequest request) {
        ErrorDetailsDTO message = ErrorDetailsDTO.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .description(request.getDescription(false))
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetailsDTO> globalExceptionHandler(final Exception ex, WebRequest request) {
        ErrorDetailsDTO message = ErrorDetailsDTO.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .description(request.getDescription(false))
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
    }
}
