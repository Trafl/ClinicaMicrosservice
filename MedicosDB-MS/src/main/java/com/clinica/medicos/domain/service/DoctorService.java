package com.clinica.medicos.domain.service;

import java.util.List;

import com.clinica.medicos.domain.model.Doctor;

public interface DoctorService {

	public Doctor findById(Long doctorId);
	
	public List<Doctor> findAll();
	
	public Doctor saveDoctor(Doctor doctor);
		
	public void deleteDoctorById(Long doctorId);
}
