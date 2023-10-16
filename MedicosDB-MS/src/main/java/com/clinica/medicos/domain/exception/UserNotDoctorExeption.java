package com.clinica.medicos.domain.exception;

public class UserNotDoctorExeption extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UserNotDoctorExeption(String message) {
		super(message);
	}
	

}
