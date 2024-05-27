package com.clinica.procedimentos.controller.api.swagger;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.clinica.procedimentos.domain.dto.ProcedureDTOInput;
import com.clinica.procedimentos.domain.dto.ProcedureDTOOutput;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@Tag(name = "Procedures", description = "Procedure Manager")
public interface ProcedureControllerSwagger {

	@Operation(summary = "List the procedures", description = "Lists the procedures registered in the database.")
	public ResponseEntity<Page<ProcedureDTOOutput>> findAllProcedures(Pageable pageable, HttpServletRequest request);
	

	 @Operation(summary = "Search for a procedure by ID", description = "Searches for a procedure registered in the database.",
			 responses = {
					 @ApiResponse(responseCode = "200"),
					 	  
					 @ApiResponse(responseCode = "400", description = "Invalid procedure ID",
					 	  content = @Content(schema = @Schema(ref = "ProblemDetail"))),
					 	  
				 	 @ApiResponse(responseCode = "404", description = "Procedure not found",
				 	 content = @Content(schema = @Schema(ref = "ProblemDetail")))
			})
	public ResponseEntity<ProcedureDTOOutput> findProcedureById(Long procedureId, HttpServletRequest request);
	

	@Operation(summary = "Register a procedure", description = "Registers a procedure in the database.",
			responses = {
					@ApiResponse(responseCode = "400", description = "Error validating the fields entered",
						 	  content = @Content(schema = @Schema(ref = "ProblemDetail")))
			})
	public ResponseEntity<ProcedureDTOOutput> createProcedure(ProcedureDTOInput dtoInput, HttpServletRequest request);
	

	@Operation(summary = "Update a procedure", description = "Updates a procedure in the database.",
			 responses = {
					  @ApiResponse(responseCode = "200"),
				 	  
					  @ApiResponse(responseCode = "400", description = "Error validating the fields entered",
				 	  content = @Content(schema = @Schema(ref = "ProblemDetail"))),
				 	  
				 	  @ApiResponse(responseCode = "404", description = "Procedure not found",
				 	  content = @Content(schema = @Schema(ref = "ProblemDetail")))
		})
	public ResponseEntity<ProcedureDTOOutput> updateProcedure(Long procedureId, ProcedureDTOInput dtoInput, HttpServletRequest request);
	
	 @Operation(summary = "Deletes a procedure", description = "Deletes a procedure in the database.",
			 responses = {
					 @ApiResponse(responseCode = "204"),
				 	  
					 @ApiResponse(responseCode = "400", description = "Error validating the fields entered",
				 	  content = @Content(schema = @Schema(ref = "ProblemDetail"))),
				 	  
				 	  @ApiResponse(responseCode = "404", description = "Procedures not found",
				 	  content = @Content(schema = @Schema(ref = "ProblemDetail"))),
				 	  
				 	 @ApiResponse(responseCode = "500", description = "Internal system error",
				 	  content = @Content(schema = @Schema(ref = "ProblemDetail")))
					})
	public void deleteProcedureById(Long procedureId, HttpServletRequest request);
}
