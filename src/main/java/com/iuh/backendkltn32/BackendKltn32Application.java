package com.iuh.backendkltn32;


import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BackendKltn32Application {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getDefault());
		SpringApplication.run(BackendKltn32Application.class, args);
	}

}
