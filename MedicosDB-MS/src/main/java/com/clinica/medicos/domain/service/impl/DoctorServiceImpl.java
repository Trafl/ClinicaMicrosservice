package com.clinica.medicos.domain.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.clinica.medicos.domain.exception.EntityNotFoundException;
import com.clinica.medicos.domain.exception.InformationInUseException;
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
	
	String timestamp = LocalDateTime.now().toString();
	
	public Doctor findById(Long doctorId) {
		
		log.info("[{}] - [DoctorServiceImpl] - Executing method findById() with Id: {}", timestamp, doctorId);
		
		var doctorInDb = repository.findById(doctorId)
				.orElseThrow(() -> new EntityNotFoundException(
						String.format("Doctor with id %s was not found", doctorId)));
		
		log.info("[{}] - [DoctorServiceImpl] - Doctor with Id: {} and CRM: {} was found", timestamp, doctorInDb.getId(), doctorInDb.getCrm());
		return doctorInDb;
	}
	
	public List<Doctor> findAll(){
		var listOfDoctor = repository.findAll();
		log.info("[{}] - [DoctorServiceImpl] Executing method()", timestamp);
		return listOfDoctor;
	}
	
	@Transactional
	public Doctor saveDoctor(Doctor doctor) {
        log.info("[{}] - [DoctorServiceImpl] -  Executing method saveDoctor() for CRM: {}", timestamp, doctor.getCrm());
        
        try {
        	var savedDoctor = repository.save(doctor);
			log.info("[{}] - [DoctorServiceImpl] - Doctor successfully saved with ID: {}", timestamp, savedDoctor.getId());
			return savedDoctor;
			
		} catch (DataIntegrityViolationException e) {
			log.error("[{}] - [DoctorServiceImpl] - InformationInUseException: CRM {} is already registered", timestamp, doctor.getCrm());
			throw new InformationInUseException("CRM: " + doctor.getCrm() + " já esta cadastrado no sistema");
		}
	}
		
	public Doctor checkInformationAndSave(Doctor doctor) {
		log.info("[{}] - [DoctorServiceImpl] Executing method checkInformationAndSave() Doctor : {} ", timestamp, doctor);

		var isCrmExist = repository.existsByCrm(doctor.getCrm());

		if(isCrmExist) {
			log.info("[{}] - [DoctorServiceImpl] - InformationInUseException CRM: {} is already registered", timestamp, doctor.getCrm());
			throw new InformationInUseException("CRM: " + doctor.getCrm() + " is already registered in the system");
		}
		log.info("[{}] - [DoctorServiceImpl] checkInformationAndSave(), CRM number: {} not registered", timestamp, doctor.getCrm());
		
		return saveDoctor(doctor);
	}
	
	@Transactional
	public void deleteDoctorById(Long doctorId) {
		log.info("[{}] - [DoctorServiceImpl] deleteDoctorById() Doctor from id: {} ", timestamp, doctorId);
		findById(doctorId);
		repository.deleteById(doctorId);
		repository.flush();
		log.info("[{}] - [DoctorServiceImpl] Doctor from id: {} was deleted", timestamp, doctorId);
	}
}
