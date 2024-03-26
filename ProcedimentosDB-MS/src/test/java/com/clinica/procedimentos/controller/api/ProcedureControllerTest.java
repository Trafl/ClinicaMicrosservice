package com.clinica.procedimentos.controller.api;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.assertj.core.util.Arrays;
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
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

import com.clinica.procedimentos.domain.model.Procedure;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	private static Procedure procedure;

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", mysql::getJdbcUrl);
		registry.add("spring.datasource.username", mysql::getUsername);
		registry.add("spring.datasource.password", mysql::getPassword);
	}

	@BeforeAll
	static void beforeAll() {
		mysql.start();
		procedure = new Procedure("Limpeza de pele", "Procedimento de limpeza de pele", new BigDecimal(200));
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
	@DisplayName("Given Procedure json object When POST CreateProcedure should return Procedure object and HTTPStatusCode code 201")
	void givenProcedureJsonObject_WhenPOST_CreateProcedure_ShouldReturnProcedureObjectAndHTTPStatusCode201()throws JsonMappingException, JsonProcessingException {
		
		var content = given().spec(specification)
				.body(procedure)
				.when()
					.post()
				.then()
					.statusCode(201)
						.extract()
						.body()
						.asString();

		Procedure returnProcedure = objectMapper.readValue(content, Procedure.class);

		assertNotNull(returnProcedure);
		assertTrue(returnProcedure.getId() > 0);
		assertEquals(procedure.getName(), returnProcedure.getName());
		assertEquals(procedure.getDescription(), returnProcedure.getDescription());
		assertEquals(procedure.getValue(), returnProcedure.getValue());
	}

	@Test
	@Order(2)
	@DisplayName("Given incomplete json of Procedure when POST CreateProcedure should return HTTPStatusCode 400")
	void givenIncompleteJsonOfProcedure_WhenPost_CreateProcedure_ShouldReturnHTTPStatusCode402() throws JsonMappingException, JsonProcessingException {
			var incompleteProcedure = new Procedure();
			
			given().spec(specification)
				.body(incompleteProcedure)
			.when()
				.post()
			.then()
				.statusCode(400);
	}
		
	@Test
	@Order(3)
	@DisplayName("When GET FindById should return Procedure object and HTTPStatusCode 200")
	void whenGET_FindById_ShouldReturnProcedureObjectAndHTTPStatusCode200() throws JsonMappingException, JsonProcessingException {
			
		var content = given().spec(specification)
					.pathParam("procedureId", 1L)
				.when()
					.get("/{procedureId}")
				.then()
					.statusCode(200)
						.extract()
							.body()
							.asString();

		Procedure returnProcedure = objectMapper.readValue(content, Procedure.class);

		assertNotNull(returnProcedure);
		assertTrue(returnProcedure.getId() > 0);
		assertEquals(procedure.getName(), returnProcedure.getName());
		assertEquals(procedure.getDescription(), returnProcedure.getDescription());
		assertEquals(procedure.getValue().intValue(), returnProcedure.getValue().intValue());
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
	@DisplayName("When GET FindByAll should return list of Procedure and HTTPStatusCode 200")
	void whenGET_FindByAll_ShouldReturnListOfProcedureAndHTTPStatus200() throws JsonMappingException, JsonProcessingException {
		
		var anotherProcedure = new Procedure("Massagem corporal", "Massagem por todo o corpo", new BigDecimal(150));
		
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
		
		
		Procedure[] procedures = objectMapper.readValue(content, Procedure[].class);
		var list =  Arrays.asList(procedures);
		
		assertNotNull(list);
		assertEquals(2, list.size());
		
		Procedure procedure1 = (Procedure) list.get(0);
		
		assertNotNull(procedure1);
		assertTrue(procedure1.getId() > 0);
		assertEquals(procedure.getName(), procedure1.getName());
		assertEquals(procedure.getDescription(), procedure1.getDescription());
		assertEquals(procedure.getValue().intValue(), procedure1.getValue().intValue());
		
		Procedure procedure2 = (Procedure) list.get(1);
		
		assertNotNull(procedure2);
		assertTrue(procedure2.getId() > 0);
		assertEquals(anotherProcedure.getName(), procedure2.getName());
		assertEquals(anotherProcedure.getDescription(), procedure2.getDescription());
		assertEquals(anotherProcedure.getValue().intValue(), procedure2.getValue().intValue());
		
	}
	
	@Test
	@Order(6)
	@DisplayName("Given Procedure object and id when PUT update should return updated Procedure and HTTPStatusCode 200")
	void givenProcedureObjectAndId_WhenPUT_update_ShouldReturnProcedureUpdatedAndHTTPStatus200() throws JsonMappingException, JsonProcessingException {
		
		var updateProcedure = new Procedure("Remoção de calos", "Procedimento para retirada de calos", new BigDecimal(350));	
		
		var content = given().spec(specification)
					.pathParam("procedureId", 1L)
					.body(updateProcedure)
				.when()
					.put("/{procedureId}")
				.then()
					.statusCode(200)
						.extract()
							.body()
							.asString();

		Procedure returnProcedure = objectMapper.readValue(content, Procedure.class);

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
				.pathParam("procedureId", 2L)
				.when()
					.delete("/{procedureId}")
				.then()
					.statusCode(204);
	}
	
	@Test
	@Order(8)
	@DisplayName("Given inexist id when DELETE should return HTTPStatusCode 404")
	void givenInexistId_When_DELETE_ShouldReturnHTTPStatus404() throws JsonMappingException, JsonProcessingException {
		
		given().spec(specification)
			.pathParam("procedureId", 2L)
		.when()
			.delete("/{procedureId}")
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
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

	}
}
