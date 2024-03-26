package com.clinica.cadastro.domain.model.feign;

import java.math.BigDecimal;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Procedure {

	private Long procedure_id;
	
	private String procedure_name;
	
	private BigDecimal procedure_value;
	
}