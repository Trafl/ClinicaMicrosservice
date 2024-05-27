package com.clinica.pacientes.controller.api;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.clinica.pacientes.controller.api.swagger.PatientControllerSwagger;
import com.clinica.pacientes.controller.modelmapper.PatientMapper;
import com.clinica.pacientes.domain.dto.PatientDTOInput;
import com.clinica.pacientes.domain.dto.PatientDTOOutput;
import com.clinica.pacientes.domain.model.Patient;
import com.clinica.pacientes.domain.service.PatientService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/pacientes")
@RequiredArgsConstructor
public class PatientController implements PatientControllerSwagger {

	final private PatientService patientService;
	
	final private PatientMapper patientMapper;
	
	String timestamp = LocalDateTime.now().toString();
	
	@GetMapping
	public ResponseEntity<Page<PatientDTOOutput>> findAllPatients(@PageableDefault Pageable pageable, HttpServletRequest request){
		log.info("[{}] - [PatientController] IP: {}, Request: GET, EndPoint: '/pacientes'", timestamp, request.getRemoteAddr());	
		
		List<Patient> patientPage = patientService.findAll();
		
		List<PatientDTOOutput> patientDtoList = patientMapper.toDTOCollection(patientPage);
		
		Page<PatientDTOOutput> patientDtoPage = new PageImpl<>(patientDtoList, pageable, patientDtoList.size());
		
		return ResponseEntity.ok(patientDtoPage);
	}
	
	@GetMapping("/{patientId}")
	public ResponseEntity<PatientDTOOutput> findPatientById(@PathVariable Long patientId, HttpServletRequest request){
		log.info("[{}] - [PatientController] IP: {}, Request: GET, EndPoint: '/pacientes/{}', Patient Id: {}", timestamp, request.getRemoteAddr(), patientId, patientId);
		Patient patient = patientService.findById(patientId);
		PatientDTOOutput patientDto = patientMapper.toDTO(patient);
		
		return ResponseEntity.ok(patientDto);
	}
	
	@PostMapping
	public ResponseEntity<PatientDTOOutput> createPatient(@RequestBody @Valid PatientDTOInput dtoInput, HttpServletRequest request){
		log.info("[{}] - [PatientController] IP: {}, Request: POST, EndPoint: '/pacientes'", timestamp, request.getRemoteAddr());
		
		Patient patient = patientMapper.toEntity(dtoInput);
		patient = patientService.checkInformationAndSave(patient);
		PatientDTOOutput PatientDto = patientMapper.toDTO(patient);
		
		return ResponseEntity.status(201).body(PatientDto);
	}
	
	@PutMapping("/{patientId}")
	public ResponseEntity<PatientDTOOutput> updatePatient(@PathVariable Long patientId, @RequestBody @Valid PatientDTOInput dtoInput, HttpServletRequest request){
		log.info("[{}] - [PatientController] IP: {}, Request: PUT, EndPoint: '/pacientes/{}', Patient Id: {}", timestamp, request.getRemoteAddr(), patientId, patientId);		
		Patient patientInDb = patientService.findById(patientId);
		
		patientMapper.copyToDomain(dtoInput, patientInDb);
		
		patientInDb = patientService.checkInformationAndSave(patientInDb);
		
		PatientDTOOutput patientDto = patientMapper.toDTO(patientInDb);
		
		return ResponseEntity.ok(patientDto);
	}
	
	@DeleteMapping("/{patientId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void deletePatientById(@PathVariable Long patientId, HttpServletRequest request){
		log.info("[{}] - [PatientController] IP: {}, Request: DELETE, EndPoint: '/pacientes/{}', Patient Id: {}", timestamp, request.getRemoteAddr(), patientId, patientId);		
		patientService.deletePatientById(patientId);
	}
	
	
}
