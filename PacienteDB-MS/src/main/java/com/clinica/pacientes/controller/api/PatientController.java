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

import com.clinica.pacientes.controller.api.swagger.PatientControllerSwagger;
import com.clinica.pacientes.controller.modelmapper.PatientMapper;
import com.clinica.pacientes.domain.dto.PatientDTOInput;
import com.clinica.pacientes.domain.dto.PatientDTOOutput;
import com.clinica.pacientes.domain.model.Patient;
import com.clinica.pacientes.domain.service.PatientService;

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
	
	@GetMapping
	public ResponseEntity<List<PatientDTOOutput>> findAllPatients(){
		log.info("Requisição GET feita no EndPoint '/pacientes' para consultar lista com todos o objetos Patient presentes no banco de dados");
		List<Patient> patientList = patientService.findAll();
		List<PatientDTOOutput> patientDtoList = patientMapper.toDTOCollection(patientList);
		
		return ResponseEntity.ok(patientDtoList);
	}
	
	@GetMapping("/{patientId}")
	public ResponseEntity<PatientDTOOutput> findPatientById(@PathVariable Long patientId){
		log.info("Requisição GET feita no EndPoint '/pacientes/{id}' para retornar objeto Patient de Id= " + patientId);
		Patient patient = patientService.findById(patientId);
		PatientDTOOutput patientDto = patientMapper.toDTO(patient);
		
		return ResponseEntity.ok(patientDto);
	}
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public ResponseEntity<PatientDTOOutput> createPatient(@RequestBody @Valid PatientDTOInput dtoInput){
		log.info("Requisição POST feita no EndPoint '/pacientes' para criar objeto Patient para ser persistido no bando de dados");
		Patient patient = patientMapper.toEntity(dtoInput);
		patient = patientService.savePatient(patient);

		PatientDTOOutput PatientDto = patientMapper.toDTO(patient);
		return ResponseEntity.ok().body(PatientDto);
	}
	
	@PutMapping("/{patientId}")
	public ResponseEntity<PatientDTOOutput> updatePatient(@PathVariable Long patientId, @RequestBody @Valid PatientDTOInput dtoInput){
		log.info("Requisição PUT feita no EndPoint '/pacientes/{id}' para retornar objeto Patient de Id= " + patientId);
		Patient patientInDb = patientService.findById(patientId);
		patientMapper.copyToDomain(dtoInput, patientInDb);
		
		patientInDb = patientService.savePatient(patientInDb);
		PatientDTOOutput patientDto = patientMapper.toDTO(patientInDb);
		
		
		return ResponseEntity.ok(patientDto);
	}
	
	@DeleteMapping("/{patientId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void deletePatientById(@PathVariable Long patientId){
		log.info("Requisição DELETE feita no EndPoint '/pacientes/{id}' para deletar objeto Patient de Id= " + patientId);
		patientService.deletePatientById(patientId);
	}
	
	
}
