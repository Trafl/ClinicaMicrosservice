package com.clinica.cadastro.controller.exceptionhandler;

import org.springframework.stereotype.Component;

import com.clinica.cadastro.domain.exception.BusinessException;
import com.clinica.cadastro.domain.exception.EntityNotFoundException;

import feign.Response;
import feign.codec.ErrorDecoder;

@Component
public class CustomErrorDecoder implements ErrorDecoder {

	@Override
	public Exception decode(String methodKey, Response response) {
		 if (response.status() == 400) {
	            return new BusinessException("Requisição inválida");
	        } else if (response.status() == 404) {
	            return new EntityNotFoundException("Entidade não encontrada");
	        }
		return null;
	       	
	}

}
