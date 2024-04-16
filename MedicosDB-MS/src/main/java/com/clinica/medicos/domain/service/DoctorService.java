package com.clinica.medicos.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.clinica.medicos.domain.model.Doctor;

public interface DoctorService {

	public Doctor findById(Long doctorId);
	
	public Page<Doctor> findAll(Pageable pageable);
	
	public Doctor saveDoctor(Doctor doctor);
	
	public Doctor checkInformationAndSave(Doctor doctor);
		
	public void deleteDoctorById(Long doctorId);
}
