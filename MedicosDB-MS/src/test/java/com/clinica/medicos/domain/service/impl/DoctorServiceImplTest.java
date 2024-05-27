package com.clinica.medicos.domain.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import com.clinica.medicos.domain.exception.EntityNotFoundException;
import com.clinica.medicos.domain.exception.InformationInUseException;
import com.clinica.medicos.domain.model.Doctor;
import com.clinica.medicos.domain.repository.DoctorRepository;

@ExtendWith(MockitoExtension.class)
class DoctorServiceImplTest {

	@InjectMocks
	private DoctorServiceImpl service;

	@Mock
	private DoctorRepository repository;
	
	private Doctor doctor;
	
	@Captor
	private ArgumentCaptor<Long> idCaptor;

	@Captor
	private ArgumentCaptor<Doctor> doctorCaptor;
	
	@BeforeEach
	void setUp(){
		doctor = new Doctor("doutor@email", "Doutor", "123.456.789-09", "CRM/RJ123456", "Physiotherapist");
	}
	
	@Nested
	class FindById{
		
		@Test
		@DisplayName("Given Doctor id when FindById should return Doctor object")
		void givenDoctorId_When_FindById_ShouldReturnDoctorObject() {
			
			doctor.setId(1L);
			given(repository.findById(anyLong())).willReturn(Optional.of(doctor));
			
			var returnDoctor = service.findById(anyLong());
			
			assertNotNull(returnDoctor);
			assertEquals("Doutor", returnDoctor.getName());
			assertEquals("doutor@email", returnDoctor.getEmail());
			assertEquals("CRM/RJ123456", returnDoctor.getCrm());
			assertEquals("123.456.789-09", returnDoctor.getCpf());
			assertEquals("Physiotherapist", returnDoctor.getSpecialty());
		}
		
		@Test
		@DisplayName("Given inexistent id when FindById should throw EntityNotFoundException")
		void givenInexistentId_When_FindById_ShouldThrowEntityNotFoundException() {
			
			given(repository.findById(anyLong())).willReturn(Optional.empty());
			
			var content = assertThrows(EntityNotFoundException.class, 
					() -> {service.findById(1L);},
					() -> "EntityNotFoundException not throw ");
			
			assertEquals("Doctor with id 1 was not found", content.getMessage());
		}
		
		@Test
		@DisplayName("Given IdCaptor when FindById should capture the same id")
		void givenIdCaptor_When_FindById_ShouldCaptureTheSameId() {

			doctor.setId(1L);
			given(repository.findById(idCaptor.capture())).willReturn(Optional.of(doctor));
			
			var returnDoctor = service.findById(1L);
			
			var captor = idCaptor.getValue();
			
			assertEquals(returnDoctor.getId(), captor);
		}
	}

	@Nested
	class FindAll {
		
		@Test
		@DisplayName("When FindAll should return list of Doctors")
		void when_FindAll_ShouldReturnPageOfDoctors() {
			
			var doctor2 = new Doctor("doutora@email", "Doutora", "321.654.987-01", "CRM/RJ233311", "Physiotherapist");
			var doctorList = List.of(doctor, doctor2);
			
			given(repository.findAll()).willReturn(doctorList);
			
			var list = service.findAll();
			
			
			assertEquals(2, list.size());
			
			var firstDoctor = list.get(0);
			
			assertEquals("Doutor", firstDoctor.getName());
			assertEquals("doutor@email", firstDoctor.getEmail());
			assertEquals("CRM/RJ123456", firstDoctor.getCrm());
			assertEquals("123.456.789-09", firstDoctor.getCpf());
			assertEquals("Physiotherapist", firstDoctor.getSpecialty());
			
			var secondDoctor = list.get(1);
			
			assertEquals("Doutora", secondDoctor.getName());
			assertEquals("doutora@email", secondDoctor.getEmail());
			assertEquals("CRM/RJ233311", secondDoctor.getCrm());
			assertEquals("321.654.987-01", secondDoctor.getCpf());
			assertEquals("Physiotherapist", secondDoctor.getSpecialty());
		}
	
	}

	@Nested
	class SavePatient {
		
		@Test
		@DisplayName("Given Doctor object when SaveDoctor should return Doctor")
		void givenDoctorObject_When_SaveDoctor_ShouldReturnDoctor() {
			
			doctor.setId(1L);
			given(repository.save(any(Doctor.class))).willReturn(doctor);
			
			var returnDoctor = service.saveDoctor(doctor);
			
			assertNotNull(returnDoctor);
			
			assertTrue(returnDoctor.getId() > 0);
			assertEquals("Doutor", returnDoctor.getName());
			assertEquals("doutor@email", returnDoctor.getEmail());
			assertEquals("CRM/RJ123456", returnDoctor.getCrm());
			assertEquals("123.456.789-09", returnDoctor.getCpf());
			assertEquals("Physiotherapist", returnDoctor.getSpecialty());
			
		}
		
		@Test
		@DisplayName("Given DoctorCaptor when SaveDoctor should capture the same Doctor")
		void givenDoctorCaptor_When_SaveDoctor_ShouldCaptureTheSameDoctor() {
			
			given(repository.save(doctorCaptor.capture())).willReturn(doctor);
			
			var returnDoctor = service.saveDoctor(doctor);
			var captureDoctor = doctorCaptor.getValue();
			
			assertNotNull(returnDoctor);
			
			assertEquals(captureDoctor.getName(), returnDoctor.getName());
			assertEquals(captureDoctor.getEmail(), returnDoctor.getEmail());
			assertEquals(captureDoctor.getCrm(), returnDoctor.getCrm());
			assertEquals(captureDoctor.getCpf(), returnDoctor.getCpf());
			assertEquals(captureDoctor.getSpecialty(), returnDoctor.getSpecialty());
			
		}
		@Test
		@DisplayName("Given Doctor with CRM in use when SaveDoctor should throw InformationInUseException")
		void givenDoctorWithCrmInUse_When_SaveDoctor_ShouldThrowInformationInUseException() {
			given(repository.save(any(Doctor.class))).willThrow(DataIntegrityViolationException.class);
			
			var content = assertThrows(InformationInUseException.class, 
					() -> {
						service.saveDoctor(doctor);},
					()-> "Exception InformationInUseException not throw");
			
			var exceptionMessage = "CRM: " + doctor.getCrm() + " já esta cadastrado no sistema";
			
			assertEquals(exceptionMessage, content.getMessage());
		}
	}

	@Nested
	class DeletePatientById {

		@Test
		@DisplayName("Given Doctor id when DeleteDoctorById should delete Doctor")
		void givenDoctorId_When_DeleteDoctorById_ShouldDeleteDoctor() {
			
			given(repository.findById(anyLong())).willReturn(Optional.of(doctor));
			
			service.deleteDoctorById(anyLong());
			
			verify(repository, times(1)).deleteById(anyLong());
			
		}
		
		@Test
		@DisplayName("Given inexist Id when DeleteDoctorById should throw EntityNotFoundException")
		void givenInexistPatientId_When_DeleteDoctorById_ShouldThrowEntityNotFoundException() {
			
			given(repository.findById(anyLong())).willReturn(Optional.empty());
			
			var content = assertThrows(EntityNotFoundException.class,
					()-> {service.deleteDoctorById(1L);},
					()-> "EntityNotFoundException not throw");
			
			verify(repository, never()).deleteById(anyLong());
			
			assertEquals("Doctor with id 1 was not found", content.getMessage());
			
		}
	}

	@Nested
	class checkInformationAndSave {
		
		@Test
		@DisplayName("Given Doctor object with crm in use when SaveDoctor should throw InformationInUseException")
		void givenDoctorObjectWithCrmInUse_When_SaveDoctor_ShouldThrowInformationInUseException() {
			
			var newDoctor = new Doctor("doutora@email", "Doutora", "321.654.987-01", "CRM/RJ233311", "Physiotherapist");
			
			given(repository.existsByCrm(anyString())).willReturn(true);
			
			var content = assertThrows(InformationInUseException.class, 
					() ->{service.checkInformationAndSave(newDoctor);},
					
					()-> "Exception InformationInUseException not throw");
			
			assertEquals("CRM: " + newDoctor.getCrm() + " is already registered in the system", content.getMessage());
		}
		
	}
}
