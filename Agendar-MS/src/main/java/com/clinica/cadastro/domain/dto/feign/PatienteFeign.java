package com.clinica.cadastro.domain.dto.feign;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatienteFeign {

	private Long id;
	
	private String name;
	
	private String email;
	
	private String phone;
}
