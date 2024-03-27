# Clinic microservice
Welcome to the medical practice management microservice. This microservice was developed to organize and centralize essential information for a medical clinic. Below you will find an overview of the main components and functionalities of this project.

## Technologies Used
- Java 17
- Spring Boot
- Spring Data JPA
- ModelMapper
- Flyway
- Lombok
- MySQL
- SpringDoc
- Spring Gateway
- Spring Netflix Eureka
- Spring Cloud OpenFeign
- Apache Kafka
- JUnit 5
- RestAssured
- TestContainers

## Documentation
Each microservice has its own documentation in the Swagger standard for a better understanding of each End-point.
Access "http://localhost:8761/" and open the Eureka graphical interface to find out which port each application is running on.
After that, replace the connected port in http://localhost:"youPort" and add "/swagger-ui/index.html#/".

## Architecture
 ### Gateway Services:
- GateWayServer-MS: GateWay that unifies API entry points uses SpringGateWay

 ### Discovery Services: 
- EurekaServer-MS: Service used for APIs to be found on the network, uses Spring Netflix Eureka.

 ### Data Storage Services:
- PacientesDB-MS: responsible for CRUD of information related to patients.
- ProcedimentosDB-MS: responsible for CRUD of information related to procedures.
- MedicosDB-MS: responsible for CRUD of information related to doctors.
  
 ### Business Services: 
- Agendar-MS: responsible for making a synchronous call to the storage API's, collecting information and creating a Medical appointment entity.
- Financeiro-MS: Responsible for managing input values, example: medical appointments, output, example: purchased materials,
calculate both and return an expense report.

 ### Messaging Services:
 - Apache Kafka: Asynchronous messaging system to communicate between microservices.
 - Spring Cloud OpenFeign: Used to make synchronous calls within the project to query data services.

### Service
- Email service: Dedicated service sending emails to customers, can be implemented with Amazon's SES or Google's SMTP direct configuration in the application properties
