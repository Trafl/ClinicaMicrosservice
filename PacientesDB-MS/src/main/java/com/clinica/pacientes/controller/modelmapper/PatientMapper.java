package com.clinica.pacientes.controller.modelmapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.clinica.pacientes.domain.dto.PatientDTOInput;
import com.clinica.pacientes.domain.dto.PatientDTOOutput;
import com.clinica.pacientes.domain.model.Patient;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PatientMapper {

	final private ModelMapper mapper;
	
	public Patient toEntity(PatientDTOInput patientInput) {
		return mapper.map(patientInput, Patient.class);
	}
	
	public PatientDTOOutput toDTO (Patient patient) {
		return mapper.map(patient, PatientDTOOutput.class);
	}
	
	public void copyToDomain(PatientDTOInput patientInput, Patient patient) {
		mapper.map(patientInput, patient);
	}
	
	public List<PatientDTOOutput> toDTOCollection(List<Patient> patientList){
		return patientList.stream()
				.map((patient) -> toDTO(patient)).toList();
	}
}
