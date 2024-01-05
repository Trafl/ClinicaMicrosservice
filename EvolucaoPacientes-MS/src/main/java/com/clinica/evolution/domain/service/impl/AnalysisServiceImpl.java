package com.clinica.evolution.domain.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clinica.evolution.domain.exceptions.AnalysisNotFoundException;
import com.clinica.evolution.domain.model.Analysis;
import com.clinica.evolution.domain.model.Evolution;
import com.clinica.evolution.domain.repository.AnalysisRepository;
import com.clinica.evolution.domain.service.AnalysisService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnalysisServiceImpl implements AnalysisService {

	private final AnalysisRepository analysisRepository;
	
	@Transactional
	public Analysis save(Analysis analysis) {
		return analysisRepository.insert(analysis);
		
	}
	
	public Analysis getOneAnalysisById(String id) {
		return analysisRepository.findById(id)
				.orElseThrow(() -> new AnalysisNotFoundException(id));
	}
	
	public List<Analysis> getAllAnalysis(){
		return analysisRepository.findAll();	

	}
	
	public List<Analysis> getAnalysisByDoctorOrPatientName(String name){
		return analysisRepository.findAllAnalysisByName(name).stream().sorted((o1, o2) -> o2.getDate().compareTo(o1.getDate())).toList();
	}
	
	@Transactional
	public List<Evolution> addEvolutionInAnalysisById(String id, Evolution evolution) {
		
		var analisys = getOneAnalysisById(id);
		
		analisys.getEvolutions().add(evolution);
		
		return analisys.getEvolutions();
	}
	
	public List<Evolution> findEvolutionsByName(String name) {
		return analysisRepository.findEvolutionsByName(name);
	}
}
