package com.clinica.procedimentos.controller.exeptionhandler;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.clinica.procedimentos.domain.exception.BusinessException;
import com.clinica.procedimentos.domain.exception.EntityNotFoundException;

import lombok.extern.log4j.Log4j2;


@Log4j2
@RestControllerAdvice
public class GlobalExeption extends ResponseEntityExceptionHandler {

	String timestamp = LocalDateTime.now().toString();
	
	@Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {

            ProblemDetail problem = ProblemDetail.forStatus(HttpStatusCode.valueOf(400));
            problem.setTitle("Error validating the fields entered");
            problem.setDetail("One or more fields are invalid. Fill in correctly and try again");
            problem.setProperty("timestamp", Instant.now());

            Map<String, String> errors = getErrorFields(ex);
            errors.forEach((fieldName, message) -> {
                problem.setProperty(fieldName, message);
            });
            
            log.error("[{}] - [GlobalExeption] - MethodArgumentNotValidException: Error validating the fields entered", timestamp);
            errors.forEach((fieldName, message) -> {
                log.error("[{}] - [GlobalExeption] - Invalid field: {} - Message: {}", timestamp, fieldName, message);
            });
            
          return new ResponseEntity<Object>(problem, status);
      }

	private Map<String, String> getErrorFields(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {

		  String fieldName = ((FieldError) error).getField();
		  String message = error.getDefaultMessage();
		  errors.put(fieldName, message);
		});
		return errors;
	}
	
	@ExceptionHandler(EntityNotFoundException.class)
	ProblemDetail handlerEntityNotFoundException(EntityNotFoundException e) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
		
		problem.setTitle("Unregistered procedure");
		problem.setProperty("timestamp", Instant.now());
		
		log.error("[{}] - [GlobalExeption] - EntityNotFoundException: {}", timestamp, e.getMessage());
		return problem;
				
	}
	
	@ExceptionHandler(BusinessException.class)
	ProblemDetail handlerBusinessException(BusinessException e) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
		
		problem.setTitle("Violation of business rules");
		problem.setProperty("timestamp", Instant.now());
		
		log.error("[{}] - [GlobalExeption] - BusinessException: {}", timestamp, e.getMessage());
		return problem;
		
		
	}
}
