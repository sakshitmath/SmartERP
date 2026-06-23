package com.smarterp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SmarterpBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(SmarterpBackendApplication.class, args);
	}
}