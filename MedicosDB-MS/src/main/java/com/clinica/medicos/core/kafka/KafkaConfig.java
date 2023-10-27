package com.clinica.medicos.core.kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.clinica.medicos.domain.dto.DoctorKafkaDTOOutput;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;

@Configuration
public class KafkaConfig {

 	@Value("${spring.kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapAddress;
	
	@Bean
	public ProducerFactory<String, DoctorKafkaDTOOutput> producerAppointment() {
		
		Map<String, Object> configProps = new HashMap<>();
        
		configProps.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapAddress);
		
        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        
        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);
        
       return new DefaultKafkaProducerFactory<>(configProps);
	}
	
	 @Bean
	    public ConsumerFactory<? super String, ? super Object> consumerFactory() {
	        Map<String, Object> props = new HashMap<>();
	        props.put(
	                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
	                bootstrapAddress);
	        props.put(
	                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
	                StringDeserializer.class);
	        props.put(
	                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
	                JsonDeserializer.class);
	        props.put(
	                JsonDeserializer.TRUSTED_PACKAGES,
	                "*");
	        return new DefaultKafkaConsumerFactory<>(props);
	    }
	
	@Bean
	public KafkaTemplate<String, DoctorKafkaDTOOutput> kafkaTemplate(){
		return new KafkaTemplate<>(producerAppointment());
	}
	
	 @Bean
	    public ConcurrentKafkaListenerContainerFactory<String, Object>
	        kafkaListenerContainerFactory() {

	        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
	                new ConcurrentKafkaListenerContainerFactory<>();
	        factory.setConsumerFactory(consumerFactory());
	        return factory;
	    }
	
}