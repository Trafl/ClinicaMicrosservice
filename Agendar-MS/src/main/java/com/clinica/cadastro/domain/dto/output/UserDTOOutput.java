package com.clinica.cadastro.domain.dto.output;

import java.util.Date;

import com.clinica.cadastro.domain.model.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTOOutput {

	private Long id;
	
	private String firstName;
	
	private String lastName;
	
	private Date birthday;
	
	private Role role;
}
