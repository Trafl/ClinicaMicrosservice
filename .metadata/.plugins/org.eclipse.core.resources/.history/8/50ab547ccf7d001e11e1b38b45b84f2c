package com.clinica.cadastro.controller.api.swagger;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.clinica.cadastro.domain.dto.input.MedicalAppointmentDTOInput;
import com.clinica.cadastro.domain.dto.output.MedicalAppointmentOutPut;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Agendar", description = "Gerenciador de Agendamentos")
public interface AppointmentControllerSwagger {

	@Operation(summary = "Lista todas as consultas", description = "Lista todas consultas registradas no banco de dados.")
	public ResponseEntity<List<MedicalAppointmentOutPut>> getAllAppointment();
	
	@Operation(summary = "Lista as consultas finalizadas", description = "Lista as consultas finalizadas, registradas no banco de dados.")
	public ResponseEntity<List<MedicalAppointmentOutPut>> getFinishedAppointment();
	
	@Operation(summary = "Lista as consultas canceladas", description = "Lista as consultas canceladas registradas no banco de dados.")
	public ResponseEntity<List<MedicalAppointmentOutPut>> getCancelAppointment();
	
	@Operation(summary = "Busca uma consulta por ID", description = "Busca uma consulta registrada no banco de dados.",
			 responses = {
					 @ApiResponse(responseCode = "200"),
					 	  
					 @ApiResponse(responseCode = "400", description = "ID da consulta invalido",
					 	  content = @Content(schema = @Schema(ref = "ProblemDetail"))),
					 	  
				 	 @ApiResponse(responseCode = "404", description = "Consulta não encontrada",
				 	 content = @Content(schema = @Schema(ref = "ProblemDetail")))
			})
	public ResponseEntity<MedicalAppointmentOutPut> getAppointmentById(Long appointmentId);
	
	@Operation(summary = "Registra uma consulta", description = "Registra uma consulta no banco de dados.",
			responses = {
					@ApiResponse(responseCode = "201"),
					@ApiResponse(responseCode = "400", description = "Erro na validação dos campos informados",
						 	  content = @Content(schema = @Schema(ref = "ProblemDetail")))
			})
	public ResponseEntity<MedicalAppointmentOutPut> createAppointment(MedicalAppointmentDTOInput appointmentDto);
	
	@Operation(summary = "Cancela uma consulta", description = "Cancela uma consulta no banco de dados.",
			responses = {
					@ApiResponse(responseCode = "204")
			})
	public void cancelAppointment(Long appointmentId );
	
	@Operation(summary = "Finaliza uma consulta", description = "Finaliza uma consulta no banco de dados.")
	public void finishAppointment(Long appointmentId );
}
