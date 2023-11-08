package com.clinica.cadastro.domain.model;

import java.time.LocalDateTime;

import com.clinica.cadastro.domain.model.feign.Doctor;
import com.clinica.cadastro.domain.model.feign.Patient;
import com.clinica.cadastro.domain.model.feign.Procedure;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "medical_appointment")
public class MedicalAppointment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Embedded
	private Doctor doctor;
	
	@Embedded
	private Patient patient;
	
	@Embedded
	private Procedure procedure;
	
	private LocalDateTime date;
	
	private LocalDateTime createdDate = LocalDateTime.now();
	
	private LocalDateTime finishedAppointment;
	
	private LocalDateTime cancelAppointment;
	
	@Enumerated(EnumType.STRING)
	private AppointmentStatus status = AppointmentStatus.CREATED;
	
	public void finishAppointment() {
		if(status.equals(AppointmentStatus.CREATED)) {
			this.status = AppointmentStatus.FINISHED;
			this.finishedAppointment = LocalDateTime.now();
		}
	}
	
	public void canceledAppointment() {
		this.status = AppointmentStatus.CANCELED;
		this.cancelAppointment = LocalDateTime.now();	
	}
}
