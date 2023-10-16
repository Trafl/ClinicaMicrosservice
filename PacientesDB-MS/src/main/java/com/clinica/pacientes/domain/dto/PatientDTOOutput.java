package com.clinica.pacientes.domain.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientDTOOutput {

	private Long id;
	
	private String firstName;
	
	private String lastName;
	
	private Date birthday;
	
	private String gender;

	private String email;
	
	private String phone;
}
