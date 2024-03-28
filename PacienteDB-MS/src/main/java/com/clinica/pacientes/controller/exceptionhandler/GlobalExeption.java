package com.clinica.pacientes.controller.exceptionhandler;

import java.time.Instant;
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

import com.clinica.pacientes.domain.exception.BusinessException;
import com.clinica.pacientes.domain.exception.EntityNotFoundException;
import com.clinica.pacientes.domain.exception.InformationInUseException;


@RestControllerAdvice
public class GlobalExeption extends ResponseEntityExceptionHandler {

	@Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {

            ProblemDetail problem = ProblemDetail.forStatus(HttpStatusCode.valueOf(400));
            problem.setTitle("Erro na validação dos campos informados");
            problem.setDetail("Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.");
            problem.setProperty("timestamp", Instant.now());

            Map<String, String> errors = getErrorFields(ex);
            errors.forEach((fieldName, message) -> {
                problem.setProperty(fieldName, message);
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
		
		problem.setTitle("Paciente não registrado");
		problem.setProperty("timestamp", Instant.now());
		return problem;
				
	}
	
	@ExceptionHandler(BusinessException.class)
	ProblemDetail handlerBusinessException(BusinessException e) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
		
		problem.setTitle("Violação de regra de negócio.");
		problem.setProperty("timestamp", Instant.now());
		return problem;
		
		
	}
	
	@ExceptionHandler(InformationInUseException.class)
	ProblemDetail handlerInformationInUseException(InformationInUseException e) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
		
		problem.setTitle("Violação de regra de negócio.");
		problem.setProperty("timestamp", Instant.now());
		return problem;
		
		
	}
}
