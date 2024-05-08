package com.clinica.evolution.controller.api;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clinica.evolution.controller.mapper.AnalysisMapper;
import com.clinica.evolution.domain.dto.AnalysisDTO;
import com.clinica.evolution.domain.dto.EvolutionDTO;
import com.clinica.evolution.domain.service.AnalysisService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/prontuarios")
public class AnalysisController {

	private final AnalysisService analysisService;
	
	private final AnalysisMapper mapper;
	
	// Futuramente usar o contexto do spring security para sempre usar o id do medico automaticamente nas buscas 
	@GetMapping("/medico/{doctorId}")
	public ResponseEntity<Page<AnalysisDTO>> getAllAnalysisByDoctorId(@PathVariable Long doctorId, @PageableDefault Pageable pageable){
		log.info("Requisição GET feita no EndPoint '/medicos/doctorId' para consultar todas as consultas presentes no banco de dados relacionadas ao medico de id:" + doctorId);
		var page = analysisService.findAnalysisByDoctorId(doctorId, pageable);
		var pageDTO = mapper.toAnalysisDTOCollection(page.getContent());
		PageImpl<AnalysisDTO> pageOfAnalysis = new PageImpl<>(pageDTO, pageable, pageDTO.size());
		
		return ResponseEntity.ok(pageOfAnalysis);
	}
	
	//Vai filtrar o nome do paciente junto com o id do medico logado, contexto do security 
	@GetMapping("/medico/{doctorId}/paciente")
	public ResponseEntity<Page<AnalysisDTO>> getAnalysisByPatientNameAndDoctorId(@PathVariable Long doctorId, @RequestParam String patientName, @PageableDefault Pageable pageable){
		log.info("Requisição GET feita no EndPoint '/medicos/doctorId/paciente' para consultar todas as consultas presentes no banco de dados relacionadas ao paciente e ao medico de id:" + doctorId);
		var page = analysisService.findAnalysisByPatientNameAndDoctorId(patientName, doctorId, pageable);
		var pageDTO = mapper.toAnalysisDTOCollection(page.getContent());
		PageImpl<AnalysisDTO> pageOfAnalysis = new PageImpl<>(pageDTO, pageable, pageDTO.size());
		
		return ResponseEntity.ok(pageOfAnalysis);
	}

	@GetMapping("/medico/{doctorId}/consulta/{analysisId}")
	public ResponseEntity<AnalysisDTO> getAnalysisByIdAndDoctorId(@PathVariable Long doctorId,@PathVariable String analysisId){
		log.info("Requisição GET feita no EndPoint '/medicos/doctorId/consulta/analysisId' para consultar todas as consultas presentes no banco de dados relacionadas ao medico de id:" + doctorId + " e a consulta de id:" + analysisId);
		var analysis = analysisService.getOneAnalysisByIdAndDoctorId(analysisId, doctorId);
		var analysisDTO = mapper.toAnalysisDTO(analysis);
		
		return ResponseEntity.status(200).body(analysisDTO);
	}
	
	@PostMapping("/medico/{doctorId}/consulta/{analysisId}")
	public ResponseEntity<List<EvolutionDTO>> addEvolutionToAnalysisByIdAndDoctorId(@PathVariable Long doctorId,@PathVariable String analysisId, @Valid @RequestBody EvolutionDTO evolutionDTO){
		log.info("Requisição POST feita no EndPoint '/medicos/doctorId/consulta/analysisId' para adicionar uma evolução a consulta presente no banco de dados relacionada ao medico de id:" + doctorId + " e a consulta de id:" + analysisId);
		var evolution = mapper.toEvolutionEntity(evolutionDTO);
		var listOfEvolutions = analysisService.addEvolutionInAnalysisById(analysisId, doctorId, evolution);
		var listOfEvolutionsDTO = mapper.toEvolutionDTOCollection(listOfEvolutions);

		return ResponseEntity.status(201).body(listOfEvolutionsDTO);
	}
}
