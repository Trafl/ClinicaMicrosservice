package com.clinica.procedimentos.domain.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.ArrayList;
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

import com.clinica.procedimentos.domain.exception.EntityNotFoundException;
import com.clinica.procedimentos.domain.model.Procedure;
import com.clinica.procedimentos.domain.repository.ProcedureRepository;

@ExtendWith(MockitoExtension.class)
class ProcedureServiceImplTest {

	@Mock
	private ProcedureRepository repository;
	
	@InjectMocks
	private ProcedureServiceImpl service;
	
	@Captor
	private ArgumentCaptor<Procedure> procedureCaptor;
	
	@Captor
	private ArgumentCaptor<Long> idCaptor;
	
	private Procedure procedure;
	
	@BeforeEach
	void setup() {
		procedure = new Procedure();
		procedure.setId(1L);
		procedure.setName("Lipo");
		procedure.setDescription("Procedimento Estetico");
		procedure.setValue(new BigDecimal(150));
	}
	
	@Nested
	class findById{
		
		@Test
		@DisplayName("Given Procedure id when FindById return Procedure object")
		void givenProcedureId_WhenFindById_ReturnProcedureObject() {
			
			given(repository.findById(anyLong())).willReturn(Optional.of(procedure));
			
			var newProcedure = service.findById(anyLong());
			
			assertNotNull(newProcedure);
			assertEquals(1L, newProcedure.getId());
			assertEquals("Lipo", newProcedure.getName());
			assertEquals("Procedimento Estetico", newProcedure.getDescription());
			assertEquals(new BigDecimal(150), newProcedure.getValue());
			
		}
		
		@Test
		@DisplayName("Given id and ArgumentCaptor when FindById Return same argument")
		void givenIdAndArgumentCaptor_WhenFindById_ReturnSameArgument() {
			
			given(repository.findById(idCaptor.capture())).willReturn(Optional.of(procedure));
			
			var outProcedure = service.findById(procedure.getId());
			
			var id = idCaptor.getValue();
			
			assertEquals(outProcedure.getId(), id);
			
		}
		
		@Test
		@DisplayName("Given inexist Procedure id when FindById should throw EntityNotFoundException")
		void givenInexistProcedureId_When_FindById_ShouldThrowEntityNotFoundException() {
			
			var id = 1L;
			
			Optional.of(Procedure.class);
			given(repository.findById(id)).willReturn(Optional.empty());
			
			var message = assertThrows(EntityNotFoundException.class,
					()-> {service.findById(id);},
					()-> "Exception not Throw");
			
			assertEquals("Procedure id 1 was not found", message.getMessage());
		}
		
	}
	
	@Nested
	class findAll{
		
		@Test
		@DisplayName("When FindAll return list of Procedure object")
		void whenFindAll_ReturnListOfProcedureObject() {
			
			List<Procedure> list = new ArrayList<>();
			var procedure1 = new Procedure();
			var procedure2 = new Procedure();
			list.add(procedure);
			list.add(procedure1);
			list.add(procedure2);
			
			given(repository.findAll()).willReturn(list);
			
			var listOfProcedure = service.findAll();
			
			assertNotNull(listOfProcedure);
			assertEquals(3, listOfProcedure.size());
			
		}
	}
	
	@Nested
	class createProcedure{
		
		@Test
		@DisplayName("Given Procedure object  when CreateProcedure should return ProcedureObject")
		void givenProcedureObject_WhenCreateProcedure_ShouldReturnProcedureObject() {
			
			given(repository.save(any(Procedure.class))).willReturn(procedure);
			
			var createdProcedure = service.saveProcedure(procedure);
			
			assertNotNull(createdProcedure);
			assertEquals(procedure.getId(), createdProcedure.getId());
			assertEquals(procedure.getName(), createdProcedure.getName());
			assertEquals(procedure.getDescription(), createdProcedure.getDescription());
			assertEquals(procedure.getValue(), createdProcedure.getValue());
		}
		
		@Test
		@DisplayName("Given Procedure object and ArgumenteCaptor when CreateProcedure should return Procedure object")
		void givenProcedureObjectAndArgumenteCaptor_WhenCreateProcedure_ShouldReturnSameProcedureObject() {
			
			given(repository.save(procedureCaptor.capture())).willReturn(procedure);
			
			var input = new Procedure();
			input.setId(1L);
			input.setName("procedimento2");
			input.setDescription("procedimento");
			input.setValue(new BigDecimal(200));
			
			var output = service.saveProcedure(input);
			var captor = procedureCaptor.getValue();
			
			assertNotNull(output);
			assertEquals(input.getId(), captor.getId());
			assertEquals(input.getName(), captor.getName());
			assertEquals(input.getDescription(), captor.getDescription());
			assertEquals(input.getValue(), captor.getValue());
		}
	}
	
	@Nested
	class deleteProcedureById{
		
		@Test
		@DisplayName("Given Procedure id when Delete ProcedureById should delete Procedure")
		void givenProcedureId_WhenDeleteProcedureById_ShouldDeleteProcedure(){
			
			given(repository.findById(anyLong())).willReturn(Optional.of(procedure));
			willDoNothing().given(repository).deleteById(procedure.getId());
			
			service.deleteProcedureById(procedure.getId());
			
			verify(repository, times(1)).deleteById(procedure.getId());
			
		}
		
		@Test
		@DisplayName("Given inexist Procedure id when Delete ProcedureById should throw EntityNotFoundException")
		void givenInexistProcedureId_WhenDeleteProcedureById_ShouldDeleteProcedure(){
			
			Optional.of(Procedure.class);
			given(repository.findById(anyLong())).willReturn(Optional.empty());
			
			var message = assertThrows(EntityNotFoundException.class,
					() -> {
						service.deleteProcedureById(procedure.getId());
					});
			
			verify(repository, never()).deleteById(procedure.getId());
			
			assertEquals("Procedure id 1 was not found", message.getMessage());
		}
		
	}

}
