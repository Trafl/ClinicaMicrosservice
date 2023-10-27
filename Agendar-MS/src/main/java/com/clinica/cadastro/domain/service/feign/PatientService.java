package com.clinica.cadastro.domain.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.clinica.cadastro.domain.dto.feign.PatienteFeign;

@FeignClient(name = "pacientes-db-ms")
public interface PatientService {

	@GetMapping("/pacientes/{patientId}")
	PatienteFeign getPatient(@PathVariable Long patientId);
}