package com.example.techcomp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = {"com.example.techcomp", "com.example.techcomp.security"})
public class TechcompApplication {
	public static void main(String[] args) {
		SpringApplication.run(TechcompApplication.class, args);
	}
}