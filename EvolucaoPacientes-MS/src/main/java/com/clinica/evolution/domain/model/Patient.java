package com.clinica.evolution.domain.model;

import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Patient {

	@Field(name = "patient_id")
	private Long id;
	
	@TextIndexed
	private String name;
}
