package com.clinica.cadastro.controller.api;

import java.util.List;

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

@RestController
@RequestMapping("/consultas")
@RequiredArgsConstructor
public class AppointmentController implements AppointmentControllerSwagger {
	
	final private MedicalAppointmentService medicalAppointmentService;
	
	final private AppointmentMapper mapper;
	
	@GetMapping()
	public ResponseEntity<List<MedicalAppointmentOutPut>> getAllAppointment(){
		var appointments = medicalAppointmentService.findAll();
		var appointmentsDto = mapper.toDtoCollection(appointments);
		return ResponseEntity.ok(appointmentsDto);
	}
	
	@GetMapping("/finalizadas")
	public ResponseEntity<List<MedicalAppointmentOutPut>> getFinishedAppointment(){
		var appointments = medicalAppointmentService.findFinished();
		var appointmentsDto = mapper.toDtoCollection(appointments);
		return ResponseEntity.ok(appointmentsDto);
	}
	
	@GetMapping("/canceladas")
	public ResponseEntity<List<MedicalAppointmentOutPut>> getCancelAppointment(){
		var appointments = medicalAppointmentService.findCancel();
		var appointmentsDto = mapper.toDtoCollection(appointments);
		return ResponseEntity.ok(appointmentsDto);
	}
	
	
	@GetMapping("/{appointmentId}")
	public ResponseEntity<MedicalAppointmentOutPut> getAppointmentById(@PathVariable Long appointmentId){
		var appointment = medicalAppointmentService.findAppointmentById(appointmentId);
		var appointmentDto = mapper.toDTO(appointment);
		return ResponseEntity.ok(appointmentDto);
	}
	
	@PostMapping
	public ResponseEntity<MedicalAppointmentOutPut> createAppointment(@Valid @RequestBody MedicalAppointmentDTOInput appointmentDto){
		MedicalAppointment appointment =  medicalAppointmentService.createAppointment(appointmentDto);
		MedicalAppointmentOutPut appointmentOut = mapper.toDTO(appointment);
		return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(appointmentOut);
	}
	
	@PutMapping("/{appointmentId}/cancelar")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void  cancelAppointment(@PathVariable Long appointmentId ){
		medicalAppointmentService.cancelAppointment(appointmentId);
	}
	
	@PutMapping("/{appointmentId}/finalizar")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void finishAppointment(@PathVariable Long appointmentId ){
		medicalAppointmentService.finishAppointment(appointmentId);
	}
}
