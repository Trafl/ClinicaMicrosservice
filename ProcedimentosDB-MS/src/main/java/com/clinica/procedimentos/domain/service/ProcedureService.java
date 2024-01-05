package com.clinica.procedimentos.domain.service;

import java.util.List;

import com.clinica.procedimentos.domain.model.Procedure;

public interface ProcedureService {

	public Procedure findById(Long procedureId);
	
	public List<Procedure> findAll();
	
	public Procedure createProcedure(Procedure procedure);
		
	public void deleteProcedureById(Long procedureId);
}
