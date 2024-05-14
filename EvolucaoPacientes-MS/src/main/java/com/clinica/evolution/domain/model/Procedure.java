package com.clinica.evolution.domain.model;

import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Procedure {

	@Field(name = "procedure_id")
	private Long id;
	
	@TextIndexed
	private String name;
}
