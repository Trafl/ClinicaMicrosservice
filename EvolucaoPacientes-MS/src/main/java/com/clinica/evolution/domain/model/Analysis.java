package com.clinica.evolution.domain.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Document
@Data
@Builder
public class Analysis {

	@Id
	private String id;
	
	private Long appointmentId;
	
	private Doctor doctor;
	
	private Patient patient;
	
	private Procedure procedure;
	
	private LocalDateTime date;
	
	private List<Evolution> evolutions; 

}
