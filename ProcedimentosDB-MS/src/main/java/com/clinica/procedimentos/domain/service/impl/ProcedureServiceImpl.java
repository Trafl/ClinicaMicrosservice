package com.clinica.procedimentos.domain.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clinica.procedimentos.domain.exception.EntityNotFoundException;
import com.clinica.procedimentos.domain.model.Procedure;
import com.clinica.procedimentos.domain.repository.ProcedureRepository;
import com.clinica.procedimentos.domain.service.ProcedureService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class ProcedureServiceImpl implements ProcedureService {

	final private ProcedureRepository repository;
	
	public Procedure findById(Long procedureId) {
		log.info("[ProcedureServiceImpl] executando metodo findById() com id= " + procedureId);
		return repository.findById(procedureId)
				.orElseThrow(() -> new EntityNotFoundException(
						String.format("Procedimento de id %s não foi encontrado", procedureId)));
	}
	
	public List<Procedure> findAll(){
		log.info("[ProcedureServiceImpl] executando metodo findAll()");
		return repository.findAll();
	}
	
	@Transactional
	public Procedure saveProcedure(Procedure procedure) {
		log.info("[ProcedureServiceImpl] executando metodo createProcedure()");
		return repository.save(procedure);
	}
		
	@Transactional
	public void deleteProcedureById(Long procedureId) {
		log.info("[ProcedureServiceImpl] executando metodo deleteProcedureById() com id= " + procedureId);
		findById(procedureId);
		repository.deleteById(procedureId);
		repository.flush();
	}
}
