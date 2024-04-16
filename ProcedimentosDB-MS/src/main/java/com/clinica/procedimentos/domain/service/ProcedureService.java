package com.clinica.procedimentos.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.clinica.procedimentos.domain.model.Procedure;

public interface ProcedureService {

	public Procedure findById(Long procedureId);
	
	public Page<Procedure> findAll(Pageable pageable);
	
	public Procedure saveProcedure(Procedure procedure);
		
	public void deleteProcedureById(Long procedureId);
}
