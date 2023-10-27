package com.clinica.cadastro.controller.modelMapper;

import java.util.Collection;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.clinica.cadastro.domain.dto.input.MedicalAppointmentDTOInput;
import com.clinica.cadastro.domain.dto.output.MedicalAppointmentDtoFinancial;
import com.clinica.cadastro.domain.dto.output.MedicalAppointmentOutPut;
import com.clinica.cadastro.domain.model.MedicalAppointment;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AppointmentMapper {

	final private ModelMapper mapper;
	
	public MedicalAppointment toEntity(MedicalAppointmentDTOInput appointmentInput) {
		return mapper.map(appointmentInput, MedicalAppointment.class);
	}
	
	public MedicalAppointmentOutPut toDTO (MedicalAppointment appointment) {
		return mapper.map(appointment, MedicalAppointmentOutPut.class);
	}
	
	public MedicalAppointmentDtoFinancial toDTOFinancial (MedicalAppointment appointment) {
		return mapper.map(appointment, MedicalAppointmentDtoFinancial.class);
	}
	
	public List<MedicalAppointmentOutPut> toDtoCollection(Collection<MedicalAppointment> list){
		return list.stream().map(appointment -> toDTO(appointment)).toList();
	}
	
}
