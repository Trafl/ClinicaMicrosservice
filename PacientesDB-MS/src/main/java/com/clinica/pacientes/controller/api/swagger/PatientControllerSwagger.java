package com.clinica.pacientes.controller.api.swagger;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.clinica.pacientes.domain.dto.PatientDTOInput;
import com.clinica.pacientes.domain.dto.PatientDTOOutput;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Pacientes", description = "Gerenciador de pacientes")
public interface PatientControllerSwagger {

	@Operation(summary = "Lista os pacientes", description = "Lista os pacientes registrados no banco de dados.")
	public ResponseEntity<List<PatientDTOOutput>> findAllPatients();
	
	 @Operation(summary = "Busca um paciente por ID", description = "Busca um paciente registrado no banco de dados.",
			 responses = {
					 @ApiResponse(responseCode = "200"),
					 	  
					 @ApiResponse(responseCode = "400", description = "ID de paciente invalido",
					 	  content = @Content(schema = @Schema(ref = "ProblemDetail"))),
					 	  
				 	 @ApiResponse(responseCode = "404", description = "Paciente não encontrado",
				 	 content = @Content(schema = @Schema(ref = "ProblemDetail")))
			})
	public ResponseEntity<PatientDTOOutput> findPatientById(Long patientId);
	
	 @Operation(summary = "Registra um paciente", description = "Registra um paciente no banco de dados.",
				responses = {
						@ApiResponse(responseCode = "400", description = "Erro na validação dos campos informados",
							 	  content = @Content(schema = @Schema(ref = "ProblemDetail")))
				})
	public ResponseEntity<PatientDTOOutput> createPatient(PatientDTOInput dtoInput);
	
	 @Operation(summary = "Atualizar um paciente", description = "Atualiza um paciente no banco de dados.",
			 responses = {
					  @ApiResponse(responseCode = "200"),
				 	  
					  @ApiResponse(responseCode = "400", description = "Erro na validação dos campos informados",
				 	  content = @Content(schema = @Schema(ref = "ProblemDetail"))),
				 	  
				 	  @ApiResponse(responseCode = "404", description = "Paciente não encontrado",
				 	  content = @Content(schema = @Schema(ref = "ProblemDetail")))
		})
	public ResponseEntity<PatientDTOOutput> updatePatient(Long patientId, PatientDTOInput dtoInput);
	
	 @Operation(summary = "Deleta um paciente", description = "Deleta um paciente no banco de dados.",
			 responses = {
					 @ApiResponse(responseCode = "204"),
				 	  
					 @ApiResponse(responseCode = "400", description = "Erro na validação dos campos informados ",
				 	  content = @Content(schema = @Schema(ref = "ProblemDetail"))),
				 	  
				 	  @ApiResponse(responseCode = "404", description = "Paciente não encontrado",
				 	  content = @Content(schema = @Schema(ref = "ProblemDetail"))),
				 	  
				 	 @ApiResponse(responseCode = "500", description = "Erro interno de sistema",
				 	  content = @Content(schema = @Schema(ref = "ProblemDetail")))
					})
	public void deletePatientById(Long patientId);
}