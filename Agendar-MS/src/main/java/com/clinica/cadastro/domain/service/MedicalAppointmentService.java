package com.clinica.cadastro.domain.service;

import java.util.List;

import com.clinica.cadastro.domain.dto.input.MedicalAppointmentDTOInput;
import com.clinica.cadastro.domain.dto.patientEvolutionService.PatientEvolutionDto;
import com.clinica.cadastro.domain.model.MedicalAppointment;

public interface MedicalAppointmentService {

	public List<MedicalAppointment> findAll();
	
	public List<MedicalAppointment> findFinished();
	
	public List<MedicalAppointment> findCancel();
	
	public MedicalAppointment findAppointmentById(Long appointmentId);
	
	public void deleteUserById(Long appointmentId);
	
	public MedicalAppointment createAppointment(MedicalAppointmentDTOInput appointmentDto );
	
	public PatientEvolutionDto evolutionDtoBuilder(MedicalAppointment appointment);
	
	public MedicalAppointment cancelAppointment(Long appointmentId);
	
	public MedicalAppointment finishAppointment(Long appointmentId);
}