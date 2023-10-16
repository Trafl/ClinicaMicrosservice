package com.clinica.cadastro.domain.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.clinica.cadastro.domain.exception.EntityInUseException;
import com.clinica.cadastro.domain.exception.EntityNotFoundException;
import com.clinica.cadastro.domain.model.Procedure;
import com.clinica.cadastro.domain.repository.ProcedureRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProcedureService {

	final private ProcedureRepository repository;
	
	public Procedure findById(Long procedureId) {
		return repository.findById(procedureId)
				.orElseThrow(() -> new EntityNotFoundException(
						String.format("Procedimento de id %s não foi encontrado", procedureId)));
	}
	
	public List<Procedure> findAll(){
		return repository.findAll();
	}
	
	@Transactional
	public Procedure createProcedure(Procedure procedure) {
		return repository.save(procedure);
	}
		
	@Transactional
	public void deleteProcedureById(Long procedureId) {
		try {
			repository.deleteById(procedureId);
			repository.flush();
			
		}catch(EmptyResultDataAccessException e) {
			throw new EntityNotFoundException(
					String.format("Procedimento de id %s não foi encontrado", procedureId));
		
		}catch(DataIntegrityViolationException e) {
			throw new EntityInUseException(
					String.format("Procedimento de id %s esta em uso", procedureId));	
		}
	}
}
