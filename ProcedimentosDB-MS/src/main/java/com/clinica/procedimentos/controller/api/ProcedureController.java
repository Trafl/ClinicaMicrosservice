package com.clinica.procedimentos.controller.api;

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

import com.clinica.procedimentos.controller.api.swagger.ProcedureControllerSwagger;
import com.clinica.procedimentos.controller.mapper.ProcedureMapper;
import com.clinica.procedimentos.domain.dto.ProcedureDTOInput;
import com.clinica.procedimentos.domain.dto.ProcedureDTOOutput;
import com.clinica.procedimentos.domain.model.Procedure;
import com.clinica.procedimentos.domain.service.ProcedureService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/procedimentos")
@RequiredArgsConstructor
public class ProcedureController implements ProcedureControllerSwagger {

	final private ProcedureService procedureService;
	
	final private ProcedureMapper procedureMapper;
	
	@GetMapping
	public ResponseEntity<List<ProcedureDTOOutput>> findAllProcedures(){
		log.info("Requisição GET feita no EndPoint '/procedimentos' para consultar lista com todos o objetos Procedure presentes no banco de dados");
		List<Procedure> procedureList = procedureService.findAll();
		List<ProcedureDTOOutput> procedureDtoList = procedureMapper.toDTOCollection(procedureList);
		
		return ResponseEntity.ok(procedureDtoList);
	}
	
	@GetMapping("/{procedureId}")
	public ResponseEntity<ProcedureDTOOutput> findProcedureById(@PathVariable Long procedureId){
		log.info("Requisição GET feita no EndPoint '/procedimentos/{id}' para retornar objeto Procedure de Id= " + procedureId);
		Procedure procedure = procedureService.findById(procedureId);
		ProcedureDTOOutput procedureDto = procedureMapper.toDTO(procedure);
		
		return ResponseEntity.ok(procedureDto);
	}
	
	@PostMapping
	public ResponseEntity<ProcedureDTOOutput> createProcedure(@RequestBody @Valid ProcedureDTOInput dtoInput){
		log.info("Requisição POST feita no EndPoint '/procedimentos' para criar objeto Procedure para ser persistido no bando de dados");
		Procedure procedure = procedureMapper.toEntity(dtoInput);
		procedure = procedureService.saveProcedure(procedure);

		ProcedureDTOOutput ProcedureDto = procedureMapper.toDTO(procedure);
		return ResponseEntity.status(201).body(ProcedureDto);
	}
	
	@PutMapping("/{procedureId}")
	public ResponseEntity<ProcedureDTOOutput> updateProcedure(@PathVariable Long procedureId, @RequestBody @Valid ProcedureDTOInput dtoInput){
		log.info("Requisição PUT feita no EndPoint '/procedimentos/{id}' para Atualizar objeto Procedure de Id= " + procedureId);
		Procedure procedureInDb = procedureService.findById(procedureId);
		procedureMapper.copyToDomain(dtoInput, procedureInDb);
		
		procedureInDb = procedureService.saveProcedure(procedureInDb);
		ProcedureDTOOutput ProcedureDto = procedureMapper.toDTO(procedureInDb);
		
		
		return ResponseEntity.ok(ProcedureDto);
	}
	
	@DeleteMapping("/{procedureId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void deleteProcedureById(@PathVariable Long procedureId){
		log.info("Requisição DELETE feita no EndPoint '/procedimentos/{id}' para deletar objeto Procedure de Id= " + procedureId);
		procedureService.deleteProcedureById(procedureId);
	}
	
	
}
