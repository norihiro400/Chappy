package com.example.book_manager_app;

import com.example.book_manager_app.domain.Book;
import com.example.book_manager_app.domain.User;
import com.example.book_manager_app.repository.BookRepository;
import com.example.book_manager_app.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class BookManagerAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookManagerAppApplication.class, args);
	}

	
}
