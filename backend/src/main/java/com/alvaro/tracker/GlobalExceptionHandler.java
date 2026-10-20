package com.alvaro.tracker;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice 
public class GlobalExceptionHandler {
   
   @ExceptionHandler (MethodArgumentNotValidException.class)
   public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
      ProblemDetail problem = ProblemDetail.forStatusAndDetail(
         HttpStatus.BAD_REQUEST, "Datos de la operacion no válidos");

      Map<String, String> errors = new HashMap<>();
      ex.getBindingResult().getFieldErrors()
         .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

      problem.setProperty("errors", errors);
      return problem;
   }

   @ExceptionHandler(HttpMessageNotReadableException.class)
   public ProblemDetail handleUnreadable(HttpMessageNotReadableException ex) {
      return ProblemDetail.forStatusAndDetail(
               HttpStatus.BAD_REQUEST,
               "JSON mal formado o con un valor no permitido (type debe ser BUY o SELL)");
   }

   @ExceptionHandler(IllegalArgumentException.class)
   public ProblemDetail handleIllegalArgument(IllegalArgumentException ex) {
      return ProblemDetail.forStatusAndDetail(
         HttpStatus.BAD_REQUEST, ex.getMessage());
   }
}
