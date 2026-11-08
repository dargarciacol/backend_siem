package com.nova.nova_siem_backend.siem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class NovaSiemApplication {

	public static void main(String[] args) {
		SpringApplication.run(NovaSiemApplication.class, args);
	}
}




