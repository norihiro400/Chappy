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
@EnableScheduling
public class BookManagerAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookManagerAppApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(BookRepository bookRepository, UserRepository userRepository) {
		return (args) -> {
			// Create users
			User user1 = new User();
			user1.setUsername("testuser1");
			user1.setEmail("testuser1@example.com");
			userRepository.save(user1);

			User user2 = new User();
			user2.setUsername("testuser2");
			user2.setEmail("testuser2@example.com");
			userRepository.save(user2);

			// Create books
			Book book1 = new Book();
			book1.setTitle("Spring Boot実践");
			book1.setAuthor("山田太郎");
			book1.setIsbn("978-4-00000000-1");
			book1.setAvailable(true);
			bookRepository.save(book1);

			Book book2 = new Book();
			book2.setTitle("Javaプログラミング入門");
			book2.setAuthor("田中花子");
			book2.setIsbn("978-4-00000000-2");
			book2.setAvailable(true);
			bookRepository.save(book2);

			Book book3 = new Book();
			book3.setTitle("Clean Code");
			book3.setAuthor("Robert C. Martin");
			book3.setIsbn("978-0-13235088-4");
			book3.setAvailable(true);
			bookRepository.save(book3);

			Book book4 = new Book();
			book4.setTitle("Effective Java");
			book4.setAuthor("Joshua Bloch");
			book4.setIsbn("978-0-13468599-1");
			book4.setAvailable(true);
			bookRepository.save(book4);
		};
	}
}
