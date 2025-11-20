package com.udacity.jdnd.course3.critter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot application class for the Critter Chronologer system.
 * Entry point for the pet care scheduling application.
 */
// @SpringBootApplication: Combines @Configuration, @EnableAutoConfiguration, and @ComponentScan
// Enables Spring Boot's auto-configuration and component scanning
@SpringBootApplication
public class CritterApplication {

	// Application entry point - starts the Spring Boot application
	public static void main(String[] args) {
		// SpringApplication.run() launches the embedded web server and Spring context
		SpringApplication.run(CritterApplication.class, args);
	}

}
