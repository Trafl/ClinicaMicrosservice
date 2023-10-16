package com.clinica.cadastro.controller.modelMapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.clinica.cadastro.domain.dto.input.UserDTOInput;
import com.clinica.cadastro.domain.dto.output.UserDTOOutput;
import com.clinica.cadastro.domain.model.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserMapper {

	final private ModelMapper mapper;
	
	public User toEntity(UserDTOInput userInput) {
		return mapper.map(userInput, User.class);
	}
	
	public UserDTOOutput toDTO (User user) {
		return mapper.map(user, UserDTOOutput.class);
	}
	
	public void copyToDomain(UserDTOInput userInput, User user) {
		mapper.map(userInput, user);
	}
	
	public List<UserDTOOutput> toDTOCollection(List<User> userList){
		return userList.stream()
				.map((user) -> toDTO(user)).toList();
	}
}
