package com.clinica.medicos.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Data
@Entity
@Table(name = "doctor", uniqueConstraints = {@UniqueConstraint(columnNames = "crm")})
public class Doctor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String email;
	
	private String name;
	
	private String cpf;
	
	private String crm;
	
	private String specialty;

	public Doctor() {}

	public Doctor(String email, String name, String cpf, String crm, String specialty) {
		this.email = email;
		this.name = name;
		this.cpf = cpf;
		this.crm = crm;
		this.specialty = specialty;
	}
}
