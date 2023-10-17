package com.clinica.medicos.core.openapi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ProblemDetail;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;

@Configuration
public class SpringDocConfig {

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("MedicoDB-MS")
						.version("v1")
						.description("Microsservice responsavel pelo banco de dados dos medicos"))
						.components(new Components()
			                    .addSchemas("ProblemDetail", new Schema<ProblemDetail>()
			                            .type("object")
			                            .addProperty("type", new StringSchema().example("https://clinicas.com/errors/entity-not-found"))
			                            .addProperty("title", new StringSchema().example("Paciente não registrado"))
			                            .addProperty("status", new StringSchema().example(404))
			                            .addProperty("detail", new StringSchema().example("Medico de id 5 não foi encontrado"))
			                            .addProperty("timestamp", new StringSchema().example("2023-10-16T19:32:54.253417400Z"))
										));			
	}

	 	
}
