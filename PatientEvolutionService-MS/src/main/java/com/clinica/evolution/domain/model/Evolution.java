package com.clinica.evolution.domain.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Evolution {

	private LocalDateTime dayOfEvolution = LocalDateTime.now();
	private String text;
}
