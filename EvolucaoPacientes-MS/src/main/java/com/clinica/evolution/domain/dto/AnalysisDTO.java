package com.clinica.evolution.domain.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.clinica.evolution.domain.model.Evolution;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnalysisDTO {

	private String id;
	
	private DoctorDTO doctor;
	
	private PatientDTO patient;
	
	private ProcedureDTO procedure;
	
	private LocalDateTime date;
	
	private List<Evolution> evolutions; 

}
