package com.emiyaconsulting.emiya_todo_list_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class EmiyaTodoListApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmiyaTodoListApiApplication.class, args);
	}

}
