package com.studymatchmaker.config;

import com.studymatchmaker.model.User;
import com.studymatchmaker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataLoader {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner seedUsers() {
        return args -> {

            // ✅ Prevent duplicate seeding
            if (userRepository.count() > 2) return;

            User u1 = new User();
            u1.setFullName("Alice Johnson");
            u1.setEmail("alice@test.com");
            u1.setPassword(passwordEncoder.encode("123")); // ✅ FIXED
            u1.setMajor("Engineering");
            u1.setAcademicLevel("Junior");
            u1.setStudyStyle("Collaborative");
            u1.setSchool("University of Minnesota");
            u1.setCourses(Set.of("Databases", "Algorithms")); // ✅ FIXED

            userRepository.save(u1);

            User u2 = new User();
            u2.setFullName("Michael Brown");
            u2.setEmail("michael@test.com");
            u2.setPassword(passwordEncoder.encode("123")); // ✅ FIXED
            u2.setMajor("Engineering");
            u2.setAcademicLevel("Junior");
            u2.setStudyStyle("Independent");
            u2.setSchool("University of Minnesota");
            u2.setCourses(Set.of("Databases")); // ✅ FIXED

            userRepository.save(u2);

            System.out.println("✅ Dummy users loaded");
        };
    }
}