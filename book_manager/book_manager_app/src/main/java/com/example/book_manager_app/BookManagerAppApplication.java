package com.example.book_manager_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BookManagerAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookManagerAppApplication.class, args);
	}

}
