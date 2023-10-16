package com.clinica.procedimentos.controller.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.clinica.procedimentos.domain.dto.ProcedureDTOInput;
import com.clinica.procedimentos.domain.dto.ProcedureDTOOutput;
import com.clinica.procedimentos.domain.model.Procedure;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProcedureMapper {

	final private ModelMapper mapper;
	
	public Procedure toEntity(ProcedureDTOInput ProcedureInput) {
		return mapper.map(ProcedureInput, Procedure.class);
	}
	
	public ProcedureDTOOutput toDTO (Procedure procedure) {
		return mapper.map(procedure, ProcedureDTOOutput.class);
	}
	
	public void copyToDomain(ProcedureDTOInput procedureInput, Procedure procedure) {
		mapper.map(procedureInput, procedure);
	}
	
	public List<ProcedureDTOOutput> toDTOCollection(List<Procedure> procedureList){
		return procedureList.stream()
				.map((Procedure) -> toDTO(Procedure)).toList();
	}
}
