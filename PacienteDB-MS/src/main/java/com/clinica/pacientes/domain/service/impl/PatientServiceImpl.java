package com.clinica.pacientes.domain.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
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
	String timestamp = LocalDateTime.now().toString();
	
	public Patient findById(Long patientId) {
		log.info("[{}] - [PatientServiceImpl] Executing method findById with Patient id: {}", timestamp, patientId);
		return repository.findById(patientId)
				.orElseThrow(() -> new EntityNotFoundException(
						String.format("Patient with id %s was not found", patientId)));
	}
	
	public List<Patient> findAll(){
		log.info("[{}] - [PatientServiceImpl] Executing method findById", timestamp);
		return repository.findAll();
	}
	
	@Transactional
	public Patient savePatient(Patient patient) {
		log.info("[{}] - [PatientServiceImpl] Executing method savePatient with Patient: {}", timestamp, patient);
		
		try {
			 return repository.save(patient);
			 
		} catch (DataIntegrityViolationException e) {
			log.error("[{}] - [PatientServiceImpl] InformationInUseException Email or phone number is already registered in the system", timestamp);
			throw new InformationInUseException("Email or phone number is already registered in the system");
		}
	}
	
	public Patient checkInformationAndSave(Patient patient) {
		log.info("[{}] - [PatientServiceImpl] Executing method checkInformationAndSave with Patient: {}", timestamp, patient);
		
		var isEmailExist = repository.existsByEmail(patient.getEmail());
		var isPhoneExist = repository.existsByPhone(patient.getPhone());
		
		if(isEmailExist) {
			log.info("[{}] - [PatientServiceImpl] InformationInUseException email: {} already registered", timestamp, patient.getEmail());
			throw new InformationInUseException("Email: " + patient.getEmail() + " is already registered in the system");
		}
		
		if(isPhoneExist) {
			log.info("[{}] - [PatientServiceImpl] InformationInUseException phone number: {} already registered", timestamp, patient.getPhone());
			throw new InformationInUseException("Phone number: " + patient.getPhone() + " is already registered in the system");
		}
		return savePatient(patient);
	}

	@Transactional
	public void deletePatientById(Long patientId) {
		log.info("[{}] - [PatientServiceImpl]Executing method deletePatientById with Patient id: {}", timestamp, patientId);
		findById(patientId);
		repository.deleteById(patientId);
		repository.flush();
		log.info("[{}] - [PatientServiceImpl]Patient with id {} was deleted", timestamp, patientId);
	}				
}
