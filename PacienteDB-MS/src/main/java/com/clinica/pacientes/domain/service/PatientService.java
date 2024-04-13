package com.clinica.pacientes.domain.service;

import java.util.List;

import com.clinica.pacientes.domain.model.Patient;

public interface PatientService {

	public Patient findById(Long patientId);
	
	public List<Patient> findAll();
	
	public void deletePatientById(Long patientId);	
	
	Patient checkInformationAndSave(Patient patient);
}
