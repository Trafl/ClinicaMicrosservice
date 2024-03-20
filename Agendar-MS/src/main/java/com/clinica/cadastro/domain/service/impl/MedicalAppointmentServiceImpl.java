package com.clinica.cadastro.domain.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.clinica.cadastro.controller.modelMapper.AppointmentMapper;
import com.clinica.cadastro.domain.dto.emailService.EmailDto;
import com.clinica.cadastro.domain.dto.feign.DoctorFeign;
import com.clinica.cadastro.domain.dto.feign.PatienteFeign;
import com.clinica.cadastro.domain.dto.feign.ProcedureFeign;
import com.clinica.cadastro.domain.dto.input.MedicalAppointmentDTOInput;
import com.clinica.cadastro.domain.dto.patientEvolutionService.PatientEvolutionDto;
import com.clinica.cadastro.domain.exception.EntityNotFoundException;
import com.clinica.cadastro.domain.model.AppointmentStatus;
import com.clinica.cadastro.domain.model.MedicalAppointment;
import com.clinica.cadastro.domain.model.feign.Doctor;
import com.clinica.cadastro.domain.model.feign.Patient;
import com.clinica.cadastro.domain.model.feign.Procedure;
import com.clinica.cadastro.domain.repository.AppointmentRepository;
import com.clinica.cadastro.domain.service.MedicalAppointmentService;
import com.clinica.cadastro.domain.service.feign.DoctorService;
import com.clinica.cadastro.domain.service.feign.PatientService;
import com.clinica.cadastro.domain.service.feign.ProcedureService;
import com.clinica.cadastro.domain.service.kafka.KafkaProducerService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicalAppointmentServiceImpl implements MedicalAppointmentService {
	
	private final AppointmentMapper appointmentMapper;
	
	private final ProcedureService procedureService;
	
	private final PatientService patientService;
	
	private final DoctorService doctorService;
	
	private final KafkaProducerService kafkaProducerService;
	
	private final AppointmentRepository appointmentRepository;
		
	private static final Logger logg = LoggerFactory.getLogger(MedicalAppointmentServiceImpl.class);
	
	public List<MedicalAppointment> findAll() {
		logg.info("Executando findAll() na class MedicalAppointmentServiceImpl");
		return appointmentRepository.findAll();
	}
	
	public List<MedicalAppointment> findFinished() {
		List<MedicalAppointment> appointments = appointmentRepository.findAll();
		
		List<MedicalAppointment> finalized = appointments.stream()
				.filter(appointment -> appointment.getStatus() == AppointmentStatus.FINISHED).collect(Collectors.toList());
		
		logg.info("Executando findFinished() na class MedicalAppointmentServiceImpl");
		return finalized;
	}
	
	public List<MedicalAppointment> findCancel() {
		List<MedicalAppointment> appointments = appointmentRepository.findAll();
	
		List<MedicalAppointment> canceled = appointments.stream()
				.filter(appointment -> appointment.getStatus() == AppointmentStatus.CANCELED).collect(Collectors.toList());
		
		logg.info("Executando findCancel() na class MedicalAppointmentServiceImpl");
		return canceled;
	}
	
	public MedicalAppointment findAppointmentById(Long appointmentId) {
		logg.info("Executando findAppointmentById() na class MedicalAppointmentServiceImpl id=" + appointmentId);
		return appointmentRepository.findById(appointmentId)
				.orElseThrow(() -> new EntityNotFoundException(
						String.format("Consulta de id %s não foi encontrado", appointmentId)));
	}
	
	@Transactional
	public void deleteAppointmentById(Long appointmentId) {
		try {
			logg.info("Executando deleteAppointmentById() na class MedicalAppointmentServiceImpl id= " + appointmentId);
			appointmentRepository.deleteById(appointmentId);
			appointmentRepository.flush();
			
		}catch(EmptyResultDataAccessException e) {
			logg.error("ERROR ao executar deleteAppointmentById() em objeto de id= " + appointmentId);
			throw new EntityNotFoundException(
					String.format("Consulta de id %s não foi encontrado.", appointmentId));
		 }
	
	}
	
	@Transactional
	public MedicalAppointment createAppointment(MedicalAppointmentDTOInput appointmentDto ) {
		MedicalAppointment appointment = extractData(appointmentDto);		
		
		appointment = appointmentRepository.save(appointment);
		
		kafkaSendToEvolutionService(appointment);
		
		kafkaSendToEmailService(appointment);
		
		
		logg.info("Executando createAppointment() na class MedicalAppointmentServiceImpl");
		return appointment;
	}
	
	private MedicalAppointment extractData(MedicalAppointmentDTOInput appointmentDto) {
		
		Long doctorIdref = appointmentDto.getDoctorId();
		Doctor doctor = extractDoctor(doctorIdref);
		
		Long patientIdRef = appointmentDto.getPatientId();
		Patient patient = extractPatient(patientIdRef); 
		
		Long procedureIdRef = appointmentDto.getProcedureId();
		Procedure procedure = extractProcedure(procedureIdRef); 
		
		MedicalAppointment appointment = fillData(appointmentDto, doctor, patient, procedure);
		
		logg.info("Executando extractData() na class MedicalAppointmentServiceImpl");
		return appointment;
	}
	
	private Doctor extractDoctor(Long doctorIdref) {
		DoctorFeign doctorF = doctorService.findDoctorById(doctorIdref);
		
		Doctor doctor = new Doctor();
		doctor.setDoctor_id(doctorF.getId());
		doctor.setDoctor_name(doctorF.getName());
		doctor.setDoctor_specialty(doctorF.getSpecialty());
		
		logg.info("Executando extractDoctor() na class MedicalAppointmentServiceImpl");
		return doctor;
	}
	
	private Patient extractPatient(Long patientIdRef) {
		PatienteFeign patientF = patientService.getPatient(patientIdRef);
		
		Patient patient = new Patient();
		patient.setPatient_id(patientF.getId());
		patient.setPatient_name(patientF.getName());
		patient.setPatient_email(patientF.getEmail());
		patient.setPatient_phone(patientF.getPhone());
		
		logg.info("Executando extractPatient() na class MedicalAppointmentServiceImpl");
		return patient;
	}
	
	private Procedure extractProcedure(Long procedureIdRef) {
		ProcedureFeign procedureF = procedureService.getPatient(procedureIdRef);
		
		Procedure procedure = new Procedure();
		procedure.setProcedure_id(procedureF.getId());
		procedure.setProcedure_name(procedureF.getName());
		procedure.setProcedure_value(procedureF.getValue());	
		
		
		logg.info("Executando extractProcedure() na class MedicalAppointmentServiceImpl");
		return procedure;
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
	
	private void kafkaSendToEvolutionService(MedicalAppointment appointment) {
		var evolutuion= evolutionDtoBuilder(appointment);
		logg.info("Enviando informações para o serviço de Evolução pela Kafka, objto de id= " + evolutuion.getId());
		kafkaProducerService.sendMessage("agendar-to-patient-evolution", evolutuion, appointment.getPatient().getPatient_name());
	}
	
	public PatientEvolutionDto evolutionDtoBuilder(MedicalAppointment appointment) {
		return PatientEvolutionDto.builder()
				.id(appointment.getId())
				.doctor_id(appointment.getDoctor().getDoctor_id())
				.patient_id(appointment.getPatient().getPatient_id())
				.procedure_id(appointment.getProcedure().getProcedure_id())
				.date(appointment.getDate())
				.build();
	}

	private void kafkaSendToEmailService(MedicalAppointment appointment) {
		var email= emailDtoBuilder(appointment);
		logg.info("Enviando informações para o serviço de Email pelo Kafka, email do paciente = " + email.getPatient_email()
		+ " nome do medico= " + email.getDoctor_name());
		kafkaProducerService.sendMessage("agendar-to-emailService", email, appointment.getPatient().getPatient_name());
	}

	public EmailDto emailDtoBuilder(MedicalAppointment appointment) {
		
		return EmailDto.builder()
				.doctor_name(appointment.getDoctor().getDoctor_name())
				.patient_email(appointment.getPatient().getPatient_email())
				.patient_name(appointment.getPatient().getPatient_name())
				.date(appointment.getDate())
				.build();
	}

	
	@Transactional
	public MedicalAppointment cancelAppointment(Long appointmentId) {
		var appointment = findAppointmentById(appointmentId);
		appointment.canceledAppointment();
		
		logg.info("Executando cancelAppointment() na class MedicalAppointmentServiceImpl");
		return appointment;
				
	}
	
	@Transactional
	public MedicalAppointment finishAppointment(Long appointmentId) {
		var appointment = findAppointmentById(appointmentId);
		appointment.finishAppointment();
		
//		var DtoFinancial = appointmentMapper.toDTOFinancial(appointment);
		
		logg.info("Executando finishAppointment() na class MedicalAppointmentServiceImpl");
		return appointment;
				
	}
}