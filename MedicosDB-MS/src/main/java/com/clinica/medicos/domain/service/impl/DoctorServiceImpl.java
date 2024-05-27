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
		
		log.info("[{}] - [DoctorServiceImpl] - Executando método findById() para o Id: {}", timestamp, doctorId);
		
		var doctorInDb = repository.findById(doctorId)
				.orElseThrow(() -> new EntityNotFoundException(
						String.format("Doutor de id %s não foi encontrado", doctorId)));
		
		log.info("[{}] - [DoctorServiceImpl] - Doutor com Id: {} e CRM: {} foi encontrado", timestamp, doctorInDb.getId(), doctorInDb.getCrm());
		return doctorInDb;
	}
	
	public List<Doctor> findAll(){
		var listOfDoctor = repository.findAll();
		log.info("[{}] - [DoctorServiceImpl] executando findAll()", timestamp);
		return listOfDoctor;
	}
	
	@Transactional
	public Doctor saveDoctor(Doctor doctor) {
        log.info("[{}] - [DoctorServiceImpl] - Executando método saveDoctor() para o CRM: {}", timestamp, doctor.getCrm());
        
        try {
        	var savedDoctor = repository.save(doctor);
			log.info("[{}] - [DoctorServiceImpl] - Doutor salvo com sucesso com ID: {}", timestamp, savedDoctor.getId());
			return savedDoctor;
			
		} catch (DataIntegrityViolationException e) {
			log.error("[{}] - [DoctorServiceImpl] - InformationInUseException: CRM {} já está cadastrado", timestamp, doctor.getCrm());
			throw new InformationInUseException("CRM: " + doctor.getCrm() + " já esta cadastrado no sistema");
		}
	}
		
	public Doctor checkInformationAndSave(Doctor doctor) {
		log.info("[{}] - [DoctorServiceImpl] executando metodo checkInformationAndSave() Doutor : {} ", timestamp, doctor);

		var isCrmExist = repository.existsByCrm(doctor.getCrm());

		if(isCrmExist) {
			log.info("[{}] - [DoctorServiceImpl] - InformationInUseException CRM: {} já cadastrado", timestamp, doctor.getCrm());
			throw new InformationInUseException("CRM: " + doctor.getCrm() + " já esta cadastrado no sistema");
		}
		log.info("[{}] - [DoctorServiceImpl] checkInformationAndSave(), Numero de CRM: {} não cadastrado", timestamp, doctor.getCrm());
		
		return saveDoctor(doctor);
	}
	
	@Transactional
	public void deleteDoctorById(Long doctorId) {
		log.info("[{}] - [DoctorServiceImpl] deleteDoctorById() Doutor de Id: {} ", timestamp, doctorId);
		findById(doctorId);
		repository.deleteById(doctorId);
		repository.flush();
		log.info("[{}] - [DoctorServiceImpl] Doutor de Id: {} foi deletado", timestamp, doctorId);
	}
}
