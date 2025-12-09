package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.service.UserService;

@SpringBootApplication
public class SmartqApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(SmartqApplication.class, args);
		//userService.createUser();
	}

}
