package com.microservices.orchestrated.product_validation_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class ProductValidationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductValidationServiceApplication.class, args);
	}

}
