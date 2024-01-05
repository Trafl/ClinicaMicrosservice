package com.clinica.evolution.domain.model;

import org.springframework.data.mongodb.core.index.Indexed;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Doctor {

	private Long id;
	
	@Indexed
	private String name;
	
}
