package com.clinica.pacientes.domain.service.impl;

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

import java.time.LocalDate;
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

import com.clinica.pacientes.domain.exception.EntityNotFoundException;
import com.clinica.pacientes.domain.exception.InformationInUseException;
import com.clinica.pacientes.domain.model.Patient;
import com.clinica.pacientes.domain.repository.PatientRepository;

@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest {
	
	@InjectMocks
	private PatientServiceImpl service;

	@Mock
	private PatientRepository repository;
	
	private Patient patient;
	
	private LocalDate date = LocalDate.of(1990, 9, 1);
	
	@Captor
	private ArgumentCaptor<Long> idCaptor;

	@Captor
	private ArgumentCaptor<Patient> patientCaptor;
	
	@BeforeEach
	void setUp(){
		patient = new Patient("Pedro ivo", date, "Male", "email@email.com", "99999-9999");
	}
	
	@Nested
	class FindById{
		
		@Test
		@DisplayName("Given Patient id when FindById should return Patient object")
		void givenPatientId_When_FindById_ShouldReturnPatientObject() {
			
			patient.setId(1L);
			given(repository.findById(anyLong())).willReturn(Optional.of(patient));
			
			var returnPatient = service.findById(anyLong());
			
			assertNotNull(returnPatient);
			assertEquals("Pedro ivo", returnPatient.getName());
			assertEquals(date, returnPatient.getBirthday());
			assertEquals("Male", returnPatient.getGender());
			assertEquals("email@email.com", returnPatient.getEmail());
			assertEquals("99999-9999", returnPatient.getPhone());
		}
		
		@Test
		@DisplayName("Given inexistent id when FindById should throw EntityNotFoundException")
		void givenInexistentId_When_FindById_ShouldThrowEntityNotFoundException() {
			
			given(repository.findById(anyLong())).willReturn(Optional.empty());
			
			var content = assertThrows(EntityNotFoundException.class, 
					() -> {service.findById(1L);},
					() -> "EntityNotFoundException not throw ");
			
			assertEquals("Paciente de id 1 não foi encontrado", content.getMessage());
		}
		
		@Test
		@DisplayName("Given IdCaptor when FindById should capture the same id")
		void givenIdCaptor_When_FindById_ShouldCaptureTheSameId() {

			patient.setId(1L);
			given(repository.findById(idCaptor.capture())).willReturn(Optional.of(patient));
			
			var returnPatient = service.findById(1L);
			
			var captor = idCaptor.getValue();
			
			assertEquals(returnPatient.getId(), captor);
		}
	}

	@Nested
	class FindAll {
		
		@Test
		@DisplayName("When FindAll should return list of Patients")
		void when_FindAll_ShouldReturnListOfPatients() {
			
			var patient2 = new Patient("Maria", LocalDate.of(1990, 11, 2), "Female", "maria@email.com", "88888-8888");
			given(repository.findAll()).willReturn(List.of(patient,patient2));
			
			var list = service.findAll();
			
			assertEquals(2, list.size());
			
			var firstPatient = list.get(0);
			
			assertEquals("Pedro ivo", firstPatient.getName());
			assertEquals(date, firstPatient.getBirthday());
			assertEquals("Male", firstPatient.getGender());
			assertEquals("email@email.com", firstPatient.getEmail());
			assertEquals("99999-9999", firstPatient.getPhone());
			
			var secondPatient = list.get(1);
			
			assertEquals("Maria", secondPatient.getName());
			assertEquals(LocalDate.of(1990, 11, 2), secondPatient.getBirthday());
			assertEquals("Female", secondPatient.getGender());
			assertEquals("maria@email.com", secondPatient.getEmail());
			assertEquals("88888-8888", secondPatient.getPhone());
			
			
		}
	
	}

	@Nested
	class SavePatient {
		
		@Test
		@DisplayName("Given Patient object when SavePatient should return Patient")
		void givenPatientObject_When_SavePatient_ShouldReturnPatient() {
			
			patient.setId(1L);
			given(repository.save(any(Patient.class))).willReturn(patient);
			
			var returnPatient = service.savePatient(patient);
			
			assertNotNull(returnPatient);
			
			assertTrue(returnPatient.getId() > 0);
			assertEquals(patient.getName(), returnPatient.getName());
			assertEquals(patient.getBirthday(), returnPatient.getBirthday());
			assertEquals(patient.getGender(), returnPatient.getGender());
			assertEquals(patient.getPhone(), returnPatient.getPhone());
			assertEquals(patient.getEmail(), returnPatient.getEmail());
			
		}
		
		@Test
		@DisplayName("Given PatientCaptor when SavePatient should capture the same Patient")
		void givenPatientCaptor_When_SavePatient_ShouldCaptureTheSamePatient() {
			
			given(repository.save(patientCaptor.capture())).willReturn(patient);
			
			var returnPatient = service.savePatient(patient);
			var capturePatient = patientCaptor.getValue();
			
			assertNotNull(returnPatient);
			
			assertEquals(capturePatient.getName(), returnPatient.getName());
			assertEquals(capturePatient.getBirthday(), returnPatient.getBirthday());
			assertEquals(capturePatient.getGender(), returnPatient.getGender());
			assertEquals(capturePatient.getPhone(), returnPatient.getPhone());
			assertEquals(capturePatient.getEmail(), returnPatient.getEmail());
			
		}
		@Test
		@DisplayName("Given Patient with email or phone in use when SavePatient should throw InformationInUseException")
		void givenPatientWithEmailOrPhoneInUse_When_SavePatient_ShouldThrowInformationInUseException() {
			given(repository.save(any(Patient.class))).willThrow(DataIntegrityViolationException.class);
			
			var content = assertThrows(InformationInUseException.class, 
					() -> {
						service.savePatient(patient);},
					()-> "Exception InformationInUseException not throw");
			
			var exceptionMessage = "Email ou numero de telefone já esta cadastrado no sistema";
			
			assertEquals(exceptionMessage, content.getMessage());
		}
	}

	@Nested
	class DeletePatientById {

		@Test
		@DisplayName("Given Patient id when DeletePatientById should delete Patient")
		void givenPatientId_When_DeletePatientById_ShouldDeletePatient() {
			
			given(repository.findById(anyLong())).willReturn(Optional.of(patient));
			
			service.deletePatientById(anyLong());
			
			verify(repository, times(1)).deleteById(anyLong());
			
		}
		
		@Test
		@DisplayName("Given inexist Id when DeletePatientById should throw EntityNotFoundException")
		void givenInexistPatientId_When_DeletePatientById_ShouldThrowEntityNotFoundException() {
			
			given(repository.findById(anyLong())).willReturn(Optional.empty());
			
			var content = assertThrows(EntityNotFoundException.class,
					()-> {service.deletePatientById(1L);},
					()-> "EntityNotFoundException not throw");
			
			verify(repository, never()).deleteById(anyLong());
			
			assertEquals("Paciente de id 1 não foi encontrado", content.getMessage());
			
		}
	}

	@Nested
	class checkInformationAndSave {
		
		@Test
		@DisplayName("Given Patient object with email in use when SavePatient should throw InformationInUseException")
		void givenPatientObjectWithEmailInUse_When_SavePatient_ShouldThrowInformationInUseException() {
			
			var newPatient = new Patient("Maria", LocalDate.of(1990, 11, 2), "Female", "email@email.com", "88888-8888");
			
			given(repository.existsByEmail(anyString())).willReturn(true);
			given(repository.existsByPhone(anyString())).willReturn(false);
			
			var content = assertThrows(InformationInUseException.class, 
					() ->{service.checkInformationAndSave(newPatient);},
					
					()-> "Exception InformationInUseException not throw");
			
			assertEquals("Email: " + newPatient.getEmail() + " já esta cadastrado no sistema", content.getMessage());
		}
		
		@Test
		@DisplayName("Given Patient object with phone in use when SavePatient should throw InformationInUseException")
		void givenPatientObjectWithPhoneInUse_When_SavePatient_ShouldThrowInformationInUseException() {
			
			var newPatient = new Patient("Maria", LocalDate.of(1990, 11, 2), "Female", "email@email.com", "88888-8888");
			
			given(repository.existsByEmail(anyString())).willReturn(false);
			given(repository.existsByPhone(anyString())).willReturn(true);
			
			var content = assertThrows(InformationInUseException.class, 
					() ->{service.checkInformationAndSave(newPatient);},
					
					()-> "Exception InformationInUseException not throw");
			
			assertEquals("Telefone: " + newPatient.getPhone() + " já esta cadastrado no sistema", content.getMessage());
		}
		
	}
}
