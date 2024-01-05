package com.clinica.evolution.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clinica.evolution.domain.model.Analysis;
import com.clinica.evolution.domain.model.Evolution;
import com.clinica.evolution.domain.repository.AnalysisRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnalysisService {

	private final AnalysisRepository analysisRepository;
	
	public Analysis getOneAnalysisById(String id) {
		return analysisRepository.findById(id)
				.orElseThrow(() -> new RuntimeException());
	}
	
	public List<Analysis> getAllByDoctorName(String name){
		return analysisRepository.findByDoctorName(name).stream().sorted((o1, o2) -> o2.getDate().compareTo(o1.getDate())).toList();
	}
	
	public List<Analysis> getAllByPatientName(String name){
		return analysisRepository.findByPatientName(name).stream().sorted((o1, o2) -> o2.getDate().compareTo(o1.getDate())).toList();
	}
	
	public List<Evolution> addEvolutionInAnalysisById(String id, Evolution evolution) {
		
		var analisys = getOneAnalysisById(id);
		
		analisys.getEvolutions().add(evolution);
		
		return analisys.getEvolutions();
	}
	
}
