package com.clinica.cadastro.domain.dto.input;

import java.util.Date;

import com.clinica.cadastro.domain.model.Role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTOInput {

	@NotBlank
	private String firstName;
	
	@NotBlank
	private String lastName;
	
	@NotNull
	private Date birthday;
	
	@NotNull
	private Role role;
}
