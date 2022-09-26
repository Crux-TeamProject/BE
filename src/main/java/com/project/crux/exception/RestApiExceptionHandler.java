package com.project.crux.exception;

import com.project.crux.common.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.Objects;

@RestControllerAdvice
public class RestApiExceptionHandler {

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ResponseDto<?>> handleApiRequestException(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(ResponseDto.fail("BAD_REQUEST", Objects.requireNonNull(ex.getFieldError()).getDefaultMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<ResponseDto<?>> handleApiRequestException(ConstraintViolationException ex ) {
        return new ResponseEntity<>(ResponseDto.fail("BAD_REQUEST", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {CustomException.class})
    public ResponseEntity<ResponseDto<?>> handleApiRequestException(CustomException ex) {
        return new ResponseEntity<>(ResponseDto.fail(ex.getErrorCode().getHttpStatus().name(), ex.getErrorCode().getErrorMessage()), ex.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ResponseDto<?>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.fail("BAD_REQUEST", ex.getMessage()));
    }
}
