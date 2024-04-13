package com.clinica.pacientes.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.clinica.pacientes.domain.model.Patient;

public interface PatientService {

	public Patient findById(Long patientId);
	
	public Page<Patient> findAll(Pageable pageable);
	
	public void deletePatientById(Long patientId);	
	
	public Patient checkInformationAndSave(Patient patient);
}
