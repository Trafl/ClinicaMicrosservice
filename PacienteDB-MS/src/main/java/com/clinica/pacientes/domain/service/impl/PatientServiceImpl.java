package com.clinica.pacientes.domain.service.impl;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.clinica.pacientes.domain.exception.EntityNotFoundException;
import com.clinica.pacientes.domain.exception.InformationInUseException;
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
	
	public Page<Patient> findAll(Pageable pageble){
		log.info("[PatientServiceImpl] executando metodo findAll()");
		return repository.findAll(pageble);
	}
	
	@Transactional
	public Patient savePatient(Patient patient) {
		log.info("[PatientServiceImpl] executando metodo savePatient()");
		
		try {
			 return repository.save(patient);
			 
		} catch (DataIntegrityViolationException e) {
			throw new InformationInUseException("Email ou numero de telefone já esta cadastrado no sistema");
		}
	}
	
	public Patient checkInformationAndSave(Patient patient) {
		log.info("[PatientServiceImpl] executando metodo checkInformationAndSave()");
		
		var isEmailExist = repository.existsByEmail(patient.getEmail());
		var isPhoneExist = repository.existsByPhone(patient.getPhone());
		
		if(isEmailExist) {
			log.info("[PatientServiceImpl] InformationInUseException email já cadastrado");
			throw new InformationInUseException("Email: " + patient.getEmail() + " já esta cadastrado no sistema");
		}
		log.info("[PatientServiceImpl] checkInformationAndSave(), Email checado");
		
		if(isPhoneExist) {
			log.info("[PatientServiceImpl] InformationInUseException numero de telefone já cadastrado");
			throw new InformationInUseException("Telefone: " + patient.getPhone() + " já esta cadastrado no sistema");
		}
		log.info("[PatientServiceImpl] checkInformationAndSave(), Numero de telefone checado");
		
		return savePatient(patient);
	}

		
	@Transactional
	public void deletePatientById(Long patientId) {
		log.info("[PatientServiceImpl] executando metodo deletePatientById() com id= " + patientId);
		findById(patientId);
		repository.deleteById(patientId);
		repository.flush();
	}				
}
