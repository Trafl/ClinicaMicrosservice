package com.clinica.pacientes.domain.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clinica.pacientes.domain.exception.InformationInUseException;
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
		
		checkInformation(patient);
		
		return repository.save(patient);
	}
	
	void checkInformation(Patient patient) {
		
		var list = repository.findAll();
		
		log.info("[PatientServiceImpl] executando metodo checkInformation()");
		
		boolean emailInUse = list.stream().anyMatch(patientInDB -> patientInDB.getEmail().equals(patient.getEmail()));
		boolean phoneInUse = list.stream().anyMatch(patientInDB -> patientInDB.getPhone().equals(patient.getPhone()));
		
		if (emailInUse) {
			log.info("[PatientServiceImpl] InformationInUseException email : " + patient.getEmail());
			throw new InformationInUseException("Paciente já existe com este email: " + patient.getEmail());
		}
		
		if (phoneInUse) {
			log.info("[PatientServiceImpl] InformationInUseException telefone : " + patient.getPhone());
			throw new InformationInUseException("Paciente já existe com este numero de telefone: " + patient.getPhone());
		}
		
		log.info("[PatientServiceImpl] O metodo checkInformation() não lançou exception");
	}

		
	@Transactional
	public void deletePatientById(Long patientId) {
		log.info("[PatientServiceImpl] executando metodo deletePatientById() com id= " + patientId);
		findById(patientId);
		repository.deleteById(patientId);
		repository.flush();
	}				
}
