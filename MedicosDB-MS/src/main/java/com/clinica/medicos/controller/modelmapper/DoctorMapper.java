package com.clinica.medicos.controller.modelmapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.clinica.medicos.domain.dto.DoctorDTOInput;
import com.clinica.medicos.domain.dto.DoctorDTOOutput;
import com.clinica.medicos.domain.model.Doctor;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DoctorMapper {

	final private ModelMapper mapper;
	
	public Doctor toEntity(DoctorDTOInput doctorInput) {
		return mapper.map(doctorInput, Doctor.class);
	}
	
	public DoctorDTOOutput toDTO (Doctor doctor) {
		return mapper.map(doctor, DoctorDTOOutput.class);
	}
	
	public void copyToDomain(DoctorDTOInput doctorInput, Doctor doctor) {
		mapper.map(doctorInput, doctor);
	}
	
	public List<DoctorDTOOutput> toDTOCollection(List<Doctor> doctorList){
		return doctorList.stream()
				.map((doctor) -> toDTO(doctor)).toList();
	}
}
