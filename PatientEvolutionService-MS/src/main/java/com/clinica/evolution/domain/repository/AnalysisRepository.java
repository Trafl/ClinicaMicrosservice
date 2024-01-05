package com.clinica.evolution.domain.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.clinica.evolution.domain.model.Analysis;

public interface AnalysisRepository extends MongoRepository<Analysis, String> {

	List<Analysis> findByDoctorName(String doctorName);
	
	List<Analysis> findByPatientName(String doctorName);
}
