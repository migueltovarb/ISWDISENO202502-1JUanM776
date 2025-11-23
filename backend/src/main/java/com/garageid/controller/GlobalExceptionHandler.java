package com.garageid.controller;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(DuplicateKeyException.class)
  public ResponseEntity<Map<String,String>> handleDuplicate(DuplicateKeyException ex){
    return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Duplicado", "detail", ex.getMessage()));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String,String>> handleBad(IllegalArgumentException ex){
    String msg = ex.getMessage()==null?"Error":ex.getMessage();
    HttpStatus status = msg.toLowerCase().contains("placa") ? HttpStatus.CONFLICT : HttpStatus.BAD_REQUEST;
    return ResponseEntity.status(status).body(Map.of("message", msg));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String,String>> handleAny(Exception ex){
    String msg = ex.getMessage()==null?"Error":ex.getMessage();
    if (msg.contains("E11000") || msg.toLowerCase().contains("duplicate key")){
      return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Duplicado", "detail", msg));
    }
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", msg));
  }
}
