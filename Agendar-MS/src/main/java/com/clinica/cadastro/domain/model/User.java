package com.clinica.cadastro.domain.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String firstName;
	
	private String lastName;
	
	private Date birthday;

	@Enumerated(EnumType.STRING)
	private Role role;
	
	public boolean isDoctor() {
		return this.role.equals(Role.ROLE_DOCTOR);
	}
}
