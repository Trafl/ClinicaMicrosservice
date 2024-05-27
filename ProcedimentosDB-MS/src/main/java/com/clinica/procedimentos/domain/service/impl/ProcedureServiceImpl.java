package com.clinica.procedimentos.domain.service.impl;

import java.time.LocalDateTime;
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
	String timestamp = LocalDateTime.now().toString();
	
	public Procedure findById(Long procedureId) {
		log.info("[{}] - [ProcedureServiceImpl] Executing method findById with Procedure id: {}", timestamp, procedureId);
		return repository.findById(procedureId)
				.orElseThrow(() -> new EntityNotFoundException(
						String.format("Procedure id %s was not found", procedureId)));
	}
	
	public List<Procedure> findAll(){
		log.info("[{}] - [ProcedureServiceImpl] Executing method findAll ", timestamp);
		return repository.findAll();
	}
	
	@Transactional
	public Procedure saveProcedure(Procedure procedure) {
		log.info("[{}] - [ProcedureServiceImpl] Executing method saveProcedure with Procedure: {}", timestamp, procedure);
		return repository.save(procedure);
	}
		
	@Transactional
	public void deleteProcedureById(Long procedureId) {
		log.info("[{}] - [ProcedureServiceImpl] Executing method deleteProcedureById with Procedure id: {}", timestamp, procedureId);
		findById(procedureId);
		repository.deleteById(procedureId);
		repository.flush();
		log.info("[{}] - [ProcedureServiceImpl] Procedure with id {} was deleted", timestamp, procedureId);
	}
}
