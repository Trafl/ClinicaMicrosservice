package com.clinica.evolution.domain.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clinica.evolution.domain.exceptions.AnalysisNotFoundException;
import com.clinica.evolution.domain.model.Analysis;
import com.clinica.evolution.domain.model.Evolution;
import com.clinica.evolution.domain.repository.AnalysisRepository;
import com.clinica.evolution.domain.service.AnalysisService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class AnalysisServiceImpl implements AnalysisService {

	private final AnalysisRepository analysisRepository;
	
	@Transactional
	public Analysis save(Analysis analysis) {
		log.info("[AnalysisServiceImpl] executando metodo save para salvar a consulta: " + analysis);
		return analysisRepository.save(analysis);
	}
	
	public Analysis getOneAnalysisByIdAndDoctorId(String analysisId, Long doctorId) {
		try {
			log.info("[AnalysisServiceImpl] executando metodo getOneAnalysisByIdAndDoctorId para procurar a consulta de id:" + analysisId + " e medico de id:" + doctorId );
			return analysisRepository.findAnalysisByIdAndDoctorId(analysisId, doctorId);
		}
		catch (NullPointerException e) {
			throw new AnalysisNotFoundException(analysisId);
		}
	}
	
	public Page<Analysis> findAnalysisByDoctorId(Long doctorId, Pageable pageable){
		log.info("[AnalysisServiceImpl] executando metodo findAnalysisByDoctorId para procurar a consulta relacionada ao medico de id:" + doctorId );
		return analysisRepository.getAnalysisByDoctorId(doctorId, pageable);
	}
	
	@Transactional
	public List<Evolution> addEvolutionInAnalysisById(String analysisId, Long doctorId, Evolution evolution) {
		log.info("[AnalysisServiceImpl] executando metodo addEvolutionInAnalysisById para procurar a consulta de id:" + analysisId + " e medico de id:" + doctorId + " e adicionar evolução: " + evolution);
		var analisy = getOneAnalysisByIdAndDoctorId(analysisId, doctorId);
		analisy.getEvolutions().add(evolution);
		save(analisy);
		
		return analisy.getEvolutions();
	}
	
	public Page<Analysis> findAnalysisByPatientNameAndDoctorId(String patientName, Long doctorId, Pageable pageable) {
		log.info("[AnalysisServiceImpl] executando metodo findAnalysisByPatientNameAndDoctorId para procurar a consulta relacionada ao medico de id:" + doctorId );
		return analysisRepository.findAnalysisByPatientNameAndDoctorId(patientName, doctorId, pageable);
	}
}
