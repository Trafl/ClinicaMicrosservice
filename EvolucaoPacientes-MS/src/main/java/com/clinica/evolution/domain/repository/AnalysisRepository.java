package com.clinica.evolution.domain.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.clinica.evolution.domain.model.Analysis;
import com.clinica.evolution.domain.model.Evolution;

public interface AnalysisRepository extends MongoRepository<Analysis, String> {
	
	@Query("find('doctor.doctor_id': ?0}).sort({date: -1})")
	Page<Analysis> getAnalysisByDoctorId(Long doctorId, Pageable pageable);
	
	@Query("{$text: {$search: ?0}}, {evolutions: 1}")
	List<Evolution> findEvolutionsByName(String name);
	
	@Query("find({$text: {$search: ?0}, 'doctor.doctor_id': ?1}).sort({date: -1})")
	Page<Analysis> findAnalysisByPatientNameAndDoctorId(String patientName, Long doctorId, Pageable pageable);
	
	@Query("{'_id': ?0, 'doctor.doctor_id': ?1 }")
	Analysis findAnalysisByIdAndDoctorId(String analysisId, Long doctorId);
}
