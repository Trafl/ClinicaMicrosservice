package com.clinica.procedimentos.core.openapi;

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
	OpenAPI openAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("ProceduresDB-MS")
						.version("v1")
						.description("Microservice responsible for storage and consultation on procedures"))
						.components(new Components()
                    .addSchemas("ProblemDetail", new Schema<ProblemDetail>()
                            .type("object")
                            .addProperty("type", new StringSchema().example("https://clinicas.com/errors/entity-not-found"))
                            .addProperty("title", new StringSchema().example("Unregistered procedure"))
                            .addProperty("status", new StringSchema().example(404))
                            .addProperty("detail", new StringSchema().example("Procedure id 5 was not found"))
                            .addProperty("timestamp", new StringSchema().example("2023-10-16T19:32:54.253417400Z"))
							));			
	}
}
