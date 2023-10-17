# ClinicaMicrosservice
Projeto criado com o intuito de melhorar a administração de uma clinica medica. 
Focado no agendamento de consultas, organização dos pacientes e controle financeiro.

# Tecnologias Utilizadas 
- Java 17
- Spring Boot
- Spring Data JPA
- ModelMapper
- Flyway
- Lombok
- MySQL
- OpenApi
- SpringDoc
- Spring Gateway
- Spring netflix eureka
- RabbitMQ

# Documentação
Cada microsserviço tem a sua propria documentação no padrão Swagger para o melhor entendimento sobre cada End-point.

# Arquitetura
 ## Serviços de Gateway:
- GateWayServer-MS: GateWay que unifica os pontos de entrada da API, utiliza o SpringGateWay.

 ## Serviços de Descoberta: 
- EurekaServer-MS: Serviço utilizado para que as API's se encontrem na rede, utilza o Spring netflix Eureka.

 ## Serviços de Armazenamento de Dados:
- PacientesDB-MS: responsavel pelo CRUD das informações relacionadas aos pacientes.
- ProcedimentosDB-MS: responsavel pelo CRUD das informações relacionadas aos procedimentos.
- MedicosDB-MS: responsavel pelo CRUD das informações relacionadas aos medicos.
  
 ## Serviços de Negócios: 
- Agendar-MS: responsavel por consultar as API's de armazenamento, coletar informações e criar uma entidade Consulta

- Financeiro-MS: responsavel por gerenciar os valores de entrada exemplo: consultas, saida exemplo: materiais comprados
calcular ambos e retornar um relatorio das despesas

 ## Serviços de Mensagens:
 - RabbitMq: Sistema de mensageria assincrono para fazer a comunicação entre os microsserviços 
