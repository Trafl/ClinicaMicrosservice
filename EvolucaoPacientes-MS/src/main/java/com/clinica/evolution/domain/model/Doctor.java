package com.clinica.evolution.domain.model;

import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Doctor {

	@Field(name = "doctor_id")
	private Long id;
	
	@TextIndexed
	private String name;
	
}
