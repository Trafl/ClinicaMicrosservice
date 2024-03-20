package com.clinica.cadastro.core.modelmapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.clinica.cadastro.domain.dto.output.DoctorOutput;
import com.clinica.cadastro.domain.dto.output.MedicalAppointmentDtoFinancial;
import com.clinica.cadastro.domain.dto.output.MedicalAppointmentOutPut;
import com.clinica.cadastro.domain.dto.output.PatienteOutPut;
import com.clinica.cadastro.domain.dto.output.ProcedureDTOOutput;
import com.clinica.cadastro.domain.model.MedicalAppointment;
import com.clinica.cadastro.domain.model.feign.Doctor;
import com.clinica.cadastro.domain.model.feign.Patient;
import com.clinica.cadastro.domain.model.feign.Procedure;

@Configuration
public class ModelMapperConfig {

	@Bean
	ModelMapper modelMapper() {
		
		var modelMapper = new ModelMapper();
		
		doctorToDto(modelMapper);
		
		patientToDto(modelMapper);
		
		procedureToDto(modelMapper);

        appointmentToDto(modelMapper);
			
        appointmentToFinancial(modelMapper);
		
        return modelMapper;
	}

	private void appointmentToFinancial(ModelMapper modelMapper) {
		TypeMap<MedicalAppointment, MedicalAppointmentDtoFinancial> medicalAppointmentFinancialTypeMap = modelMapper.createTypeMap(MedicalAppointment.class, MedicalAppointmentDtoFinancial.class);
        medicalAppointmentFinancialTypeMap.addMapping(appointment -> appointment.getDoctor().getDoctor_id(), MedicalAppointmentDtoFinancial::setDoctor_id)
        								  .addMapping(appointment -> appointment.getPatient().getPatient_id(), MedicalAppointmentDtoFinancial::setPatient_id)
        								  .addMapping(appointment -> appointment.getProcedure().getProcedure_id(), MedicalAppointmentDtoFinancial::setProcedure_id)
        								  .addMapping(appointment -> appointment.getProcedure().getProcedure_value(), MedicalAppointmentDtoFinancial::setValue)
        								  .addMapping(appointment -> appointment.getCreatedDate(), MedicalAppointmentDtoFinancial::setCreatedDate)
        								  .addMapping(appointment -> appointment.getFinishedAppointment(), MedicalAppointmentDtoFinancial::setFinishedAppointment);
	}

	private void appointmentToDto(ModelMapper modelMapper) {
		TypeMap<MedicalAppointment, MedicalAppointmentOutPut> medicalAppointmentTypeMap = modelMapper.createTypeMap(MedicalAppointment.class, MedicalAppointmentOutPut.class);
        medicalAppointmentTypeMap.addMapping(appointment -> appointment.getDate(), MedicalAppointmentOutPut::setDate);
	}

	private void procedureToDto(ModelMapper modelMapper) {
		TypeMap<Procedure, ProcedureDTOOutput> procedureTypeMap = modelMapper.createTypeMap(Procedure.class, ProcedureDTOOutput.class);
		procedureTypeMap.addMapping(procedure -> procedure.getProcedure_name(), ProcedureDTOOutput::setName)
						.addMapping(procedure -> procedure.getProcedure_value(), ProcedureDTOOutput::setValue);
	}

	private void patientToDto(ModelMapper modelMapper) {
		TypeMap<Patient, PatienteOutPut> patientTypeMap = modelMapper.createTypeMap(Patient.class, PatienteOutPut.class);
		patientTypeMap.addMapping(src -> src.getPatient_name(), PatienteOutPut::setName)
					  .addMapping(patient -> patient.getPatient_email(), PatienteOutPut::setEmail)
					  .addMapping(patient -> patient.getPatient_phone(), PatienteOutPut::setPhone);
	}

	private void doctorToDto(ModelMapper modelMapper) {
		TypeMap<Doctor, DoctorOutput> doctorTypeMap = modelMapper.createTypeMap(Doctor.class, DoctorOutput.class);
		doctorTypeMap.addMapping(doctor -> doctor.getDoctor_name(), DoctorOutput::setName)
			     	.addMapping(doctor -> doctor.getDoctor_specialty(), DoctorOutput::setSpecialty);
	}
}
