package com.clinica.cadastro.domain.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.clinica.cadastro.domain.dto.feign.ProcedureFeign;

@FeignClient(name = "procedimentos-db-ms")
public interface ProcedureService {

	@GetMapping("/procedimentos/{procedureId}")
	ProcedureFeign getPatient(@PathVariable Long procedureId);
}
