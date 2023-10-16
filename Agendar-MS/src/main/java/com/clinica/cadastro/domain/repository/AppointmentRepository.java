package com.clinica.cadastro.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clinica.cadastro.domain.model.MedicalAppointment;

public interface AppointmentRepository extends JpaRepository<MedicalAppointment, Long> {

}
