package com.clinica.procedimentos.domain.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "_procedure")
public class Procedure {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	private String description;
	
	private BigDecimal value;

	public Procedure() {
		super();
	}
	
	public Procedure(String name, String description, BigDecimal value) {
		super();
		this.name = name;
		this.description = description;
		this.value = value;
	}
}
