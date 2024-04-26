package com.clinica.cadastro.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.clinica.cadastro.domain.dto.input.MedicalAppointmentDTOInput;
import com.clinica.cadastro.domain.dto.patientEvolutionService.PatientEvolutionDto;
import com.clinica.cadastro.domain.model.MedicalAppointment;

public interface MedicalAppointmentService {

	public Page<MedicalAppointment> findAll(Pageable pageable);
	
	public Page<MedicalAppointment> findCreated(Pageable pageable);
	
	public Page<MedicalAppointment> findFinished(Pageable pageable);
	
	public Page<MedicalAppointment> findCancel(Pageable pageable);
	
	public MedicalAppointment findAppointmentById(Long appointmentId);
	
	public void deleteAppointmentById(Long appointmentId);
	
	public MedicalAppointment createAppointment(MedicalAppointmentDTOInput appointmentDto );
	
	public PatientEvolutionDto evolutionDtoBuilder(MedicalAppointment appointment);
	
	public MedicalAppointment cancelAppointment(Long appointmentId);
	
	public MedicalAppointment finishAppointment(Long appointmentId);
}