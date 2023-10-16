package com.clinica.pacientes.controller.api;

import java.util.List;

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

import com.clinica.pacientes.controller.modelmapper.PatientMapper;
import com.clinica.pacientes.domain.dto.PatientDTOInput;
import com.clinica.pacientes.domain.dto.PatientDTOOutput;
import com.clinica.pacientes.domain.model.Patient;
import com.clinica.pacientes.domain.service.PatientService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/pacientes")
@RequiredArgsConstructor
public class PatientController {

	final private PatientService patientService;
	
	final private PatientMapper patientMapper;
	
	@GetMapping
	public ResponseEntity<List<PatientDTOOutput>> findAllPatients(){
		List<Patient> patientList = patientService.findAll();
		List<PatientDTOOutput> patientDtoList = patientMapper.toDTOCollection(patientList);
		
		return ResponseEntity.ok(patientDtoList);
	}
	
	@GetMapping("/{patientId}")
	public ResponseEntity<PatientDTOOutput> findPatientById(@PathVariable Long patientId){
		Patient patient = patientService.findById(patientId);
		PatientDTOOutput patientDto = patientMapper.toDTO(patient);
		
		return ResponseEntity.ok(patientDto);
	}
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public ResponseEntity<PatientDTOOutput> createPatient(@RequestBody @Valid PatientDTOInput dtoInput){
		Patient patient = patientMapper.toEntity(dtoInput);
		patient = patientService.savePatient(patient);

		PatientDTOOutput PatientDto = patientMapper.toDTO(patient);
		return ResponseEntity.ok().body(PatientDto);
	}
	
	@PutMapping("/{patientId}")
	public ResponseEntity<PatientDTOOutput> updatePatient(@PathVariable Long patientId, @RequestBody @Valid PatientDTOInput dtoInput){
		Patient patientInDb = patientService.findById(patientId);
		patientMapper.copyToDomain(dtoInput, patientInDb);
		
		patientInDb = patientService.savePatient(patientInDb);
		PatientDTOOutput patientDto = patientMapper.toDTO(patientInDb);
		
		
		return ResponseEntity.ok(patientDto);
	}
	
	@DeleteMapping("/{patientId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void deletePatientById(@PathVariable Long patientId){
		patientService.deletePatientById(patientId);
	}
	
	
}
