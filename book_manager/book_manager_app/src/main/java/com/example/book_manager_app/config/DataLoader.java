package com.example.book_manager_app.config;

import com.example.book_manager_app.domain.Role;
import com.example.book_manager_app.domain.User;
import com.example.book_manager_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            User teacher = new User();
            teacher.setName("Teacher");
            teacher.setEmail("teacher@example.com");
            teacher.setPassword(passwordEncoder.encode("password"));
            teacher.setRole(Role.TEACHER);
            userRepository.save(teacher);

            User student = new User();
            student.setName("Student");
            student.setStudentId("S001");
            student.setEmail("student@example.com");
            student.setPassword(passwordEncoder.encode("password"));
            student.setRole(Role.STUDENT);
            userRepository.save(student);
        }
    }
}