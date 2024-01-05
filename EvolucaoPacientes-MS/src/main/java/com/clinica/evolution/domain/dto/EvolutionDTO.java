package com.clinica.evolution.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EvolutionDTO {

	@NotBlank
	private String text;
}
