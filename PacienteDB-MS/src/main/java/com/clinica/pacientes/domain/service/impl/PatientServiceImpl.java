package com.clinica.pacientes.domain.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clinica.pacientes.domain.exception.EntityNotFoundException;
import com.clinica.pacientes.domain.model.Patient;
import com.clinica.pacientes.domain.repository.PatientRepository;
import com.clinica.pacientes.domain.service.PatientService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

	final private PatientRepository repository;
	
	public Patient findById(Long patientId) {
		log.info("[PatientServiceImpl] executando metodo findById() com id= " + patientId);
		return repository.findById(patientId)
				.orElseThrow(() -> new EntityNotFoundException(
						String.format("Paciente de id %s não foi encontrado", patientId)));
	}
	
	public List<Patient> findAll(){
		log.info("[PatientServiceImpl] executando metodo findAll()");
		return repository.findAll();
	}
	
	@Transactional
	public Patient savePatient(Patient patient) {
		log.info("[PatientServiceImpl] executando metodo savePatient()");
		return repository.save(patient);
	}
		
	@Transactional
	public void deletePatientById(Long patientId) {
		log.info("[PatientServiceImpl] executando metodo deletePatientById() com id= " + patientId);
		findById(patientId);
		repository.deleteById(patientId);
		repository.flush();
	}				
}
