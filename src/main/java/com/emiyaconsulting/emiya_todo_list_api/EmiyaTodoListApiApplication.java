package com.emiyaconsulting.emiya_todo_list_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class EmiyaTodoListApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmiyaTodoListApiApplication.class, args);
	}
}
