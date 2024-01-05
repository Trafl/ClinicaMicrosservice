package com.clinica.evolution.domain.exceptions;

public class AnalysisNotFoundException extends BusinessException {
	private static final long serialVersionUID = 1L;
	
	public AnalysisNotFoundException(String id) {
		super(String.format("Prontuario de id: %s, não foi encontrado no banco de dados.", id));
	}
}
