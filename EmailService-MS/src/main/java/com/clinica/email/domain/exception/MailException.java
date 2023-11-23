package com.clinica.email.domain.exception;
public class MailException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MailException(String mensagem) {
		super(mensagem);
	}
	
	public MailException(String mensagem, Throwable causa ) {
		super(mensagem, causa);
	}
	
}