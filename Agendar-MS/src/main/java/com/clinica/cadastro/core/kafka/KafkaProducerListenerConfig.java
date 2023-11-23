package com.clinica.cadastro.core.kafka;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducerListenerConfig implements ProducerListener<String, Object> {

	@Override
	public void onSuccess(ProducerRecord<String, Object> producerRecord, RecordMetadata recordMetadata) {
		ProducerListener.super.onSuccess(producerRecord, recordMetadata);

		System.out.println("Mensagem enviada com sucesso:");
		System.out.println("Tópico: " + producerRecord.topic());
		System.out.println("Partição: " + recordMetadata.partition());
		System.out.println("Offset: " + recordMetadata.offset());
	}

	@Override
	public void onError(ProducerRecord<String, Object> producerRecord, RecordMetadata recordMetadata,
			Exception exception) {
		ProducerListener.super.onError(producerRecord, recordMetadata, exception);

		System.err.println(
				"Falha no envio da mensagem para o tópico " + producerRecord.topic() + ": " + exception.getMessage());
	}
}
