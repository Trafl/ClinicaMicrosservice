package com.clinica.email.kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;


@Configuration
public class ConsumerKafkaConfig {

	  @Value(value = "${spring.kafka.bootstrap-servers:localhost:9092}")
	    private String bootstrapAddress;

	  
	  @Bean
	    public ConsumerFactory<String, Object > consumerFactory() {
	        Map<String, Object> props = new HashMap<>();
	        props.put(
	                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
	                bootstrapAddress);
	        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
	        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
	        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, StringDeserializer.class);
	        props.put(
	                JsonDeserializer.TRUSTED_PACKAGES,
	                "*");
	       
	        return new DefaultKafkaConsumerFactory<>(props);
	    }
	  
	  @Bean
	    public ConcurrentKafkaListenerContainerFactory<String, Object>
	        kafkaListenerContainerFactory() {

	        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
	                new ConcurrentKafkaListenerContainerFactory<>();
	        factory.setConsumerFactory(consumerFactory());
	        factory.setRecordMessageConverter(new JsonMessageConverter());
	        return factory;
	    }

}
