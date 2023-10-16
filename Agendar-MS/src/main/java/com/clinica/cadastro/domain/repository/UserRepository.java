package com.clinica.cadastro.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clinica.cadastro.domain.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
