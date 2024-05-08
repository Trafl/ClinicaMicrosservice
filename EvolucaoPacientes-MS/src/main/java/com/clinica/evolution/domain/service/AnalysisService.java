package com.clinica.evolution.domain.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.clinica.evolution.domain.model.Analysis;
import com.clinica.evolution.domain.model.Evolution;

public interface AnalysisService {

	public Analysis save(Analysis analysis);
	
	public Analysis getOneAnalysisByIdAndDoctorId(String analysisId, Long doctorId);
	
	public Page<Analysis> findAnalysisByDoctorId(Long doctorId, Pageable pageable);
	
	public List<Evolution> addEvolutionInAnalysisById(String id, Long doctorId, Evolution evolution);
	
	public Page<Analysis> findAnalysisByPatientNameAndDoctorId(String patientName, Long doctorId, Pageable pageable);
}
