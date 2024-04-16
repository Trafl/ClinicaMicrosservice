package com.clinica.procedimentos.controller.api;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.Page;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

import com.clinica.procedimentos.core.jackson.PageDeserializer;
import com.clinica.procedimentos.domain.dto.ProcedureDTOInput;
import com.clinica.procedimentos.domain.dto.ProcedureDTOOutput;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProcedureControllerTest {

	@LocalServerPort
	private Integer port;

	static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.28");

	private static RequestSpecification specification;
	private static ObjectMapper objectMapper;
	private static ProcedureDTOInput procedureDtoInput;

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", mysql::getJdbcUrl);
		registry.add("spring.datasource.username", mysql::getUsername);
		registry.add("spring.datasource.password", mysql::getPassword);
	}

	@BeforeAll
	static void beforeAll() {
		mysql.start();
		procedureDtoInput = new ProcedureDTOInput("Limpeza de pele", "Procedimento de limpeza de pele", new BigDecimal(200));
	}

	@AfterAll
	static void afterAll() {
		mysql.stop();
	}

	@BeforeEach
	void setUp() {
		configurations();
	}

	
	@Test
	@Order(1)
	@DisplayName("Given ProcedureDTOInput json object When POST CreateProcedure should return ProcedureDTOOutput and HTTPStatusCode code 201")
	void givenProcedureJsonObject_WhenPOST_CreateProcedure_ShouldReturnProcedureDTOOutputAndHTTPStatusCode201()throws JsonMappingException, JsonProcessingException {
		
		var content = given().spec(specification)
				.body(procedureDtoInput)
				.when()
					.post()
				.then()
					.statusCode(201)
						.extract()
						.body()
						.asString();

		ProcedureDTOOutput returnProcedure = objectMapper.readValue(content, ProcedureDTOOutput.class);
		
		assertNotNull(returnProcedure);
		assertEquals(1L, returnProcedure.getId());
		assertEquals(procedureDtoInput.getName(), returnProcedure.getName());
		assertEquals(procedureDtoInput.getDescription(), returnProcedure.getDescription());
		assertEquals(procedureDtoInput.getValue(), returnProcedure.getValue());
	}

	@Test
	@Order(2)
	@DisplayName("Given incomplete json of ProcedureDTOInput when POST CreateProcedure should return HTTPStatusCode 400")
	void givenIncompleteJsonOfProcedure_WhenPost_CreateProcedure_ShouldReturnHTTPStatusCode402() throws JsonMappingException, JsonProcessingException {
			
		var incompleteProcedure = new ProcedureDTOInput("", "", new  BigDecimal(-1));
			
			given().spec(specification)
				.body(incompleteProcedure)
			.when()
				.post()
			.then()
				.statusCode(400);
	}
		
	@Test
	@Order(3)
	@DisplayName("When GET FindById should return ProcedureDTOOutput object and HTTPStatusCode 200")
	void whenGET_FindById_ShouldReturnProcedureDTOOutputAndHTTPStatusCode200() throws JsonMappingException, JsonProcessingException {
			
		var content = given().spec(specification)
					.pathParam("procedureDtoInputId", 1L)
				.when()
					.get("/{procedureDtoInputId}")
				.then()
					.statusCode(200)
						.extract()
							.body()
							.asString();

		ProcedureDTOOutput returnProcedure = objectMapper.readValue(content, ProcedureDTOOutput.class);

		assertNotNull(returnProcedure);
		assertTrue(returnProcedure.getId() > 0);
		assertEquals(procedureDtoInput.getName(), returnProcedure.getName());
		assertEquals(procedureDtoInput.getDescription(), returnProcedure.getDescription());
		assertEquals(procedureDtoInput.getValue().intValue(), returnProcedure.getValue().intValue());
	}
	
	@Test
	@Order(4)
	@DisplayName("Given inexist id when GET FindById should return HTTPStatus 404")
	void givenInexistId_WhenGET_FindById_ShouldReturnHTTPStatus404() throws JsonMappingException, JsonProcessingException {
		
		given().spec(specification)
				.pathParam("procedureId", 8865L)
				.when()
					.get("/{procedureId}")
				.then()
					.statusCode(404);
	}
	
	@Test
	@Order(5)
	@DisplayName("When GET FindByAll should return list of ProcedureDTOOutput and HTTPStatusCode 200")
	void whenGET_FindByAll_ShouldReturnListOfProcedureAndHTTPStatus200() throws JsonMappingException, JsonProcessingException {
		
		var anotherProcedure = new ProcedureDTOInput("Massagem corporal", "Massagem por todo o corpo", new BigDecimal(150));
		
		given().spec(specification)
		.body(anotherProcedure)
		.when()
			.post();
				
		var content = given().spec(specification)
			.when()
				.get()
			.then()
				.statusCode(200)
					.extract()
					.body()
						.asString();
		
		
		var procedureDtoInputs = objectMapper.readValue(content, Page.class);
		
		var list =  procedureDtoInputs.getContent();
		
		assertNotNull(list);
		assertEquals(2, list.size());
		
		ProcedureDTOOutput procedureDtoOutput1 = (ProcedureDTOOutput) list.get(0);
		
		assertNotNull(procedureDtoOutput1);
		assertTrue(procedureDtoOutput1.getId() > 0);
		assertEquals(procedureDtoInput.getName(), procedureDtoOutput1.getName());
		assertEquals(procedureDtoInput.getDescription(), procedureDtoOutput1.getDescription());
		assertEquals(procedureDtoInput.getValue().intValue(), procedureDtoOutput1.getValue().intValue());
		
		ProcedureDTOOutput procedureDtoOutput2 = (ProcedureDTOOutput) list.get(1);
		
		assertNotNull(procedureDtoOutput2);
		assertTrue(procedureDtoOutput2.getId() > 0);
		assertEquals(anotherProcedure.getName(), procedureDtoOutput2.getName());
		assertEquals(anotherProcedure.getDescription(), procedureDtoOutput2.getDescription());
		assertEquals(anotherProcedure.getValue().intValue(), procedureDtoOutput2.getValue().intValue());
		
	}
	
	@Test
	@Order(6)
	@DisplayName("Given ProcedureDTOInput and id when PUT update should return updated ProcedureDTOOutput and HTTPStatusCode 200")
	void givenProcedureObjectAndId_WhenPUT_update_ShouldReturnProcedureUpdatedAndHTTPStatus200() throws JsonMappingException, JsonProcessingException {
		
		var updateProcedure = new ProcedureDTOInput("Remoção de calos", "Procedimento para retirada de calos", new BigDecimal(350));	
		
		var content = given().spec(specification)
					.pathParam("procedureDtoInputId", 1L)
					.body(updateProcedure)
				.when()
					.put("/{procedureDtoInputId}")
				.then()
					.statusCode(200)
						.extract()
							.body()
							.asString();

		ProcedureDTOOutput returnProcedure = objectMapper.readValue(content, ProcedureDTOOutput.class);

		assertNotNull(returnProcedure);
		assertEquals(1L, returnProcedure.getId());
		assertEquals(updateProcedure.getName(), returnProcedure.getName());
		assertEquals(updateProcedure.getDescription(), returnProcedure.getDescription());
		assertEquals(updateProcedure.getValue().intValue(), returnProcedure.getValue().intValue());
	}
	
	@Test
	@Order(7)
	@DisplayName("Given id when DELETE should return HTTPStatusCode 204")
	void givenId_When_DELETE_ShouldReturnHTTPStatus204() throws JsonMappingException, JsonProcessingException {
		
		given().spec(specification)
				.pathParam("procedureDtoInputId", 2L)
				.when()
					.delete("/{procedureDtoInputId}")
				.then()
					.statusCode(204);
	}
	
	@Test
	@Order(8)
	@DisplayName("Given inexist id when DELETE should return HTTPStatusCode 404")
	void givenInexistId_When_DELETE_ShouldReturnHTTPStatus404() throws JsonMappingException, JsonProcessingException {
		
		given().spec(specification)
			.pathParam("procedureDtoInputId", 2L)
		.when()
			.delete("/{procedureDtoInputId}")
		.then()
			.statusCode(404);
	}

	private void configurations() {
		specification = new RequestSpecBuilder().setBasePath("/procedimentos")
				.setPort(port)
				.setContentType(ContentType.JSON)
					.addFilter(new RequestLoggingFilter(LogDetail.ALL))
					.addFilter(new ResponseLoggingFilter(LogDetail.ALL)).build();

		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new SimpleModule().addDeserializer(Page.class, new PageDeserializer<>()));
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

	}
}
