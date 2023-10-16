package com.clinica.cadastro.controller.api;

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

import com.clinica.cadastro.controller.modelMapper.ProcedureMapper;
import com.clinica.cadastro.domain.dto.input.ProcedureDTOInput;
import com.clinica.cadastro.domain.dto.output.ProcedureDTOOutput;
import com.clinica.cadastro.domain.model.Procedure;
import com.clinica.cadastro.domain.service.ProcedureService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/procedimentos")
@RequiredArgsConstructor
public class ProcedureController {

	final private ProcedureService procedureService;
	
	final private ProcedureMapper procedureMapper;
	
	@GetMapping
	public ResponseEntity<List<ProcedureDTOOutput>> findAllProcedures(){
		List<Procedure> procedureList = procedureService.findAll();
		List<ProcedureDTOOutput> procedureDtoList = procedureMapper.toDTOCollection(procedureList);
		
		return ResponseEntity.ok(procedureDtoList);
	}
	
	@GetMapping("/{procedureId}")
	public ResponseEntity<ProcedureDTOOutput> findProcedureById(@PathVariable Long procedureId){
		Procedure procedure = procedureService.findById(procedureId);
		ProcedureDTOOutput procedureDto = procedureMapper.toDTO(procedure);
		
		return ResponseEntity.ok(procedureDto);
	}
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public ResponseEntity<ProcedureDTOOutput> createProcedure(@RequestBody @Valid ProcedureDTOInput dtoInput){
		Procedure procedure = procedureMapper.toEntity(dtoInput);
		procedure = procedureService.createProcedure(procedure);

		ProcedureDTOOutput ProcedureDto = procedureMapper.toDTO(procedure);
		return ResponseEntity.ok().body(ProcedureDto);
	}
	
	@PutMapping("/{procedureId}")
	public ResponseEntity<ProcedureDTOOutput> updateProcedure(@PathVariable Long procedureId, @RequestBody @Valid ProcedureDTOInput dtoInput){
		Procedure procedureInDb = procedureService.findById(procedureId);
		procedureMapper.copyToDomain(dtoInput, procedureInDb);
		
		procedureInDb = procedureService.createProcedure(procedureInDb);
		ProcedureDTOOutput ProcedureDto = procedureMapper.toDTO(procedureInDb);
		
		
		return ResponseEntity.ok(ProcedureDto);
	}
	
	@DeleteMapping("/{procedureId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void deleteProcedureById(@PathVariable Long procedureId){
		procedureService.deleteProcedureById(procedureId);
	}
	
	
}
