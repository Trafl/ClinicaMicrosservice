package com.clinica.cadastro.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.clinica.cadastro.domain.model.AppointmentStatus;
import com.clinica.cadastro.domain.model.MedicalAppointment;


public interface AppointmentRepository extends JpaRepository<MedicalAppointment, Long> {

	Page<MedicalAppointment> findByStatus(AppointmentStatus status, Pageable pageable);
}
