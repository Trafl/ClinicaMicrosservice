package com.clinica.evolution.domain.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Evolution {

	private LocalDateTime dayOfEvolution = LocalDateTime.now();
	
	private String text;
}
