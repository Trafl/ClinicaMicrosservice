package com.clinica.medicos.domain.service.impl;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	
	public Doctor findById(Long doctorId) {
		log.info("[DoctorServiceImpl] executando metodo findById() com id= " + doctorId);
		return repository.findById(doctorId)
				.orElseThrow(() -> new EntityNotFoundException(
						String.format("Medico de id %s não foi encontrado", doctorId)));
	}
	
	public Page<Doctor> findAll(Pageable pageable){
		log.info("[DoctorServiceImpl] executando metodo findAll()");
		return repository.findAll(pageable);
	}
	
	@Transactional
	public Doctor saveDoctor(Doctor doctor) {
		log.info("[DoctorServiceImpl] executando metodo saveDoctor()");
		try {
			return repository.save(doctor);
		} catch (DataIntegrityViolationException e) {
			log.info("[DoctorServiceImpl] InformationInUseException numero de CRM já cadastrado");
			throw new InformationInUseException("CRM: " + doctor.getCrm() + " já esta cadastrado no sistema");
		}
	}
		
	public Doctor checkInformationAndSave(Doctor doctor) {
		log.info("[DoctorServiceImpl] executando metodo checkInformationAndSave()");

		var isCrmExist = repository.existsByCrm(doctor.getCrm());

		if(isCrmExist) {
			log.info("[DoctorServiceImpl] InformationInUseException numero de CRM já cadastrado");
			throw new InformationInUseException("CRM: " + doctor.getCrm() + " já esta cadastrado no sistema");
		}
		log.info("[DoctorServiceImpl] checkInformationAndSave(), Numero de CRM checado");
		
		return saveDoctor(doctor);
	}
	
	@Transactional
	public void deleteDoctorById(Long doctorId) {
		log.info("[DoctorServiceImpl] executando metodo deleteDoctorById() com id= " + doctorId);
		findById(doctorId);
		repository.deleteById(doctorId);
		repository.flush();
	}
}
