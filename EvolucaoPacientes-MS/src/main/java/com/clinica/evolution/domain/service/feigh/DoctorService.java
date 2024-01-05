package com.clinica.evolution.domain.service.feigh;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.clinica.evolution.domain.model.Doctor;

@FeignClient(name = "medicos-db-ms")
public interface DoctorService {

	@GetMapping(value = "/medicos/{doctorId}")
	Doctor findDoctorById(@PathVariable Long doctorId);
}
