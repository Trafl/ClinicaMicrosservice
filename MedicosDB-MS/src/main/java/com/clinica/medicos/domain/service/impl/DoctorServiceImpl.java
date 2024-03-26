package com.clinica.medicos.domain.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clinica.medicos.domain.exception.EntityNotFoundException;
import com.clinica.medicos.domain.model.Doctor;
import com.clinica.medicos.domain.repository.DoctorRepository;
import com.clinica.medicos.domain.service.DoctorService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService{

	final private DoctorRepository repository;
	
	public Doctor findById(Long doctorId) {
		log.info("[DoctorServiceImpl] executando metodo findById() com id= " + doctorId);
		return repository.findById(doctorId)
				.orElseThrow(() -> new EntityNotFoundException(
						String.format("Medico de id %s não foi encontrado", doctorId)));
	}
	
	public List<Doctor> findAll(){
		log.info("[DoctorServiceImpl] executando metodo findAll()");
		return repository.findAll();
	}
	
	@Transactional
	public Doctor saveDoctor(Doctor doctor) {
		log.info("[DoctorServiceImpl] executando metodo saveDoctor()");
		return repository.save(doctor);
	}
		
	@Transactional
	public void deleteDoctorById(Long doctorId) {
		log.info("[DoctorServiceImpl] executando metodo deleteDoctorById() com id= " + doctorId);
		findById(doctorId);
		repository.deleteById(doctorId);
		repository.flush();
	}
}