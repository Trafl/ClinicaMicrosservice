package com.clinica.cadastro.domain.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.clinica.cadastro.controller.modelMapper.AppointmentMapper;
import com.clinica.cadastro.domain.dto.feign.DoctorFeign;
import com.clinica.cadastro.domain.dto.feign.PatienteFeign;
import com.clinica.cadastro.domain.dto.feign.ProcedureFeign;
import com.clinica.cadastro.domain.dto.input.MedicalAppointmentDTOInput;
import com.clinica.cadastro.domain.dto.output.MedicalAppointmentDtoFinancial;
import com.clinica.cadastro.domain.exception.EntityNotFoundException;
import com.clinica.cadastro.domain.model.AppointmentStatus;
import com.clinica.cadastro.domain.model.Doctor;
import com.clinica.cadastro.domain.model.MedicalAppointment;
import com.clinica.cadastro.domain.model.Patient;
import com.clinica.cadastro.domain.model.Procedure;
import com.clinica.cadastro.domain.repository.AppointmentRepository;
import com.clinica.cadastro.domain.service.feign.DoctorService;
import com.clinica.cadastro.domain.service.feign.PatientService;
import com.clinica.cadastro.domain.service.feign.ProcedureService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicalAppointmentService {
	
	private final AppointmentMapper appointmentMapper;
	
	private final ProcedureService procedureService;
	
	private final PatientService patientService;
	
	private final DoctorService doctorService;
		
	private final KafkaTemplate<String, MedicalAppointmentDtoFinancial> kafkaTemplate;
	
	private final AppointmentRepository appointmentRepository;
	
	public List<MedicalAppointment> findAll() {
		return appointmentRepository.findAll();
	}
	
	public List<MedicalAppointment> findFinished() {
		List<MedicalAppointment> appointments = appointmentRepository.findAll();
		
		List<MedicalAppointment> finalized = appointments.stream()
				.filter(appointment -> appointment.getStatus() == AppointmentStatus.FINISHED).collect(Collectors.toList());
		
		return finalized;
	}
	
	public List<MedicalAppointment> findCancel() {
		List<MedicalAppointment> appointments = appointmentRepository.findAll();
	
		List<MedicalAppointment> canceled = appointments.stream()
				.filter(appointment -> appointment.getStatus() == AppointmentStatus.CANCELED).collect(Collectors.toList());
		
		return canceled;
	}
	
	public MedicalAppointment findAppointmentById(Long appointmentId) {
		return appointmentRepository.findById(appointmentId)
				.orElseThrow(() -> new EntityNotFoundException(
						String.format("Consulta de id %s não foi encontrado", appointmentId)));
	}
	
	@Transactional
	public void deleteUserById(Long appointmentId) {
		try {
			appointmentRepository.deleteById(appointmentId);
			appointmentRepository.flush();
			
		}catch(EmptyResultDataAccessException e) {
			throw new EntityNotFoundException(
					String.format("Consulta de id %s não foi encontrado.", appointmentId));
		 }
	
	}
	
	@Transactional
	public MedicalAppointment createAppointment(MedicalAppointmentDTOInput appointmentDto ) {
		MedicalAppointment appointment = extractData(appointmentDto);		
		
		return appointmentRepository.save(appointment);
	}

	private MedicalAppointment extractData(MedicalAppointmentDTOInput appointmentDto) {
		
		Long doctorIdref = appointmentDto.getDoctorId();
		Doctor doctor = extractDoctor(doctorIdref);
		
		Long patientIdRef = appointmentDto.getPatientId();
		Patient patient = extractPatient(patientIdRef); 
		
		Long procedureIdRef = appointmentDto.getProcedureId();
		Procedure procedure = extractProcedure(procedureIdRef); 
		
		MedicalAppointment appointment = fillData(appointmentDto, doctor, patient, procedure);
		
		return appointment;
	}

	private MedicalAppointment fillData(MedicalAppointmentDTOInput appointmentDto, Doctor doctor, Patient patient,
			Procedure procedure) {
		
		MedicalAppointment appointment = new MedicalAppointment();
		appointment.setDoctor(doctor);
		appointment.setPatient(patient);
		appointment.setProcedure(procedure);
		appointment.setDate(appointmentDto.getDate());
		return appointment;
	}

	private Doctor extractDoctor(Long doctorIdref) {
		DoctorFeign doctorF = doctorService.findDoctorById(doctorIdref);
		
		Doctor doctor = new Doctor();
		doctor.setDoctor_id(doctorF.getId());
		doctor.setDoctor_name(doctorF.getName());
		doctor.setDoctor_specialty(doctorF.getSpecialty());
		return doctor;
	}
	
	private Patient extractPatient(Long patientIdRef) {
		PatienteFeign patientF = patientService.getPatient(patientIdRef);
		
		Patient patient = new Patient();
		patient.setPatient_id(patientF.getId());
		patient.setPatient_name(patientF.getName());
		patient.setPatient_email(patientF.getEmail());
		patient.setPatient_phone(patientF.getPhone());
		
		return patient;
	}
	
	private Procedure extractProcedure(Long procedureIdRef) {
		ProcedureFeign procedureF = procedureService.getPatient(procedureIdRef);
		
		Procedure procedure = new Procedure();
		procedure.setProcedure_id(procedureF.getId());
		procedure.setProcedure_name(procedureF.getName());
		procedure.setProcedure_value(procedureF.getValue());		
		return procedure;
	}
	
	@Transactional
	public MedicalAppointment cancelAppointment(Long appointmentId) {
		var appointment = findAppointmentById(appointmentId);
		appointment.canceledAppointment();
		
		return appointment;
				
	}
	
	@Transactional
	public MedicalAppointment finishAppointment(Long appointmentId) {
		var appointment = findAppointmentById(appointmentId);
		appointment.finishAppointment();
		
		var DtoFinancial = appointmentMapper.toDTOFinancial(appointment);
		
		kafkaTemplate.send("agendamento-to-financeiro", DtoFinancial);
		
		return appointment;
				
	}
}