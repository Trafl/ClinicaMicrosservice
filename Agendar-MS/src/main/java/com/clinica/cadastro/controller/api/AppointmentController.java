package com.clinica.cadastro.controller.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.clinica.cadastro.controller.api.swagger.AppointmentControllerSwagger;
import com.clinica.cadastro.controller.modelMapper.AppointmentMapper;
import com.clinica.cadastro.domain.dto.input.MedicalAppointmentDTOInput;
import com.clinica.cadastro.domain.dto.output.MedicalAppointmentOutPut;
import com.clinica.cadastro.domain.model.MedicalAppointment;
import com.clinica.cadastro.domain.service.MedicalAppointmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/consultas")
@RequiredArgsConstructor
public class AppointmentController implements AppointmentControllerSwagger {
	
	final private MedicalAppointmentService medicalAppointmentService;
	
	final private AppointmentMapper mapper;
	
	@GetMapping()
	public ResponseEntity<Page<MedicalAppointmentOutPut>> getAllAppointment(@PageableDefault(size = 10) Pageable pageable){
		log.info("Requisição GET no EndPoint '/consultas'");

		var appointments = medicalAppointmentService.findAll(pageable);
		
		var appointmentsDto = mapper.toDtoCollection(appointments.getContent());
		
		PageImpl<MedicalAppointmentOutPut> allAppointmentPage = new PageImpl<>(appointmentsDto, pageable, appointmentsDto.size());
		
		return ResponseEntity.ok(allAppointmentPage);
	}
	
	@GetMapping("/agendadas")
	public ResponseEntity<Page<MedicalAppointmentOutPut>> getScheduledAppointment(@PageableDefault(size = 10) Pageable pageable){
		log.info("Requisição GET no EndPoint '/consultas/agendadas'");
		
		var appointments = medicalAppointmentService.findCreated(pageable);
		
		var appointmentsDto = mapper.toDtoCollection(appointments.getContent());
		
		PageImpl<MedicalAppointmentOutPut> ScheduledAppointment = new PageImpl<>(appointmentsDto, pageable, appointmentsDto.size());
		
		return ResponseEntity.ok(ScheduledAppointment);
	}
	@GetMapping("/finalizadas")
	public ResponseEntity<Page<MedicalAppointmentOutPut>> getFinishedAppointment(@PageableDefault(size = 10) Pageable pageable){
		log.info("Requisição GET no EndPoint '/consultas/finalizadas'");

		var appointments = medicalAppointmentService.findFinished(pageable);
		
		var appointmentsDto = mapper.toDtoCollection(appointments.getContent());
		
		PageImpl<MedicalAppointmentOutPut> finishedAppointmentPage = new PageImpl<>(appointmentsDto, pageable, appointmentsDto.size());
		
		return ResponseEntity.ok(finishedAppointmentPage);
	}
	
	@GetMapping("/canceladas")
	public ResponseEntity<Page<MedicalAppointmentOutPut>> getCancelAppointment(@PageableDefault(size = 10) Pageable pageable){
		log.info("Requisição GET no EndPoint '/consultas/canceladas'");

		var appointments = medicalAppointmentService.findCancel(pageable);
		
		var appointmentsDto = mapper.toDtoCollection(appointments.getContent());
		
		PageImpl<MedicalAppointmentOutPut> canceledAppointmentPage = new PageImpl<>(appointmentsDto, pageable, appointmentsDto.size());
		
		return ResponseEntity.ok(canceledAppointmentPage);
	}
	
	
	@GetMapping("/{appointmentId}")
	public ResponseEntity<MedicalAppointmentOutPut> getAppointmentById(@PathVariable Long appointmentId){
		log.info("Requisição GET no EndPoint '/consultas/" + appointmentId );

		var appointment = medicalAppointmentService.findAppointmentById(appointmentId);
		var appointmentDto = mapper.toDTO(appointment);
		
		return ResponseEntity.ok(appointmentDto);
	}
	
	@PostMapping
	public ResponseEntity<MedicalAppointmentOutPut> createAppointment(@Valid @RequestBody MedicalAppointmentDTOInput appointmentDto){
		log.info("Requisição POST no EndPoint '/consultas'");
		
		MedicalAppointment appointment =  medicalAppointmentService.createAppointment(appointmentDto);
		MedicalAppointmentOutPut appointmentOut = mapper.toDTO(appointment);
		
		return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(appointmentOut);
	}
	
	@PutMapping("/{appointmentId}/cancelar")
	@ResponseStatus(code = HttpStatus.OK)
	public void  cancelAppointment(@PathVariable Long appointmentId ){
		log.info("Requisição PUT no EndPoint '/consultas/" + appointmentId + "/cancelar'");
		medicalAppointmentService.cancelAppointment(appointmentId);
	}
	
	@PutMapping("/{appointmentId}/finalizar")
	@ResponseStatus(code = HttpStatus.OK)
	public void finishAppointment(@PathVariable Long appointmentId ){
		log.info("Requisição PUT no EndPoint '/consultas/" + appointmentId + "/finalizar'");
		medicalAppointmentService.finishAppointment(appointmentId);
	}
}
