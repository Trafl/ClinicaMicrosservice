package com.clinica.procedimentos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ProcedimentosDbMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProcedimentosDbMsApplication.class, args);
	}

}
