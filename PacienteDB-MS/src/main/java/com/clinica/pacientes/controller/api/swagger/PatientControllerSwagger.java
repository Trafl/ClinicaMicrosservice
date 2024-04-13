package com.clinica.pacientes.controller.api.swagger;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.clinica.pacientes.domain.dto.PatientDTOInput;
import com.clinica.pacientes.domain.dto.PatientDTOOutput;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Patients", description = "Patient Manager")
public interface PatientControllerSwagger {

	@Operation(summary = "List patients", description = "Lists patients registered in the database.")
	public ResponseEntity<Page<PatientDTOOutput>> findAllPatients(Pageable pageble);
	
	 @Operation(summary = "Search a patient by ID", description = "Searches for a patient registered in the database.",
			 responses = {
					 @ApiResponse(responseCode = "200"),
					 	  
					 @ApiResponse(responseCode = "400", description = "Invalid patient ID",
					 	  content = @Content(schema = @Schema(ref = "ProblemDetail"))),
					 	  
				 	 @ApiResponse(responseCode = "404", description = "Patient not found",
				 	 content = @Content(schema = @Schema(ref = "ProblemDetail")))
			})
	public ResponseEntity<PatientDTOOutput> findPatientById(Long patientId);
	
	 @Operation(summary = "Register a patient", description = "Registers a patient in the database.",
				responses = {
						@ApiResponse(responseCode = "400", description = "Error validating the fields entered",
							 	  content = @Content(schema = @Schema(ref = "ProblemDetail")))
				})
	public ResponseEntity<PatientDTOOutput> createPatient(PatientDTOInput dtoInput);
	
	 @Operation(summary = "Update a patient", description = "Updates a patient in the database.",
			 responses = {
					  @ApiResponse(responseCode = "200"),
				 	  
					  @ApiResponse(responseCode = "400", description = "Error validating the fields entered",
				 	  content = @Content(schema = @Schema(ref = "ProblemDetail"))),
				 	  
				 	  @ApiResponse(responseCode = "404", description = "Patient not found",
				 	  content = @Content(schema = @Schema(ref = "ProblemDetail")))
		})
	public ResponseEntity<PatientDTOOutput> updatePatient(Long patientId, PatientDTOInput dtoInput);
	
	 @Operation(summary = "Deletes a patient", description = "Deletes a patient from the database.",
			 responses = {
					 @ApiResponse(responseCode = "204"),
				 	  
					 @ApiResponse(responseCode = "400", description = "Error validating the fields entered",
				 	  content = @Content(schema = @Schema(ref = "ProblemDetail"))),
				 	  
				 	  @ApiResponse(responseCode = "404", description = "Patient not found",
				 	  content = @Content(schema = @Schema(ref = "ProblemDetail"))),
				 	  
				 	 @ApiResponse(responseCode = "500", description = "Internal system error",
				 	  content = @Content(schema = @Schema(ref = "ProblemDetail")))
					})
	public void deletePatientById(Long patientId);
}
