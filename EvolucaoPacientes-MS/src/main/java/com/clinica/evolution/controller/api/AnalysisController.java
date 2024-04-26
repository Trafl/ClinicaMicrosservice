package com.clinica.evolution.controller.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.clinica.evolution.controller.mapper.AnalysisMapper;
import com.clinica.evolution.domain.dto.AnalysisDTO;
import com.clinica.evolution.domain.dto.EvolutionDTO;
import com.clinica.evolution.domain.service.AnalysisService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/prontuarios")
public class AnalysisController {

	private final AnalysisService analysisService;
	
	private final AnalysisMapper mapper;
	
	// Futuramente usar o contexto do spring security para sempre usar o id/nome do medico automaticamente nas buscas 
	@GetMapping("/{name}")
	public ResponseEntity<List<AnalysisDTO>> getAllAnalysisByName(@PathVariable String name){
		var list = analysisService.getAnalysisByDoctorOrPatientName(name);
		var listDTO = mapper.toAnalysisDTOCollection(list);
		return ResponseEntity.ok(listDTO);
		
	}
	
	//Vai filtrar o nome do paciente junto com o id do medico logado, contexto do security 
	@GetMapping("/procurar")
	public ResponseEntity<List<AnalysisDTO>> getAnalysisByPatientName(@RequestParam String patientName){
		var list = analysisService.findByPatientName(patientName);
		var listDTO = mapper.toAnalysisDTOCollection(list);
		return ResponseEntity.ok(listDTO);
	}

	@PostMapping("/{id}")
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseEntity<List<EvolutionDTO>> addEvolutionToAnalysisById(@PathVariable String id, @Valid @RequestBody EvolutionDTO evolutionDTO){
		var evolution = mapper.toEvolutionEntity(evolutionDTO);
		var listOfEvolutions = analysisService.addEvolutionInAnalysisById(id, evolution);
		var listOfEvolutionsDTO = mapper.toEvolutionDTOCollection(listOfEvolutions);
		return ResponseEntity.ok(listOfEvolutionsDTO);
	}

	@GetMapping
	public List<AnalysisDTO> getAll(){
		return mapper.toAnalysisDTOCollection(analysisService.getAllAnalysis());
	}
}
