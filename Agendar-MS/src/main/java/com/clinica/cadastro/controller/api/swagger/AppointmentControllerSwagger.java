package com.clinica.cadastro.controller.api.swagger;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.clinica.cadastro.domain.dto.input.MedicalAppointmentDTOInput;
import com.clinica.cadastro.domain.dto.output.MedicalAppointmentOutPut;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "To schedule", description = "Schedule Manager")
public interface AppointmentControllerSwagger {

	@Operation(summary = "List all medical appointments", description = "Lists all medical appointments registered in the database.")
	public ResponseEntity<Page<MedicalAppointmentOutPut>> getAllAppointment(Pageable pageable);

	@Operation(summary = "List all medical appointments", description = "Lists all medical appointments scheduled in the database.")
	public ResponseEntity<Page<MedicalAppointmentOutPut>> getScheduledAppointment(Pageable pageable);
	
	@Operation(summary = "List completed medical appointments", description = "Lists completed medical appointments recorded in the database.")
	public ResponseEntity<Page<MedicalAppointmentOutPut>> getFinishedAppointment(Pageable pageable);
	
	@Operation(summary = "List canceled medical appointments", description = "Lists canceled medical appointments registered in the database.")
	public ResponseEntity<Page<MedicalAppointmentOutPut>> getCancelAppointment(Pageable pageable);
	
	@Operation(summary = "Search for a medical appointment by ID", description = "Search for a medical appointment registered in the database.",
			 responses = {
					 @ApiResponse(responseCode = "200"),
					 	  
					 @ApiResponse(responseCode = "400", description = "Invalid medical appointment ID",
					 	  content = @Content(schema = @Schema(ref = "ProblemDetail"))),
					 	  
				 	 @ApiResponse(responseCode = "404", description = "Medical appointment not found",
				 	 content = @Content(schema = @Schema(ref = "ProblemDetail")))
			})
	public ResponseEntity<MedicalAppointmentOutPut> getAppointmentById(Long appointmentId);
	
	@Operation(summary = "Register a medical appointment", description = "Registers a medical appointment in the database.",
			responses = {
					@ApiResponse(responseCode = "201"),
					@ApiResponse(responseCode = "400", description = "Error validating the fields entered",
						 	  content = @Content(schema = @Schema(ref = "ProblemDetail")))
			})
	public ResponseEntity<MedicalAppointmentOutPut> createAppointment(MedicalAppointmentDTOInput appointmentDto);
	
	@Operation(summary = "Cancel a medical appointment", description = "Cancels a medical appointment in the database.",
			responses = {
					@ApiResponse(responseCode = "204")
			})
	public void cancelAppointment(Long appointmentId );
	
	@Operation(summary = "Complete a medical appointment", description = "Completes a medical appointment in the database.")
	public void finishAppointment(Long appointmentId );
}
