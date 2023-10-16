package com.clinica.cadastro.domain.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.clinica.cadastro.domain.exception.EntityInUseException;
import com.clinica.cadastro.domain.exception.EntityNotFoundException;
import com.clinica.cadastro.domain.exception.UserNotDoctorExeption;
import com.clinica.cadastro.domain.model.MedicalAppointment;
import com.clinica.cadastro.domain.model.Procedure;
import com.clinica.cadastro.domain.model.User;
import com.clinica.cadastro.domain.repository.AppointmentRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicalAppointmentService {

	private final ProcedureService procedureService;
	
	private final UserService userService;
	
	private final AppointmentRepository appointmentRepository;
	
	public List<MedicalAppointment> findAll() {
		return appointmentRepository.findAll();
	}
	
	public MedicalAppointment findAppointmentById(Long appointmentId) {
		return appointmentRepository.findById(appointmentId)
				.orElseThrow(() -> new EntityNotFoundException(
						String.format("Consulta de id %s não foi encontrado", appointmentId)));
	}
	
	public MedicalAppointment createAppointment(MedicalAppointment appointment) {
		User doctor = userService.findById(appointment.getDoctor().getId());
		User pacient = userService.findById(appointment.getPatient().getId());
		Procedure procedure = procedureService.findById(appointment.getProcedure().getId());
		
		if(!doctor.isDoctor()) {
			throw new UserNotDoctorExeption(
					String.format("Usuario de Id %s não esta registrado como medico.", doctor.getId()));
		}
		
		appointment.setDoctor(doctor);
		appointment.setPatient(pacient);
		appointment.setProcedure(procedure);
		
		return appointmentRepository.save(appointment);
	}
	
	@Transactional
	public void deleteUserById(Long appointmentId) {
		try {
			appointmentRepository.deleteById(appointmentId);
			appointmentRepository.flush();
			
		}catch(EmptyResultDataAccessException e) {
			throw new EntityNotFoundException(
					String.format("Consulta de id %s não foi encontrado.", appointmentId));
		
		}catch(DataIntegrityViolationException e) {
			throw new EntityInUseException(
					String.format("Consulta de id %s esta em uso.", appointmentId));
		}
	}
	
}
