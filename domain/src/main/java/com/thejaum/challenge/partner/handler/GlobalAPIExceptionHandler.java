package com.thejaum.challenge.partner.handler;

import com.thejaum.challenge.partner.dto.ErrorDetailsDTO;
import com.thejaum.challenge.partner.dto.FieldErrorDetailsDTO;
import com.thejaum.challenge.partner.exception.NotFoundException;
import com.thejaum.challenge.partner.exception.WrongGeometryTypeException;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class GlobalAPIExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(WrongGeometryTypeException.class)
    public ResponseEntity<ErrorDetailsDTO> wrongGeometryTypeException(final WrongGeometryTypeException ex, WebRequest request) {
        ErrorDetailsDTO message = ErrorDetailsDTO.builder()
                .statusCode(BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .description(request.getDescription(false))
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

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

    @Override
    protected ResponseEntity<Object> handleBindException(
            final BindException ex,
            final HttpHeaders headers,
            final HttpStatus status,
            final WebRequest request
    ) {
        ErrorDetailsDTO message = ErrorDetailsDTO.builder()
                .statusCode(BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .description(request.getDescription(false))
                .fieldList(getListErros(ex.getBindingResult(), ClassUtils.getShortClassName(ex.getClass()), ex))
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException ex,
            final HttpHeaders headers,
            final HttpStatus status,
            final WebRequest request
    ) {
        ErrorDetailsDTO message = ErrorDetailsDTO.builder()
                .statusCode(BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .description(request.getDescription(false))
                .fieldList(getListErros(ex.getBindingResult(), ClassUtils.getShortClassName(ex.getClass()), ex))
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    private List<FieldErrorDetailsDTO> getListErros(
            final BindingResult bindingResult,
            final String shortClassName,
            final Exception ex
    ) {
        List<FieldErrorDetailsDTO> errors = new ArrayList<>();
        bindingResult.getFieldErrors().forEach(error -> {
            String exception = shortClassName;
            errors.add(FieldErrorDetailsDTO.builder()
                    .fieldName(error.getField())
                    .description(error.getDefaultMessage())
                    .build());
        });
        return errors;
    }
}
