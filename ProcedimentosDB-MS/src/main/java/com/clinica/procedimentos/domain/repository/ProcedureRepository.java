package com.clinica.procedimentos.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clinica.procedimentos.domain.model.Procedure;

public interface ProcedureRepository extends JpaRepository<Procedure, Long> {

}
