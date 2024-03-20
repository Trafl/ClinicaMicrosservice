create table medical_appointment (
	id bigint not null auto_increment,
	doctor_id bigint not null,
	patient_id bigint not null,
	procedure_id bigint not null, 
	procedure_value decimal(38,2) not null, 
	date datetime(6) not null,
	finished_appointment datetime(6),   
	cancel_appointment datetime(6), 
	created_date datetime(6) not null,   
	doctor_name varchar(255) not null,
	doctor_specialty varchar(255) not null, 
	patient_email varchar(255), 
	patient_name varchar(255) not null,
	patient_phone varchar(255),
	procedure_name varchar(255) not null,
	status enum ('CANCELED','CREATED','FINISHED') not null,
	primary key (id)
) engine=InnoDB;