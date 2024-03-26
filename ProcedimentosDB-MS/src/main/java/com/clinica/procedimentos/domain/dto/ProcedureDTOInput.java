package com.clinica.procedimentos.domain.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class ProcedureDTOInput {
	
	@NotBlank
	@Schema(example = "Lymphatic drainage")
	private String name;
	
	@NotBlank
	@Schema(example = "Lymphatic drainage in specific muscles")
	private String description;
	
	@NotNull
	@PositiveOrZero
	@Schema(example = "120.00")
	private BigDecimal value;

	public ProcedureDTOInput(@NotBlank String name, @NotBlank String description,
			@NotNull @PositiveOrZero BigDecimal value) {
		super();
		this.name = name;
		this.description = description;
		this.value = value;
	}
	
	
}
