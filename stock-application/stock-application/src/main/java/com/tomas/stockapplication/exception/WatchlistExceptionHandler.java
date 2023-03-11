package com.tomas.stockapplication.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class WatchlistExceptionHandler {

    @ExceptionHandler(value = WatchlistNotFoundException.class)
    public ResponseEntity<String> handleWatchlistNotFound(WatchlistNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
