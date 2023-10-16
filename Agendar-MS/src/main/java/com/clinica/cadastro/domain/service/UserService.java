package com.clinica.cadastro.domain.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.clinica.cadastro.domain.exception.EntityInUseException;
import com.clinica.cadastro.domain.exception.EntityNotFoundException;
import com.clinica.cadastro.domain.model.User;
import com.clinica.cadastro.domain.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	final private UserRepository repository;
	
	public User findById(Long userId) {
		return repository.findById(userId)
				.orElseThrow(() -> new EntityNotFoundException(
						String.format("Usuario de id %s não foi encontrado", userId)));
	}
	
	public List<User> findAll(){
		return repository.findAll();
	}
	
	@Transactional
	public User saveUser(User user) {
		return repository.save(user);
	}
		
	@Transactional
	public void deleteUserById(Long userId) {
		try {
			repository.deleteById(userId);
			repository.flush();
			
		}catch(EmptyResultDataAccessException e) {
			throw new EntityNotFoundException(
					String.format("Usuario de id %s não foi encontrado.", userId));
		
		}catch(DataIntegrityViolationException e) {
			throw new EntityInUseException(
					String.format("Usuario de id %s esta em uso.", userId));
		}
	}
}
