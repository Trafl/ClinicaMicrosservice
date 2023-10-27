package com.clinica.medicos.controller.api;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.clinica.medicos.controller.modelmapper.DoctorMapper;
import com.clinica.medicos.domain.dto.DoctorKafkaDTOOutput;
import com.clinica.medicos.domain.service.DoctorService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentListener {
	
	private final DoctorMapper doctorMapper;

	private final DoctorService doctorService;
	
	private final KafkaTemplate<String, DoctorKafkaDTOOutput> kafkaTemplate;
	
	@KafkaListener(topics = "appointment-to-doctor", groupId = "grupo")
	public void getDoctor(Long id) {
		
		System.out.println("Chegou em doctor-MS");
		
		var doctor = doctorService.findById(id);
		var doctorOut = doctorMapper.toDTOKafka(doctor);
		
		kafkaTemplate.send("doctor-to-appointment", doctorOut);
		
		System.out.println("Envio para appoitment-MS");
		
	}
}