package com.clinica.evolution.domain.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.clinica.evolution.domain.model.Analysis;
import com.clinica.evolution.domain.model.Evolution;

public interface AnalysisRepository extends MongoRepository<Analysis, String> {

	@Query("{$text: {$search: ?0}}")
	List<Analysis> findAllAnalysisByName(String name);
	
	@Query("{$text: {$search: ?0}}, {evolutions: 1}")
	List<Evolution> findEvolutionsByName(String name);
	
	@Query("{'patient.name':{ $regex: ?0, $options: 'i'} }")
	List<Analysis> findByPatientName(String patientName);
}
