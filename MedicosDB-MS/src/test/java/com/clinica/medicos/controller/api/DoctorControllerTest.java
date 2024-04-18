package com.clinica.medicos.controller.api;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.Page;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

import com.clinica.medicos.core.jackson.PageDeserializer;
import com.clinica.medicos.domain.dto.DoctorDTOInput;
import com.clinica.medicos.domain.dto.DoctorDTOOutput;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DoctorControllerTest {

	@LocalServerPort
	private Integer port;
	
	static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.28");

	private static RequestSpecification specification;
	private static ObjectMapper objectMapper;
	private DoctorDTOInput doctorDtoInput;

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", mysql::getJdbcUrl);
		registry.add("spring.datasource.username", mysql::getUsername);
		registry.add("spring.datasource.password", mysql::getPassword);
	}

	@BeforeAll
	static void beforeAll() {
		mysql.start();
	}
	
	@AfterAll
	static void afterAll() {
		mysql.stop();
	}
	
	@BeforeEach
	void setUp(){
		configurations();
		doctorDtoInput = new DoctorDTOInput("Alan", "alan@email", "123.456.789-09", "CRM/RJ123456", "Physiotherapist");
	}

	@Test
	@Order(1)
	@DisplayName("Given DoctorDTOInput json object when POST CreateDoctor should return DoctorDTOOutput and HTTPStatusCode 201")
	void givenDoctorDTOInputJsonObject_WhenPOST_CreateDoctor_ShouldReturnDoctorDTOOutputAndHTTPStatusCode201() throws JsonMappingException, JsonProcessingException {
		
		var content = given().spec(specification)
				.body(doctorDtoInput)
			.when()
				.post()
			.then()
				.statusCode(201)
				.extract()
					.body()
					.asString();
		
		DoctorDTOOutput returnDoctor = objectMapper.readValue(content, DoctorDTOOutput.class);
		
		assertNotNull(returnDoctor);
		assertEquals(1L, returnDoctor.getId());
		assertEquals(doctorDtoInput.getName(), returnDoctor.getName());
		assertEquals(doctorDtoInput.getCrm(), returnDoctor.getCrm());
		assertEquals(doctorDtoInput.getSpecialty(), returnDoctor.getSpecialty());
			
	}
	
	@Test
	@Order(2)
	@DisplayName("Given DoctorDTOInput json object with same CRM when POST should return HTTPStatusCode 400")
	void givenDoctorDTOInputJsonObjectWithSameCRM_WhenPOST_CreateDoctor_ShouldReturnHTTPStatusCode400() throws JsonMappingException, JsonProcessingException {
		
		var anotherDoctor = new DoctorDTOInput("Alan", "alan@email","123.456.789-09", "CRM/RJ123456", "Physiotherapist");
		
		 given().spec(specification)
				.body(anotherDoctor)
				.when()
					.post()
				.then()
					.statusCode(400);
	}
	
	@Test
	@Order(3)
	@DisplayName("Given incomplete json of DoctorDTOInput when POST CreatePatient should return HTTPStatusCode 400")
	void givenIncompleteJsonOfDoctor_WhenPost_CreateDoctor_ShouldReturnHTTPStatusCode400() throws JsonMappingException, JsonProcessingException {
			
		var incompletePatient = new DoctorDTOInput("", "", "", "", "");
			
			given().spec(specification)
				.body(incompletePatient)
			.when()
				.post()
			.then()
				.statusCode(400);
	}
	
	@Test
	@Order(4)
	@DisplayName("When GET FindById should return DoctorDTOOutput object and HTTPStatusCode 200")
	void whenGET_FindById_ShouldReturnDoctorDTOOutputAndHTTPStatusCode200() throws JsonMappingException, JsonProcessingException {
		
		var content = given().spec(specification)
					.pathParam("doctorDtoInputId", 1L)
				.when()
					.get("/{doctorDtoInputId}")
				.then()
					.statusCode(200)
						.extract()
							.body()
							.asString();

		DoctorDTOOutput returnDoctor = objectMapper.readValue(content, DoctorDTOOutput.class);

		assertNotNull(returnDoctor);
		assertEquals(1L, returnDoctor.getId());
		assertEquals(doctorDtoInput.getName(), returnDoctor.getName());
		assertEquals(doctorDtoInput.getCrm(), returnDoctor.getCrm());
		assertEquals(doctorDtoInput.getSpecialty(), returnDoctor.getSpecialty());
	}
	
	@Test
	@Order(5)
	@DisplayName("Given inexist id when GET FindById should return HTTPStatus 404")
	void givenInexistId_WhenGET_FindById_ShouldReturnHTTPStatus404() throws JsonMappingException, JsonProcessingException {
		
		given().spec(specification)
				.pathParam("doctorId", 8865L)
				.when()
					.get("/{doctorId}")
				.then()
					.statusCode(404);
	}
	
	@Test
	@Order(6)
	@DisplayName("When GET FindByAll should return list of DoctorDTOOutput and HTTPStatusCode 200")
	void whenGET_FindByAll_ShouldReturnListOfPatientAndHTTPStatus200() throws JsonMappingException, JsonProcessingException {
				
		var anotherDoctor = new DoctorDTOInput("Marta","marta@email", "123.456.789-09", "CRM/RJ654321", "Physiotherapist");
		
		given().spec(specification)
		.body(anotherDoctor)
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
		
		var pageOfPatients = objectMapper.readValue(content, Page.class);
		
		var list = pageOfPatients.getContent();
		
		assertNotNull(list);
		assertEquals(2, list.size());
		
		DoctorDTOOutput doctorDtoOutput1 = (DoctorDTOOutput) list.get(0);
		
		assertNotNull(doctorDtoOutput1);
		assertTrue(doctorDtoOutput1.getId() > 0);
		assertEquals(doctorDtoInput.getName(), doctorDtoOutput1.getName());
		assertEquals(doctorDtoInput.getCrm(), doctorDtoOutput1.getCrm());
		assertEquals(doctorDtoInput.getSpecialty(), doctorDtoOutput1.getSpecialty());
		
		
		DoctorDTOOutput doctorDtoOutput2 = (DoctorDTOOutput) list.get(1);
		
		assertNotNull(doctorDtoOutput2);
		assertTrue(doctorDtoOutput2.getId() > 0);
		assertEquals(anotherDoctor.getName(), doctorDtoOutput2.getName());
		assertEquals(anotherDoctor.getCrm(), doctorDtoOutput2.getCrm());
		assertEquals(anotherDoctor.getSpecialty(), doctorDtoOutput2.getSpecialty());
	
	}
	
	@Test
	@Order(7)
	@DisplayName("Given DoctorDTOInput and id when PUT update should return Doctor updated and HTTPStatusCode 200")
	void givenDoctorObjectAndId_WhenPUT_update_ShouldReturnDoctorUpdatedAndHTTPStatus200() throws JsonMappingException, JsonProcessingException {
		
		var updateDoctor = new DoctorDTOInput("Rafael", "rafael@email", "123.456.789-09", "CRM/RJ334512", "Physiotherapist");	
		
		var content = given().spec(specification)
					.pathParam("doctorDtoInputId", 1L)
					.body(updateDoctor)
				.when()
					.put("/{doctorDtoInputId}")
				.then()
					.statusCode(200)
						.extract()
							.body()
							.asString();

		DoctorDTOOutput returnDoctor = objectMapper.readValue(content, DoctorDTOOutput.class);

		assertNotNull(returnDoctor);
		assertEquals(1L, returnDoctor.getId());
		assertEquals(updateDoctor.getName(), returnDoctor.getName());
		assertEquals(updateDoctor.getCrm(), returnDoctor.getCrm());
		assertEquals(updateDoctor.getSpecialty(), returnDoctor.getSpecialty());
		
	}
	
	@Test
	@Order(8)
	@DisplayName("Given Doctor object with same CRM when PUT update should return HTTPStatusCode 400")
	void givenDoctorObjectWithSameCRM_WhenPUT_update_ShouldReturnHTTPStatus400() throws JsonMappingException, JsonProcessingException {
		
		var updateDoctor = new DoctorDTOInput("Max", "max@email", "123.456.789-09", "CRM/RJ334512", "Physiotherapist");	
		
		 given().spec(specification)
					.pathParam("doctorDtoInputId", 1L)
					.body(updateDoctor)
				.when()
					.put("/{doctorDtoInputId}")
				.then()
					.statusCode(400);
	}
	
	@Test
	@Order(10)
	@DisplayName("Given id when DELETE should return HTTPStatusCode 204")
	void givenId_When_DELETE_ShouldReturnHTTPStatus204() throws JsonMappingException, JsonProcessingException {
		
		given().spec(specification)
				.pathParam("doctorDtoInputId", 1L)
				.when()
					.delete("/{doctorDtoInputId}")
				.then()
					.statusCode(204);
	}
	
	@Test
	@Order(11)
	@DisplayName("Given inexist id when DELETE should return HTTPStatusCode 404")
	void givenInexistId_When_DELETE_ShouldReturnHTTPStatus404() throws JsonMappingException, JsonProcessingException {
		
		given().spec(specification)
			.pathParam("doctorDtoInputId", 1L)
		.when()
			.delete("/{doctorDtoInputId}")
		.then()
			.statusCode(404);
	}
	
	private void configurations() {
		specification = new RequestSpecBuilder().setBasePath("/medicos")
				.setPort(port)
				.setContentType(ContentType.JSON)
					.addFilter(new RequestLoggingFilter(LogDetail.ALL))
					.addFilter(new ResponseLoggingFilter(LogDetail.ALL)).build();

		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.registerModule(new SimpleModule().addDeserializer(Page.class, new PageDeserializer<>()));
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

	}

}
