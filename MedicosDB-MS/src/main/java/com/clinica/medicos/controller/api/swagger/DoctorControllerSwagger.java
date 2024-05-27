package com.clinica.medicos.controller.api.swagger;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.clinica.medicos.domain.dto.DoctorDTOInput;
import com.clinica.medicos.domain.dto.DoctorDTOOutput;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@Tag(name = "Doctors", description = "Doctor manager")
public interface DoctorControllerSwagger {

	@Operation(summary = "List the doctors", description = "Lists doctors registered in the database.")
	public ResponseEntity<Page<DoctorDTOOutput>> findAllDoctors(Pageable pageable, HttpServletRequest request);
	
	@Operation(summary ="Search for a doctor by ID", description = "Search for a doctor registered in the database.",
			 responses = {
					 @ApiResponse(responseCode = "200"),
					 	  
					 @ApiResponse(responseCode = "400", description = "Invalid doctor ID",
					 	  content = @Content(schema = @Schema(ref = "ProblemDetail"))),
					 	  
				 	 @ApiResponse(responseCode = "404", description = "Doctor not found",
				 	 content = @Content(schema = @Schema(ref = "ProblemDetail")))
			})
	public ResponseEntity<DoctorDTOOutput> findDoctorById(Long doctorId, HttpServletRequest request);
	
	@Operation(summary = "Register a doctor", description = "Register a doctor in the database.",
			responses = {
					@ApiResponse(responseCode = "400", description = "Error validating the fields entered",
						 	  content = @Content(schema = @Schema(ref = "ProblemDetail")))
			})
	public ResponseEntity<DoctorDTOOutput> createDoctor(DoctorDTOInput dtoInput, HttpServletRequest request);

	 @Operation(summary = "Update a doctor", description = "Updates a doctor in the database.",
			 responses = {
					  @ApiResponse(responseCode = "200"),
				 	  
					  @ApiResponse(responseCode = "400", description = "Error validating the fields entered",
				 	  content = @Content(schema = @Schema(ref = "ProblemDetail"))),
				 	  
				 	  @ApiResponse(responseCode = "404", description = "Doctor not found.",
				 	  content = @Content(schema = @Schema(ref = "ProblemDetail")))
		})
	public ResponseEntity<DoctorDTOOutput> updateDoctor(Long doctorId, DoctorDTOInput dtoInput,  HttpServletRequest request);
	
	 @Operation(summary = "Delete a doctor", description = "Deletes a doctor from the database.",
			 responses = {
					 @ApiResponse(responseCode = "204"),
				 	  
					 @ApiResponse(responseCode = "400", description = "Error validating the fields entered",
				 	  content = @Content(schema = @Schema(ref = "ProblemDetail"))),
				 	  
				 	  @ApiResponse(responseCode = "404", description = "Doctor not found",
				 	  content = @Content(schema = @Schema(ref = "ProblemDetail"))),
				 	  
				 	 @ApiResponse(responseCode = "500", description = "Internal system error",
				 	  content = @Content(schema = @Schema(ref = "ProblemDetail")))
					})
	public void deleteDoctorById( Long doctorId,  HttpServletRequest request);
}
