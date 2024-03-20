package com.clinica.evolution.core.modelmapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfiguration {

	@Bean
	 ModelMapper modelMapper() {
		
		var mapper = new ModelMapper();
		
		return mapper;
	}
	
}
