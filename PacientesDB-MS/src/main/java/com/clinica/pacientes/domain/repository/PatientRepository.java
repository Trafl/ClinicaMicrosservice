package com.clinica.pacientes.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clinica.pacientes.domain.model.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {

}
