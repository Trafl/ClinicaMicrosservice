CREATE TABLE _procedure (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    value DECIMAL(10, 2)
);

CREATE TABLE medical_appointment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    doctor_id BIGINT,
    patient_id BIGINT,
    appointment_time DATETIME,
    procedure_id BIGINT,
    is_open TINYINT(1) DEFAULT 1,
    FOREIGN KEY (doctor_id) REFERENCES user (id),
    FOREIGN KEY (patient_id) REFERENCES user (id),
    FOREIGN KEY (procedure_id) REFERENCES _procedure (id)
);