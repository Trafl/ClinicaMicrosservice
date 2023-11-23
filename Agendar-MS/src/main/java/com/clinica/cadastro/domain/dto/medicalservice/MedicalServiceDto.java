package com.clinica.cadastro.domain.dto.medicalservice;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicalServiceDto {

	private Long id;
	
	private Long doctor_id;
	private String doctor_name;
	
	private Long pacient_id;
	private String pacient_name;
	
}
