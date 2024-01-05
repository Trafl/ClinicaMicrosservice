package com.clinica.evolution.domain.model;

import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Doctor {

	@Field(name = "doctor_id")
	private Long id;
	
	@TextIndexed
	private String name;
	
}
