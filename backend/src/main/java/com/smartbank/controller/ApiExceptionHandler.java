package com.smartbank.controller;
import org.slf4j.Logger;import org.slf4j.LoggerFactory;import org.springframework.http.*;import org.springframework.web.bind.MethodArgumentNotValidException;import org.springframework.web.bind.annotation.*;import org.springframework.web.server.ResponseStatusException;import java.time.Instant;import java.util.*;
@RestControllerAdvice public class ApiExceptionHandler {
 private static final Logger log=LoggerFactory.getLogger(ApiExceptionHandler.class);
 @ExceptionHandler(ResponseStatusException.class) public ResponseEntity<?> status(ResponseStatusException e){return ResponseEntity.status(e.getStatusCode()).body(Map.of("timestamp",Instant.now().toString(),"message",Objects.toString(e.getReason(),"Request failed")));}
 @ExceptionHandler(MethodArgumentNotValidException.class) public ResponseEntity<?> validation(MethodArgumentNotValidException e){String msg=e.getBindingResult().getFieldErrors().stream().findFirst().map(x->x.getDefaultMessage()).orElse("Please check the submitted fields.");return ResponseEntity.badRequest().body(Map.of("timestamp",Instant.now().toString(),"message",msg));}
 @ExceptionHandler(Exception.class) public ResponseEntity<?> other(Exception e){log.error("Unhandled API request error",e);return ResponseEntity.status(500).body(Map.of("timestamp",Instant.now().toString(),"message","The request could not be completed."));}
}
