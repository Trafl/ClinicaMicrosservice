package com.clinica.evolution.controller.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.clinica.evolution.domain.dto.AnalysisDTO;
import com.clinica.evolution.domain.dto.EvolutionDTO;
import com.clinica.evolution.domain.model.Analysis;
import com.clinica.evolution.domain.model.Evolution;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AnalysisMapper {
	
	private final ModelMapper mapper;
	
	public Analysis toAnalysisEntity(AnalysisDTO analysisDTO) {
		return mapper.map(analysisDTO, Analysis.class);
	}
	
	public AnalysisDTO toAnalysisDTO(Analysis analysis) {
		return mapper.map(analysis, AnalysisDTO.class);
	}
	
	public List<AnalysisDTO> toAnalysisDTOCollection(List<Analysis> analysis){
		return analysis.stream().map(a-> toAnalysisDTO(a)).toList();
	}
	
	public Evolution toEvolutionEntity(EvolutionDTO evolutionDTO) {
		return mapper.map(evolutionDTO, Evolution.class);
	}
	
	public EvolutionDTO toEvolutionDTO(Evolution evolution) {
		return mapper.map(evolution, EvolutionDTO.class);
	}
	
	public List<EvolutionDTO> toEvolutionDTOCollection(List<Evolution> evolutions){
		return evolutions.stream().map(a-> toEvolutionDTO(a)).toList();
	}
	
}
