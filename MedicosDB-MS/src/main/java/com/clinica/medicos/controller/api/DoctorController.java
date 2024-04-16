package com.clinica.medicos.controller.api;

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

import com.clinica.medicos.controller.api.swagger.DoctorControllerSwagger;
import com.clinica.medicos.controller.modelmapper.DoctorMapper;
import com.clinica.medicos.domain.dto.DoctorDTOInput;
import com.clinica.medicos.domain.dto.DoctorDTOOutput;
import com.clinica.medicos.domain.model.Doctor;
import com.clinica.medicos.domain.service.DoctorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/medicos")
@RequiredArgsConstructor
public class DoctorController implements DoctorControllerSwagger {

	final private DoctorService doctorService;
	
	final private DoctorMapper doctorMapper;
	
	@GetMapping
	public ResponseEntity<Page<DoctorDTOOutput>> findAllDoctors(@PageableDefault(size = 10) Pageable pageable){
		log.info("Requisição GET feita no EndPoint '/medicos' para consultar lista com todos o objetos Doctor presentes no banco de dados");
		
		Page<Doctor> doctorPage = doctorService.findAll(pageable);
		
		List<DoctorDTOOutput> doctorDtoList = doctorMapper.toDTOCollection(doctorPage.getContent());
		
		PageImpl<DoctorDTOOutput> doctorDTOPage = new PageImpl<>(doctorDtoList, pageable, doctorPage.getSize());
		
		return ResponseEntity.ok(doctorDTOPage);
	}
	
	@GetMapping("/{doctorId}")
	public ResponseEntity<DoctorDTOOutput> findDoctorById(@PathVariable Long doctorId){
		log.info("Requisição GET feita no EndPoint '/medicos/{id}' para retornar objeto Doctor de Id= " + doctorId);
		Doctor doctor = doctorService.findById(doctorId);
		DoctorDTOOutput doctorDto = doctorMapper.toDTO(doctor);
		
		return ResponseEntity.ok(doctorDto);
	}
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public ResponseEntity<DoctorDTOOutput> createDoctor(@RequestBody @Valid DoctorDTOInput dtoInput){
		log.info("Requisição POST feita no EndPoint '/medicos', para criar objeto Doctor para ser persistido no banco");
		Doctor doctor = doctorMapper.toEntity(dtoInput);
		doctor = doctorService.saveDoctor(doctor);

		DoctorDTOOutput doctorDto = doctorMapper.toDTO(doctor);
		
		return ResponseEntity.ok().body(doctorDto);
	}
	
	@PutMapping("/{doctorId}")
	public ResponseEntity<DoctorDTOOutput> updateDoctor(@PathVariable Long doctorId, @RequestBody @Valid DoctorDTOInput dtoInput){
		log.info("Requisição PUT feita no EndPoint '/medicos/{id}' para atualizar o objeto Doctor de Id= " + doctorId);
		
		Doctor doctorInDb = doctorService.findById(doctorId);
		
		doctorMapper.copyToDomain(dtoInput, doctorInDb);
		
		doctorInDb = doctorService.checkInformationAndSave(doctorInDb);
		
		DoctorDTOOutput doctorDto = doctorMapper.toDTO(doctorInDb);
		
		return ResponseEntity.ok(doctorDto);
	}
	
	@DeleteMapping("/{doctorId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void deleteDoctorById(@PathVariable Long doctorId){
		log.info("Requisição DELETE feita no EndPoint '/medicos/{id}' para deletar o objeto Doctor de Id= " + doctorId);
		doctorService.deleteDoctorById(doctorId);
		
	}
}
