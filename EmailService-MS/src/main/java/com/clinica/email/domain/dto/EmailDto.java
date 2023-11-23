package com.clinica.email.domain.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String patient_email;
	
	private String patient_name;
	
	private String doctor_name;
	
	private LocalDateTime date;
	
	private String convertDate;
	
	public void convertDateToEmail() {
		var format = DateTimeFormatter.ofPattern("dd/MM/yyyy : HH:mm:ss");
		this.convertDate = date.format(format);
	}
}
