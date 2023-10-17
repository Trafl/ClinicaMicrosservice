package com.clinica.medicos.controller.api.swagger;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.clinica.medicos.domain.dto.DoctorDTOInput;
import com.clinica.medicos.domain.dto.DoctorDTOOutput;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Medicos", description = "Gerenciador de medicos")
public interface DoctorControllerSwagger {

	@Operation(summary = "Lista os medicos", description = "Lista os medicos registrados no banco de dados.")
	public ResponseEntity<List<DoctorDTOOutput>> findAllDoctors();
	
	@Operation(summary = "Busca um medico por ID", description = "Busca um medico registrado no banco de dados.",
			 responses = {
					 @ApiResponse(responseCode = "200"),
					 	  
					 @ApiResponse(responseCode = "400", description = "ID de medico invalido",
					 	  content = @Content(schema = @Schema(ref = "ProblemDetail"))),
					 	  
				 	 @ApiResponse(responseCode = "404", description = "Medico não encontrado",
				 	 content = @Content(schema = @Schema(ref = "ProblemDetail")))
			})
	public ResponseEntity<DoctorDTOOutput> findDoctorById( Long doctorId);
	
	@Operation(summary = "Registra um medico", description = "Registra um medico no banco de dados.",
			responses = {
					@ApiResponse(responseCode = "400", description = "Erro na validação dos campos informados",
						 	  content = @Content(schema = @Schema(ref = "ProblemDetail")))
			})
	public ResponseEntity<DoctorDTOOutput> createDoctor( DoctorDTOInput dtoInput);

	 @Operation(summary = "Atualizar um medico", description = "Atualiza um medico no banco de dados.",
			 responses = {
					  @ApiResponse(responseCode = "200"),
				 	  
					  @ApiResponse(responseCode = "400", description = "Erro na validação dos campos informados",
				 	  content = @Content(schema = @Schema(ref = "ProblemDetail"))),
				 	  
				 	  @ApiResponse(responseCode = "404", description = "Medico não encontrado",
				 	  content = @Content(schema = @Schema(ref = "ProblemDetail")))
		})
	public ResponseEntity<DoctorDTOOutput> updateDoctor(Long doctorId, DoctorDTOInput dtoInput);
	
	 @Operation(summary = "Deleta um medico", description = "Deleta um medico no banco de dados.",
			 responses = {
					 @ApiResponse(responseCode = "204"),
				 	  
					 @ApiResponse(responseCode = "400", description = "Erro na validação dos campos informados ",
				 	  content = @Content(schema = @Schema(ref = "ProblemDetail"))),
				 	  
				 	  @ApiResponse(responseCode = "404", description = "Medico não encontrado",
				 	  content = @Content(schema = @Schema(ref = "ProblemDetail"))),
				 	  
				 	 @ApiResponse(responseCode = "500", description = "Erro interno de sistema",
				 	  content = @Content(schema = @Schema(ref = "ProblemDetail")))
					})
	public void deleteDoctorById( Long doctorId);
}
