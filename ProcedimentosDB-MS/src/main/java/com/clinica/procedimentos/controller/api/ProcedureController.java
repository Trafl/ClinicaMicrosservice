package com.clinica.procedimentos.controller.api;

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

import com.clinica.procedimentos.controller.api.swagger.ProcedureControllerSwagger;
import com.clinica.procedimentos.controller.mapper.ProcedureMapper;
import com.clinica.procedimentos.domain.dto.ProcedureDTOInput;
import com.clinica.procedimentos.domain.dto.ProcedureDTOOutput;
import com.clinica.procedimentos.domain.model.Procedure;
import com.clinica.procedimentos.domain.service.ProcedureService;

import jakarta.servlet.http.HttpServletRequest;
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
	
	String timestamp = LocalDateTime.now().toString();
	@GetMapping
	public ResponseEntity<Page<ProcedureDTOOutput>> findAllProcedures(@PageableDefault Pageable pageable, HttpServletRequest request){
		log.info("[{}] - [ProcedureController] IP: {}, Request: GET, EndPoint: '/procedimentos'", timestamp, request.getRemoteAddr());	
		
		List<Procedure> procedureList = procedureService.findAll();
		List<ProcedureDTOOutput> procedureDtoList = procedureMapper.toDTOCollection(procedureList);
		PageImpl<ProcedureDTOOutput> procedureDtoPage = new PageImpl<>(procedureDtoList, pageable, procedureList.size());
		
		return ResponseEntity.ok(procedureDtoPage);
	}
	
	@GetMapping("/{procedureId}")
	public ResponseEntity<ProcedureDTOOutput> findProcedureById(@PathVariable Long procedureId, HttpServletRequest request){
		log.info("[{}] - [ProcedureController] IP: {}, Request: GET, EndPoint: '/procedimentos/{}' Procedure Id: {}", timestamp, request.getRemoteAddr(), procedureId, procedureId);

		Procedure procedure = procedureService.findById(procedureId);
		ProcedureDTOOutput procedureDto = procedureMapper.toDTO(procedure);
		
		return ResponseEntity.ok(procedureDto);
	}
	
	@PostMapping
	public ResponseEntity<ProcedureDTOOutput> createProcedure(@RequestBody @Valid ProcedureDTOInput dtoInput, HttpServletRequest request){
		log.info("[{}] - [ProcedureController] IP: {}, Request: POST, EndPoint: '/procedimentos/' ", timestamp, request.getRemoteAddr());
		
		Procedure procedure = procedureMapper.toEntity(dtoInput);
		procedure = procedureService.saveProcedure(procedure);
		ProcedureDTOOutput ProcedureDto = procedureMapper.toDTO(procedure);
		
		return ResponseEntity.status(201).body(ProcedureDto);
	}
	
	@PutMapping("/{procedureId}")
	public ResponseEntity<ProcedureDTOOutput> updateProcedure(@PathVariable Long procedureId, @RequestBody @Valid ProcedureDTOInput dtoInput, HttpServletRequest request){
		log.info("[{}] - [ProcedureController] IP: {}, Request: PUT, EndPoint: '/procedimentos/{}' Procedure Id: {}", timestamp, request.getRemoteAddr(), procedureId, procedureId);
		
		Procedure procedureInDb = procedureService.findById(procedureId);
		procedureMapper.copyToDomain(dtoInput, procedureInDb);
		procedureInDb = procedureService.saveProcedure(procedureInDb);
		ProcedureDTOOutput ProcedureDto = procedureMapper.toDTO(procedureInDb);
		
		return ResponseEntity.ok(ProcedureDto);
	}
	
	@DeleteMapping("/{procedureId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void deleteProcedureById(@PathVariable Long procedureId, HttpServletRequest request){
		log.info("[{}] - [ProcedureController] IP: {}, Request: DELETE, EndPoint: '/procedimentos/{}' Procedure Id: {}", timestamp, request.getRemoteAddr(), procedureId, procedureId);
		procedureService.deleteProcedureById(procedureId);
	}
	
	
}
