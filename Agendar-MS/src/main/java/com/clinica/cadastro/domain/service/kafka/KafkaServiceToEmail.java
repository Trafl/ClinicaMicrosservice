package com.clinica.cadastro.domain.service.kafka;

import java.util.concurrent.CompletableFuture;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class KafkaServiceToEmail {

	private final KafkaTemplate<String, Object> template;
	
	public void sendMessage(String topic, Object value) {
		
		final ProducerRecord<String, Object> data = createRecord(topic, value);
		
		CompletableFuture<SendResult<String, Object>> future = template.send(data);
		
		/*
		 * future.whenComplete((result, ex) -> { if (ex == null) {
		 * System.out.println(result.getRecordMetadata().topic());
		 * System.out.println(result.getRecordMetadata().partition());
		 * System.out.println(result.getRecordMetadata().offset());
		 * System.out.println(result.getRecordMetadata().serializedValueSize()); } else
		 * { throw new BusinessException (ex.getMessage()); } });
		 */
	}

	private ProducerRecord<String, Object> createRecord(String topic, Object value) {
		return new ProducerRecord<String, Object>(topic, value);
	}
}
