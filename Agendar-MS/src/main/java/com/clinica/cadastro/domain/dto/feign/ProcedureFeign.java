package com.clinica.cadastro.domain.dto.feign;

import java.math.BigDecimal;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class ProcedureFeign {

	private Long id;
	
	private String name;
	
	private BigDecimal value;
	
}