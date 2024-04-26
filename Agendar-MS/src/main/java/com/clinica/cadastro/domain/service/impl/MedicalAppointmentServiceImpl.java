package com.clinica.cadastro.domain.service.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class MedicalAppointmentServiceImpl implements MedicalAppointmentService {
	
	private final ProcedureService procedureService;
	
	private final PatientService patientService;
	
	private final DoctorService doctorService;
	
	private final KafkaProducerService kafkaProducerService;
	
	private final AppointmentRepository appointmentRepository;
		
	public Page<MedicalAppointment> findAll(Pageable pageable) {
		log.info("Executando findAll() em MedicalAppointmentServiceImpl");
		return appointmentRepository.findAll(pageable);
	}
	
	public Page<MedicalAppointment> findCreated(Pageable pageable) {
		log.info("Executando findCreated() em MedicalAppointmentServiceImpl");
		Page<MedicalAppointment> finalized = appointmentRepository.findByStatus(AppointmentStatus.CREATED, pageable);
		
		return finalized;
	}
	
	public Page<MedicalAppointment> findFinished(Pageable pageable) {
		log.info("Executando findFinished() em MedicalAppointmentServiceImpl");
		Page<MedicalAppointment> finalized = appointmentRepository.findByStatus(AppointmentStatus.FINISHED, pageable);
		
		return finalized;
	}
	
	public Page<MedicalAppointment> findCancel(Pageable pageable) {
		log.info("Executando findCancel() em MedicalAppointmentServiceImpl");
		Page<MedicalAppointment> canceled = appointmentRepository.findByStatus(AppointmentStatus.CANCELED, pageable);

		return canceled;
	}
	
	public MedicalAppointment findAppointmentById(Long appointmentId) {
		log.info("Executando findAppointmentById() em MedicalAppointmentServiceImpl id=" + appointmentId);
		return appointmentRepository.findById(appointmentId)
				.orElseThrow(() -> new EntityNotFoundException(
						String.format("Consulta de id %s não foi encontrado", appointmentId)));
	}
	
	@Transactional
	public void deleteAppointmentById(Long appointmentId) {
		try {
			log.info("Executando deleteAppointmentById() na class MedicalAppointmentServiceImpl id= " + appointmentId);
			appointmentRepository.deleteById(appointmentId);
			appointmentRepository.flush();
			
		}catch(EmptyResultDataAccessException e) {
			log.info("Falha ao executar deleteAppointmentById() em objeto de id= " + appointmentId + " não foi encontrado");
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
		
		
		log.info("Executando createAppointment() na class MedicalAppointmentServiceImpl");
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
		
		log.info("Executando extractData() na class MedicalAppointmentServiceImpl");
		return appointment;
	}
	
	private Doctor extractDoctor(Long doctorIdref) {
		DoctorFeign doctorF = doctorService.findDoctorById(doctorIdref);
		
		Doctor doctor = new Doctor();
		doctor.setDoctor_id(doctorF.getId());
		doctor.setDoctor_name(doctorF.getName());
		doctor.setDoctor_specialty(doctorF.getSpecialty());
		
		log.info("Executando extractDoctor() na class MedicalAppointmentServiceImpl");
		return doctor;
	}
	
	private Patient extractPatient(Long patientIdRef) {
		PatienteFeign patientF = patientService.getPatient(patientIdRef);
		
		Patient patient = new Patient();
		patient.setPatient_id(patientF.getId());
		patient.setPatient_name(patientF.getName());
		patient.setPatient_email(patientF.getEmail());
		patient.setPatient_phone(patientF.getPhone());
		
		log.info("Executando extractPatient() na class MedicalAppointmentServiceImpl");
		return patient;
	}
	
	private Procedure extractProcedure(Long procedureIdRef) {
		ProcedureFeign procedureF = procedureService.getPatient(procedureIdRef);
		
		Procedure procedure = new Procedure();
		procedure.setProcedure_id(procedureF.getId());
		procedure.setProcedure_name(procedureF.getName());
		procedure.setProcedure_value(procedureF.getValue());	
		
		
		log.info("Executando extractProcedure() na class MedicalAppointmentServiceImpl");
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
		log.info("Enviando informações para o serviço de Evolução pela Kafka, objto de id= " + evolutuion.getId());
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
		log.info("Enviando informações para o serviço de Email pelo Kafka, email do paciente = " + email.getPatient_email()
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
		log.info("Executando cancelAppointment() na class MedicalAppointmentServiceImpl");
	
		var appointment = findAppointmentById(appointmentId);
		appointment.canceledAppointment();
		
		return appointment;
				
	}
	
	@Transactional
	public MedicalAppointment finishAppointment(Long appointmentId) {
		log.info("Executando finishAppointment() na class MedicalAppointmentServiceImpl");

		var appointment = findAppointmentById(appointmentId);
		appointment.finishAppointment();
		
//		var DtoFinancial = appointmentMapper.toDTOFinancial(appointment);
		
		return appointment;
				
	}
}