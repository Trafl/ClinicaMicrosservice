package com.clinica.evolution.domain.service.kafka;

import java.util.ArrayList;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.clinica.evolution.domain.dto.kafka.AnalysisDTOFromKafka;
import com.clinica.evolution.domain.model.Analysis;
import com.clinica.evolution.domain.model.Doctor;
import com.clinica.evolution.domain.model.Patient;
import com.clinica.evolution.domain.model.Procedure;
import com.clinica.evolution.domain.service.AnalysisService;
import com.clinica.evolution.domain.service.feigh.DoctorService;
import com.clinica.evolution.domain.service.feigh.PatientService;
import com.clinica.evolution.domain.service.feigh.ProcedureService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Listener {
	
	private final DoctorService doctorService;
	
	private final PatientService patientService;
	
	private final ProcedureService procedureService;
	
	private final AnalysisService analysisService;

	@KafkaListener(topics = "agendar-to-patient-evolution", groupId = "group")
	public void receber(@Payload AnalysisDTOFromKafka analysisDTO) {

		Doctor doctorF = doctorService.findDoctorById(analysisDTO.getDoctor_id());
		Patient patientF = patientService.getPatient(analysisDTO.getPatient_id());
		Procedure procedureF = procedureService.getPatient(analysisDTO.getProcedure_id());
	
		Analysis analysis = Analysis.builder()
				.appointmentId(analysisDTO.getId())
				.doctor(doctorF)
				.patient(patientF)
				.procedure(procedureF)
				.date(analysisDTO.getDate())
				.evolutions(new ArrayList<>())
				.build();
		
		
		analysisService.save(analysis);
	}

}
