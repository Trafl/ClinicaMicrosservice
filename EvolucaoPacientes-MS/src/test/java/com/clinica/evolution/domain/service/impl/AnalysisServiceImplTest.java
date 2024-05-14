package com.clinica.evolution.domain.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.clinica.evolution.domain.exceptions.AnalysisNotFoundException;
import com.clinica.evolution.domain.model.Analysis;
import com.clinica.evolution.domain.model.Doctor;
import com.clinica.evolution.domain.model.Evolution;
import com.clinica.evolution.domain.model.Patient;
import com.clinica.evolution.domain.model.Procedure;
import com.clinica.evolution.domain.repository.AnalysisRepository;

@ExtendWith(MockitoExtension.class)
class AnalysisServiceImplTest {

	@Mock
	private AnalysisRepository repository;
	
	@InjectMocks
	private AnalysisServiceImpl service;
	
	private Analysis analysis;
	private Doctor doctor;
	private Patient patient;
	private Procedure procedure;
	
	@BeforeEach
	void setUp() {
		doctor = new Doctor(1L, "Doctor");
		patient= new Patient(1L, "Patient");
		procedure = new Procedure(1L, "Procedure");
		
		analysis = Analysis.builder()
						.id("testID")
						.appointmentId(1L)
						.doctor(doctor)
						.patient(patient)
						.procedure(procedure)
						.date(LocalDateTime.of(2024, 5, 12, 14, 30))
						.evolutions(new ArrayList<Evolution>())
						.build();
	}
	
	@Nested
	class getOneAnalysisByIdAndDoctorId{
		
		@Test
		@DisplayName("Given AnalysisId and DoctorId when find by id should return AnalisysObject")
		void given_AnalysisIdAndDoctorId_When_findById_shouldReturnAnalisysObject() {
			
			given(repository.findAnalysisByIdAndDoctorId(anyString(), anyLong())).willReturn(analysis);
			
			var checkAnalysis = service.getOneAnalysisByIdAndDoctorId(anyString(), anyLong());
			
			assertNotNull(checkAnalysis);
			assertEquals(checkAnalysis.getDoctor(), analysis.getDoctor());
			assertEquals(checkAnalysis.getPatient(), analysis.getPatient());
			assertEquals(checkAnalysis.getProcedure(), analysis.getProcedure());
		}
		
		
		@Test
		@DisplayName("Given incorrect AnalysisId and DoctorId when find by id should throw AnalysisNotFoundException")
		void given_IncorrectAnalysisIdAndDoctorId_When_findById_ShouldThrowAnalysisNotFoundException() {
			
			given(repository.findAnalysisByIdAndDoctorId(anyString(), anyLong())).willThrow(NullPointerException.class);

			var content = assertThrows(AnalysisNotFoundException.class, 
					()->{service.getOneAnalysisByIdAndDoctorId("test", 2L);},
					()-> "Not throw the AnalysisNotFoundException");
			
			assertEquals("Prontuario de id: test, não foi encontrado no banco de dados.", content.getMessage());
		}
	}
	
	@Nested
	class findAnalysisByDoctorId {
		
		@Test
		@DisplayName("Given DoctorId when findById should return Page of Analysis")
		void given_DoctorId_When_findById_Should_returnPageOfAnalysis() {
	
			Analysis analysis2 = createdAnalysis2();
			List<Analysis> list = new ArrayList<>(); 
			list.add(analysis);
			list.add(analysis2);
			
			Pageable pageable = Pageable.ofSize(10);
			
			PageImpl<Analysis> analysPage = new PageImpl<>(list, pageable, list.size());
			
			given(repository.getAnalysisByDoctorId(anyLong(), any())).willReturn(analysPage);
			
			var page = service.findAnalysisByDoctorId(1L, pageable);
			assertTrue(page instanceof Page);
			
			var analysisList = page.getContent();
			assertEquals(2, analysisList.size());
			
			var analisysyInList = analysisList.get(0);
			
			assertEquals(analisysyInList.getAppointmentId(), analysis.getAppointmentId());
			assertEquals(analisysyInList.getDoctor(), analysis.getDoctor());
			assertEquals(analisysyInList.getPatient(), analysis.getPatient());
			assertEquals(analisysyInList.getProcedure(), analysis.getProcedure());
			assertEquals(analisysyInList.getDate(), analysis.getDate());

			var analisysyInList2 = analysisList.get(1);
			
			assertEquals(analisysyInList2.getAppointmentId(), analysis2.getAppointmentId());
			assertEquals(analisysyInList2.getDoctor(), analysis2.getDoctor());
			assertEquals(analisysyInList2.getPatient(), analysis2.getPatient());
			assertEquals(analisysyInList2.getProcedure(), analysis2.getProcedure());
			assertEquals(analisysyInList2.getDate(), analysis2.getDate());
			
		}
	}

	@Nested
	class addEvolutionInAnalysisById {
		
		@Test
		@DisplayName("Given evolution object and AnalysisId and DoctorId when addEvolution should return list of Evolution")
		void  given_evolutionObjectAndAnalysisIdAndDoctorId_When_addEvolution_shouldReturnListOfEvolution() {
			
			Evolution evolution = new Evolution();
			evolution.setDayOfEvolution(LocalDateTime.of(2024, 6, 5, 14, 30));
			evolution.setText("test");
			given(repository.findAnalysisByIdAndDoctorId(anyString(), anyLong())).willReturn(analysis);
			
			var listOfEvolutions = service.addEvolutionInAnalysisById("test1", 1L, evolution);
			
			assertNotNull(listOfEvolutions);
			assertEquals(1, listOfEvolutions.size());
			
			var evolution1 = listOfEvolutions.get(0);
			
			assertEquals(evolution.getDayOfEvolution(), evolution1.getDayOfEvolution());
			assertEquals(evolution.getText(), evolution1.getText());
		}
		
	}
	
	@Nested
	class findAnalysisByPatientNameAndDoctorId {
		
		@Test
		@DisplayName("Given Patient name and DoctorId when findAnalysis should return page of Analysis")
		void given_PatientNameAndDoctorId_When_findAnalysis_shouldReturnPageOfAnalysis() {
			
			List<Analysis> list = new ArrayList<>(); 
			list.add(analysis);
			Pageable pageable = Pageable.ofSize(10);
			PageImpl<Analysis> page = new PageImpl<>(list, pageable, list.size());
			
			given(repository.findAnalysisByPatientNameAndDoctorId(anyString(), anyLong(), any())).willReturn(page);
			
			
			var analysisPage = service.findAnalysisByPatientNameAndDoctorId("Miquel", 1L, pageable);
			
			assertTrue(analysisPage instanceof Page);
			assertNotNull(analysisPage);
			
			var analysisList = analysisPage.getContent();
			assertEquals(1, analysisList.size());
			
			var analisysyInList = analysisList.get(0);
			
			assertEquals(analisysyInList.getAppointmentId(), analysis.getAppointmentId());
			assertEquals(analisysyInList.getDoctor(), analysis.getDoctor());
			assertEquals(analisysyInList.getPatient(), analysis.getPatient());
			assertEquals(analisysyInList.getProcedure(), analysis.getProcedure());
			assertEquals(analisysyInList.getDate(), analysis.getDate());
		}
	}
	
	
	
	private Analysis createdAnalysis2() {
	var doctor2 = new Doctor(2L, "Doctor2");
	var patient2= new Patient(2L, "Patient2");
	var procedure2 = new Procedure(2L, "Procedure2");
	
	return Analysis.builder()
			.id("testID2")
			.appointmentId(2L)
			.doctor(doctor2)
			.patient(patient2)
			.procedure(procedure2)
			.date(LocalDateTime.of(2024, 5, 10, 10, 30))
			.evolutions(new ArrayList<Evolution>())
			.build();
	}
}
