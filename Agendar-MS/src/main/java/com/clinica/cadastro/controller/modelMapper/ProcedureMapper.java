package com.clinica.cadastro.controller.modelMapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.clinica.cadastro.domain.dto.input.ProcedureDTOInput;
import com.clinica.cadastro.domain.dto.output.ProcedureDTOOutput;
import com.clinica.cadastro.domain.model.Procedure;

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
