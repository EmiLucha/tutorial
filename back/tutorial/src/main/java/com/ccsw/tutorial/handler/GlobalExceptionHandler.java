package com.ccsw.tutorial.handler;

import com.ccsw.tutorial.exception.DuplicateNameException;
import com.ccsw.tutorial.exception.GameAlreadyInLoanException;
import com.ccsw.tutorial.exception.TooManyLoansException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateNameException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateNameException(DuplicateNameException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(GameAlreadyInLoanException.class)
    public ResponseEntity<Map<String, String>> handleGameAlreadyInLoanException(GameAlreadyInLoanException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "NotAvailable");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(TooManyLoansException.class)
    public ResponseEntity<Map<String, String>> handleTooManyLoansException(TooManyLoansException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "ManyLoans");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

}
