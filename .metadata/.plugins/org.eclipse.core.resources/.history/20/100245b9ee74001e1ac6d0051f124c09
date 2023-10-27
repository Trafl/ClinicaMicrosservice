package com.clinica.cadastro.core.openapi;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
@Configuration
public class SpringDocConfig {

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("Clinica")
						.version("v1")
						.description("Microsservice responsavel pelo cadastro e agendamento de clientes")
						);			
	}

	 	private static final String badRequestResponse = "BadRequestResponse";
	    private static final String notFoundResponse = "NotFoundResponse";
	    private static final String notAcceptableResponse = "NotAcceptableResponse";
	    private static final String internalServerErrorResponse = "InternalServerErrorResponse";
	
	
	@Bean
	    public OpenApiCustomizer openApiCustomiser() {
	        return openApi -> {
	            openApi.getPaths()
	                    .values()
	                    .forEach(pathItem -> pathItem.readOperationsMap()
	                            .forEach((httpMethod, operation) -> {
	                                ApiResponses responses = operation.getResponses();
	                                switch (httpMethod) {
	                                case GET:
                                        responses.addApiResponse("406", new ApiResponse().$ref(notAcceptableResponse));
                                        responses.addApiResponse("500", new ApiResponse().$ref(internalServerErrorResponse));
                                        break;
                                    case POST:
                                        responses.addApiResponse("400", new ApiResponse().$ref(badRequestResponse));
                                        responses.addApiResponse("500", new ApiResponse().$ref(internalServerErrorResponse));
                                        break;
                                    case PUT:
                                        responses.addApiResponse("400", new ApiResponse().$ref(badRequestResponse));
                                        responses.addApiResponse("500", new ApiResponse().$ref(internalServerErrorResponse));
                                        break;
                                    case DELETE:
                                        responses.addApiResponse("500", new ApiResponse().$ref(internalServerErrorResponse));
                                        break;
                                    default:
                                        responses.addApiResponse("500", new ApiResponse().$ref(internalServerErrorResponse));
                                        break;
	                                }
	                            })
	                    );
	        };
	    }
	
}
