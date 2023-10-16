package com.clinica.cadastro.domain.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "medical_appointment")
public class MedicalAppointment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	private User doctor;
	
	@OneToOne
	private User patient;
	
	private Date time;
	
	@OneToOne
	private Procedure procedure;
	
	private Boolean open = true;
	
	public Boolean isOpen() {
		if(this.open) {
			return true;
		}
		return false;
	}
	
	public void closeProcedure() {
		if(this.open) {
			this.open = false;
		}
	}
}
