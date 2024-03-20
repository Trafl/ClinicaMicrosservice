package com.clinica.evolution.domain.service.feigh;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.clinica.evolution.domain.model.Patient;

@FeignClient(name = "paciente-db-ms")
public interface PatientService {

	@GetMapping("/pacientes/{patientId}")
	Patient getPatient(@PathVariable Long patientId);
}
