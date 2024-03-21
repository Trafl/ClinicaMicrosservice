package com.clinica.procedimentos.domain.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
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
		void givenArgumentCaptor_WhenFindById_ReturnProcedureObject() {
			given(repository.findById(idCaptor.capture())).willReturn(Optional.of(procedure));
			
			var outProcedure = service.findById(procedure.getId());
			
			var id = idCaptor.getValue();
			
			assertEquals(outProcedure.getId(), id);
			
		}
		
		@Test
		void givenProcedureInexistId_WhenFindById_ThenThrowEntityNotFoundException() {
			
			var id = 1L;
			
			given(repository.findById(id)).willThrow(new EntityNotFoundException(String.format("Procedimento de id %s não foi encontrado", id)));
			
			var message = assertThrows(EntityNotFoundException.class,
					()-> {service.findById(id);},
					()-> "Exception not Throw");
			
			assertEquals("Procedimento de id 1 não foi encontrado", message.getMessage());
		}
		
	}
	
	@Nested
	class findAll{
		
		@Test
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
		void givenProcedureObject_WhenCreateProcedure_ThenReturnProcedureObject() {
			given(repository.save(any(Procedure.class))).willReturn(procedure);
			
			var createdProcedure = service.createProcedure(procedure);
			
			assertNotNull(createdProcedure);
			assertEquals(procedure.getId(), createdProcedure.getId());
			assertEquals(procedure.getName(), createdProcedure.getName());
			assertEquals(procedure.getDescription(), createdProcedure.getDescription());
			assertEquals(procedure.getValue(), createdProcedure.getValue());
		}
		
		@Test
		void givenArgumenteCaptor_WhenCreateProcedure_ThenReturnProcedureObject() {
			
			given(repository.save(procedureCaptor.capture())).willReturn(procedure);
			
			var input = new Procedure();
			input.setId(1L);
			input.setName("procedimento2");
			input.setDescription("procedimento");
			input.setValue(new BigDecimal(200));
			
			var output = service.createProcedure(input);
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
		void givenProcedureId_WhenDeleteProcedureById_ShouldDeleteProcedure(){
			
			given(repository.findById(anyLong())).willReturn(Optional.of(procedure));
			willDoNothing().given(repository).deleteById(procedure.getId());
			
			service.deleteProcedureById(procedure.getId());
			
			verify(repository, times(1)).deleteById(procedure.getId());
			
		}
		
		@Test
		void givenInexistProcedureId_WhenDeleteProcedureById_ShouldDeleteProcedure(){
			
			given(repository.findById(anyLong())).willThrow(
					new EntityNotFoundException(String.format("Procedimento de id %s não foi encontrado", procedure.getId())));
			
			var message = assertThrows(EntityNotFoundException.class,
					() -> {
						service.deleteProcedureById(procedure.getId());
					});
			
			verify(repository, never()).deleteById(procedure.getId());
			
			assertEquals("Procedimento de id 1 não foi encontrado", message.getMessage());
		}
		
	}

}
