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
	            .partitions(10)
	            .replicas(3)
	            .compact()
	            .build();
	}

}
