package com.clinica.cadastro.core.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopics {

	@Bean
	public NewTopic toEmailTopic() {
	    return TopicBuilder.name("agendar-to-emailService")
	            .partitions(4)
	            .replicas(1)
	            .compact()
	            .build();
	}

	@Bean
	public NewTopic toPatientEvolutionTopic() {
	    return TopicBuilder.name("agendar-to-patient-evolution")
	            .partitions(4)
	            .replicas(1)
	            .compact()
	            .build();
	}
	
}
