package com.clinica.pacientes.domain.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clinica.pacientes.domain.exception.EntityNotFoundException;
import com.clinica.pacientes.domain.model.Patient;
import com.clinica.pacientes.domain.repository.PatientRepository;
import com.clinica.pacientes.domain.service.PatientService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

	final private PatientRepository repository;
	
	public Patient findById(Long patientId) {
		return repository.findById(patientId)
				.orElseThrow(() -> new EntityNotFoundException(
						String.format("Paciente de id %s não foi encontrado", patientId)));
	}
	
	public List<Patient> findAll(){
		return repository.findAll();
	}
	
	@Transactional
	public Patient savePatient(Patient patient) {
		return repository.save(patient);
	}
		
	@Transactional
	public void deletePatientById(Long patientId) {
		findById(patientId);
		repository.deleteById(patientId);
		repository.flush();
	}				
}
