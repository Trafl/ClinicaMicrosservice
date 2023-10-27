package com.clinica.cadastro.domain.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.clinica.cadastro.domain.dto.feign.DoctorFeign;

@FeignClient(name = "medicos-db-ms")
public interface DoctorService {

	@GetMapping(value = "/medicos/{doctorId}")
	DoctorFeign findDoctorById(@PathVariable Long doctorId);
}