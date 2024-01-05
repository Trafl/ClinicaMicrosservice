package com.clinica.evolution.domain.service.feigh;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.clinica.evolution.domain.model.Procedure;

@FeignClient(name = "procedimentos-db-ms")
public interface ProcedureService {

	@GetMapping("/procedimentos/{procedureId}")
	Procedure getPatient(@PathVariable Long procedureId);
}
