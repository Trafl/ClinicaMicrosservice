package com.clinica.cadastro.controller.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.clinica.cadastro.controller.modelMapper.UserMapper;
import com.clinica.cadastro.domain.dto.input.UserDTOInput;
import com.clinica.cadastro.domain.dto.output.UserDTOOutput;
import com.clinica.cadastro.domain.model.User;
import com.clinica.cadastro.domain.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UserController {

	final private UserService userService;
	
	final private UserMapper userMapper;
	
	@GetMapping
	public ResponseEntity<List<UserDTOOutput>> findAllUsers(){
		List<User> userList = userService.findAll();
		List<UserDTOOutput> userDtoList = userMapper.toDTOCollection(userList);
		
		return ResponseEntity.ok(userDtoList);
	}
	
	@GetMapping("/{userId}")
	public ResponseEntity<UserDTOOutput> findUserById(@PathVariable Long userId){
		User user = userService.findById(userId);
		UserDTOOutput userDto = userMapper.toDTO(user);
		
		return ResponseEntity.ok(userDto);
	}
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public ResponseEntity<UserDTOOutput> createUser(@RequestBody @Valid UserDTOInput dtoInput){
		User user = userMapper.toEntity(dtoInput);
		user = userService.saveUser(user);

		UserDTOOutput userDto = userMapper.toDTO(user);
		return ResponseEntity.ok().body(userDto);
	}
	
	@PutMapping("/{userId}")
	public ResponseEntity<UserDTOOutput> updateUser(@PathVariable Long userId, @RequestBody @Valid UserDTOInput dtoInput){
		User userInDb = userService.findById(userId);
		userMapper.copyToDomain(dtoInput, userInDb);
		
		userInDb = userService.saveUser(userInDb);
		UserDTOOutput userDto = userMapper.toDTO(userInDb);
		
		
		return ResponseEntity.ok(userDto);
	}
	
	@DeleteMapping("/{userId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void deleteUserById(@PathVariable Long userId){
		userService.deleteUserById(userId);
	}
	
	
}
