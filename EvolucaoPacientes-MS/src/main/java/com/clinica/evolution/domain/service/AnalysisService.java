package com.clinica.evolution.domain.service;

import java.util.List;

import com.clinica.evolution.domain.model.Analysis;
import com.clinica.evolution.domain.model.Evolution;

public interface AnalysisService {

	public Analysis save(Analysis analysis);
	
	public Analysis getOneAnalysisById(String id);
	
	public List<Analysis> getAllAnalysis();
	
	public List<Analysis> getAnalysisByDoctorOrPatientName(String name);
	
	public List<Evolution> addEvolutionInAnalysisById(String id, Evolution evolution);
	
	public List<Evolution> findEvolutionsByName(String name);
	
	List<Analysis> findByPatientName(String doctorName);
}
