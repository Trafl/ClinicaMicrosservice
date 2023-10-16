package com.clinica.procedimentos.controller.api.swagger;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.clinica.procedimentos.domain.dto.ProcedureDTOInput;
import com.clinica.procedimentos.domain.dto.ProcedureDTOOutput;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Procedimentos", description = "Gerenciador de procedimentos")
public interface ProcedureControllerSwagger {

	@Operation(summary = "Lista os procedimentos", description = "Lista os procedimentos registrados no banco de dados.")
	public ResponseEntity<List<ProcedureDTOOutput>> findAllProcedures();
	

	 @Operation(summary = "Busca um procedimento por ID", description = "Busca um procedimento registrado no banco de dados.",
			 responses = {
					 @ApiResponse(responseCode = "200"),
					 	  
					 @ApiResponse(responseCode = "400", description = "ID de procedimentos invalido",
					 	  content = @Content(schema = @Schema(ref = "ProblemDetail"))),
					 	  
				 	 @ApiResponse(responseCode = "404", description = "Procedimento não encontrado",
				 	 content = @Content(schema = @Schema(ref = "ProblemDetail")))
			})
	public ResponseEntity<ProcedureDTOOutput> findProcedureById(@PathVariable Long procedureId);
	

	@Operation(summary = "Registra um procedimento", description = "Registra um procedimento no banco de dados.",
			responses = {
					@ApiResponse(responseCode = "400", description = "Erro na validação dos campos informados",
						 	  content = @Content(schema = @Schema(ref = "ProblemDetail")))
			})
	public ResponseEntity<ProcedureDTOOutput> createProcedure(@RequestBody @Valid ProcedureDTOInput dtoInput);
	

	@Operation(summary = "Atualizar um procedimento", description = "Atualiza um procedimento no banco de dados.",
			 responses = {
					  @ApiResponse(responseCode = "200"),
				 	  
					  @ApiResponse(responseCode = "400", description = "Erro na validação dos campos informados",
				 	  content = @Content(schema = @Schema(ref = "ProblemDetail"))),
				 	  
				 	  @ApiResponse(responseCode = "404", description = "Procedimento não encontrado",
				 	  content = @Content(schema = @Schema(ref = "ProblemDetail")))
		})
	public ResponseEntity<ProcedureDTOOutput> updateProcedure(@PathVariable Long procedureId, @RequestBody @Valid ProcedureDTOInput dtoInput);
	
	 @Operation(summary = "Deleta um procedimento", description = "Deleta um procedimento no banco de dados.",
			 responses = {
					 @ApiResponse(responseCode = "204"),
				 	  
					 @ApiResponse(responseCode = "400", description = "Erro na validação dos campos informados ",
				 	  content = @Content(schema = @Schema(ref = "ProblemDetail"))),
				 	  
				 	  @ApiResponse(responseCode = "404", description = "Procedimentos não encontrado",
				 	  content = @Content(schema = @Schema(ref = "ProblemDetail"))),
				 	  
				 	 @ApiResponse(responseCode = "500", description = "Erro interno de sistema",
				 	  content = @Content(schema = @Schema(ref = "ProblemDetail")))
					})
	public void deleteProcedureById( Long procedureId);
}
