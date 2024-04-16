package com.clinica.medicos.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clinica.medicos.domain.model.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long>{

	boolean existsByCrm(String crm);
}
