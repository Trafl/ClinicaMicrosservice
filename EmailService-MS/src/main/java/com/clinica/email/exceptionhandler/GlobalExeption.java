package com.clinica.email.exceptionhandler;

import java.net.URI;
import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.clinica.email.domain.exception.MailException;

@RestControllerAdvice
public class GlobalExeption extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(MailException.class)
	ProblemDetail handlerhttpMessageNotReadableException(MailException e) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
		
		problem.setTitle("Não foi possivel enviar o Email");
		problem.setType(URI.create("https://clinicas.com/errors/bad-request"));
		problem.setProperty("timestamp", Instant.now());
		return problem;
				
	}
	
}
