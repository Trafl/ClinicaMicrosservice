package com.clinica.medicos.controller.api;

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

import com.clinica.medicos.controller.api.swagger.DoctorControllerSwagger;
import com.clinica.medicos.controller.modelmapper.DoctorMapper;
import com.clinica.medicos.domain.dto.DoctorDTOInput;
import com.clinica.medicos.domain.dto.DoctorDTOOutput;
import com.clinica.medicos.domain.model.Doctor;
import com.clinica.medicos.domain.service.DoctorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/medicos")
@RequiredArgsConstructor
public class DoctorController implements DoctorControllerSwagger {

	final private DoctorService doctorService;
	
	final private DoctorMapper doctorMapper;
	
	@GetMapping
	public ResponseEntity<List<DoctorDTOOutput>> findAllDoctors(){
		List<Doctor> doctorList = doctorService.findAll();
		List<DoctorDTOOutput> doctorDtoList = doctorMapper.toDTOCollection(doctorList);
		
		return ResponseEntity.ok(doctorDtoList);
	}
	
	@GetMapping("/{doctorId}")
	public ResponseEntity<DoctorDTOOutput> findDoctorById(@PathVariable Long doctorId){
		Doctor doctor = doctorService.findById(doctorId);
		DoctorDTOOutput doctorDto = doctorMapper.toDTO(doctor);
		
		return ResponseEntity.ok(doctorDto);
	}
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public ResponseEntity<DoctorDTOOutput> createDoctor(@RequestBody @Valid DoctorDTOInput dtoInput){
		Doctor doctor = doctorMapper.toEntity(dtoInput);
		doctor = doctorService.saveDoctor(doctor);

		DoctorDTOOutput doctorDto = doctorMapper.toDTO(doctor);
		return ResponseEntity.ok().body(doctorDto);
	}
	
	@PutMapping("/{doctorId}")
	public ResponseEntity<DoctorDTOOutput> updateDoctor(@PathVariable Long doctorId, @RequestBody @Valid DoctorDTOInput dtoInput){
		Doctor doctorInDb = doctorService.findById(doctorId);
		doctorMapper.copyToDomain(dtoInput, doctorInDb);
		
		doctorInDb = doctorService.saveDoctor(doctorInDb);
		DoctorDTOOutput doctorDto = doctorMapper.toDTO(doctorInDb);
		
		
		return ResponseEntity.ok(doctorDto);
	}
	
	@DeleteMapping("/{doctorId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void deleteDoctorById(@PathVariable Long doctorId){
		doctorService.deleteDoctorById(doctorId);
	}
}
