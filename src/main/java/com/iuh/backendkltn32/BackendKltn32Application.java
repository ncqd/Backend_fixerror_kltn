package com.iuh.backendkltn32;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BackendKltn32Application {

	
	public static void main(String[] args) {
		SpringApplication.run(BackendKltn32Application.class, args);
	}

}
