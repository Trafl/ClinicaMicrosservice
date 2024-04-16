package com.clinica.pacientes.controller.api;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

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

import com.clinica.pacientes.core.jackson.PageDeserializer;
import com.clinica.pacientes.domain.dto.PatientDTOInput;
import com.clinica.pacientes.domain.dto.PatientDTOOutput;
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
class PatientControllerTest {

	@LocalServerPort
	private Integer port;
	
	static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.28");

	private static RequestSpecification specification;
	private static ObjectMapper objectMapper;
	private PatientDTOInput patientDtoInput;

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
		patientDtoInput = new PatientDTOInput("Marcos Antonio", LocalDate.of(1998, 02, 11), "Male", "marcos@email.com", "999999-9999");
	}


	@Test
	@Order(1)
	@DisplayName("Given PatientDTOInput json object when POST CreatePatient should return PatientDTOOutput and HTTPStatusCode 201")
	void givenPatientDTOInputJsonObject_WhenPOST_CreatePatient_ShouldReturnPatientDTOOutputAndHTTPStatusCode201() throws JsonMappingException, JsonProcessingException {
		
		var content = given().spec(specification)
				.body(patientDtoInput)
			.when()
				.post()
			.then()
				.statusCode(201)
				.extract()
					.body()
					.asString();
		
		PatientDTOOutput returnPatient = objectMapper.readValue(content, PatientDTOOutput.class);
		
		assertNotNull(returnPatient);
		assertEquals(1L, returnPatient.getId());
		assertEquals(patientDtoInput.getName(), returnPatient.getName());
		assertEquals(patientDtoInput.getGender(), returnPatient.getGender());
		assertEquals(patientDtoInput.getBirthday(), returnPatient.getBirthday());
		assertEquals(patientDtoInput.getEmail(), returnPatient.getEmail());
		assertEquals(patientDtoInput.getPhone(), returnPatient.getPhone());
			
	}
	
	@Test
	@Order(2)
	@DisplayName("Given PatientDTOInput json object with same email when POST should return HTTPStatusCode 400")
	void givenPatientDTOInputJsonObjectWithSameProperties_WhenPOST_CreatePatient_ShouldReturnHTTPStatusCode400() throws JsonMappingException, JsonProcessingException {
		
		PatientDTOInput anotherPatient = new PatientDTOInput("Marcos Fernando", LocalDate.of(1998, 02, 11), "Male", "marcos@email.com", "22222-2222");
		
		 given().spec(specification)
				.body(anotherPatient)
				.when()
					.post()
				.then()
					.statusCode(400);
	}
	
	@Test
	@Order(2)
	@DisplayName("Given PatientDTOInput json object with same phone number when POST should return HTTPStatusCode 400")
	void givenPatientDTOInputJsonObjectWithSamePhoneNumber_WhenPOST_CreatePatient_ShouldReturnHTTPStatusCode400() throws JsonMappingException, JsonProcessingException {
		
		PatientDTOInput anotherPatient = new PatientDTOInput("Marcos Fernando", LocalDate.of(1998, 02, 11), "Male", "mar@email.com", "999999-9999");
		
		given().spec(specification)
				.body(anotherPatient)
				.when()
					.post()
				.then()
					.statusCode(400);
	}
	
	@Test
	@Order(3)
	@DisplayName("Given incomplete json of PatientDTOInput when POST CreatePatient should return HTTPStatusCode 400")
	void givenIncompleteJsonOfProcedure_WhenPost_CreateProcedure_ShouldReturnHTTPStatusCode402() throws JsonMappingException, JsonProcessingException {
			
		var incompletePatient = new PatientDTOInput("", null, "", "", "");
			
			given().spec(specification)
				.body(incompletePatient)
			.when()
				.post()
			.then()
				.statusCode(400);
	}
	
	@Test
	@Order(4)
	@DisplayName("When GET FindById should return PatientDTOOutput object and HTTPStatusCode 200")
	void whenGET_FindById_ShouldReturnPatientDTOOutputAndHTTPStatusCode200() throws JsonMappingException, JsonProcessingException {
		
		var content = given().spec(specification)
					.pathParam("patientDtoInputId", 1L)
				.when()
					.get("/{patientDtoInputId}")
				.then()
					.statusCode(200)
						.extract()
							.body()
							.asString();

		PatientDTOOutput returnPatient = objectMapper.readValue(content, PatientDTOOutput.class);

		assertNotNull(returnPatient);
		assertEquals(1L, returnPatient.getId());
		assertEquals(patientDtoInput.getName(), returnPatient.getName());
		assertEquals(patientDtoInput.getGender(), returnPatient.getGender());
		assertEquals(patientDtoInput.getBirthday(), returnPatient.getBirthday());
		assertEquals(patientDtoInput.getEmail(), returnPatient.getEmail());
		assertEquals(patientDtoInput.getPhone(), returnPatient.getPhone());
	}
	
	@Test
	@Order(5)
	@DisplayName("Given inexist id when GET FindById should return HTTPStatus 404")
	void givenInexistId_WhenGET_FindById_ShouldReturnHTTPStatus404() throws JsonMappingException, JsonProcessingException {
		
		given().spec(specification)
				.pathParam("patientId", 8865L)
				.when()
					.get("/{patientId}")
				.then()
					.statusCode(404);
	}
	
	@Test
	@Order(6)
	@DisplayName("When GET FindByAll should return list of PatientDTOOutput and HTTPStatusCode 200")
	void whenGET_FindByAll_ShouldReturnListOfPatientAndHTTPStatus200() throws JsonMappingException, JsonProcessingException {
				
		var anotherPatient = new PatientDTOInput("Maria Santos", LocalDate.of(2000, 11, 3), "Female", "maria@email.com", "88888-8888");
		
		given().spec(specification)
		.body(anotherPatient)
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
		
		PatientDTOOutput patientDtoOutput1 = (PatientDTOOutput) list.get(0);
		
		assertNotNull(patientDtoOutput1);
		assertTrue(patientDtoOutput1.getId() > 0);
		assertEquals(patientDtoInput.getName(), patientDtoOutput1.getName());
		assertEquals(patientDtoInput.getGender(), patientDtoOutput1.getGender());
		assertEquals(patientDtoInput.getBirthday(), patientDtoOutput1.getBirthday());
		assertEquals(patientDtoInput.getEmail(), patientDtoOutput1.getEmail());
		assertEquals(patientDtoInput.getPhone(), patientDtoOutput1.getPhone());
		
		PatientDTOOutput patientDtoOutput2 = (PatientDTOOutput) list.get(1);
		
		assertNotNull(patientDtoOutput2);
		assertTrue(patientDtoOutput2.getId() > 0);
		assertEquals(anotherPatient.getName(), patientDtoOutput2.getName());
		assertEquals(anotherPatient.getGender(), patientDtoOutput2.getGender());
		assertEquals(anotherPatient.getBirthday(), patientDtoOutput2.getBirthday());
		assertEquals(anotherPatient.getEmail(), patientDtoOutput2.getEmail());
		assertEquals(anotherPatient.getPhone(), patientDtoOutput2.getPhone());
	}
	
	@Test
	@Order(7)
	@DisplayName("Given PatientDTOInput and id when PUT update should return updated PatientDTOOutput and HTTPStatusCode 200")
	void givenPatientObjectAndId_WhenPUT_update_ShouldReturnPatientUpdatedAndHTTPStatus200() throws JsonMappingException, JsonProcessingException {
		
		var updatePatient = new PatientDTOInput("Marlon silva", LocalDate.of(2002, 12, 24), "Male", "marl@email.com", "77777-7777");	
		
		var content = given().spec(specification)
					.pathParam("patientDtoInputId", 1L)
					.body(updatePatient)
				.when()
					.put("/{patientDtoInputId}")
				.then()
					.statusCode(200)
						.extract()
							.body()
							.asString();

		PatientDTOOutput returnPatient = objectMapper.readValue(content, PatientDTOOutput.class);

		assertNotNull(returnPatient);
		assertEquals(1L, returnPatient.getId());
		assertEquals(updatePatient.getName(), returnPatient.getName());
		assertEquals(updatePatient.getGender(), returnPatient.getGender());
		assertEquals(updatePatient.getBirthday(), returnPatient.getBirthday());
		assertEquals(updatePatient.getEmail(), returnPatient.getEmail());
		assertEquals(updatePatient.getPhone(), returnPatient.getPhone());
		
	}
	
	@Test
	@Order(8)
	@DisplayName("Given PatientDTOInput with same email when PUT update should return updated PatientDTOOutput and HTTPStatusCode 200")
	void givenPatientObjectWithSameEmail_WhenPUT_update_ShouldReturnHTTPStatus400() throws JsonMappingException, JsonProcessingException {
		
		var updatePatient = new PatientDTOInput("Alex max", LocalDate.of(2002, 12, 24), "Male", "marl@email.com", "77777-7777");	
		
		 given().spec(specification)
					.pathParam("patientDtoInputId", 1L)
					.body(updatePatient)
				.when()
					.put("/{patientDtoInputId}")
				.then()
					.statusCode(400);
	}
	
	@Test
	@Order(9)
	@DisplayName("Given PatientDTOInput with same phone when PUT update should return updated PatientDTOOutput and HTTPStatusCode 200")
	void givenPatientObjectWithSamePhone_WhenPUT_update_ShouldReturnHTTPStatus400() throws JsonMappingException, JsonProcessingException {

		var updatePatient = new PatientDTOInput("Alex max", LocalDate.of(2002, 12, 24), "Male", "marlon@email.com", "77777-7777");	
		
		given().spec(specification)
				.pathParam("patientDtoInputId", 1L)
				.body(updatePatient)
			.when()
				.put("/{patientDtoInputId}")
			.then()
				.statusCode(400);
	}
	
	@Test
	@Order(10)
	@DisplayName("Given id when DELETE should return HTTPStatusCode 204")
	void givenId_When_DELETE_ShouldReturnHTTPStatus204() throws JsonMappingException, JsonProcessingException {
		
		given().spec(specification)
				.pathParam("patientDtoInputId", 1L)
				.when()
					.delete("/{patientDtoInputId}")
				.then()
					.statusCode(204);
	}
	
	@Test
	@Order(11)
	@DisplayName("Given inexist id when DELETE should return HTTPStatusCode 404")
	void givenInexistId_When_DELETE_ShouldReturnHTTPStatus404() throws JsonMappingException, JsonProcessingException {
		
		given().spec(specification)
			.pathParam("patientDtoInputId", 1L)
		.when()
			.delete("/{patientDtoInputId}")
		.then()
			.statusCode(404);
	}
	
	private void configurations() {
		specification = new RequestSpecBuilder().setBasePath("/pacientes")
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
