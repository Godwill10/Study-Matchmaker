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
            if (userRepository.count() > 0) return;

            String pw = passwordEncoder.encode("password123");

            userRepository.save(User.builder()
                .fullName("Alice Johnson").email("alice@test.com").password(pw)
                .school("University of Minnesota").major("Computer Science").academicLevel("Junior")
                .studyStyle("Collaborative").preferredMode("Hybrid").state("MN").city("Minneapolis")
                .bio("CS major interested in AI and web development. Love group study sessions!")
                .courses(Set.of("Algorithms", "Database Systems", "Software Engineering", "Computer Networks"))
                .build());

            userRepository.save(User.builder()
                .fullName("Michael Brown").email("michael@test.com").password(pw)
                .school("University of Minnesota").major("Computer Science").academicLevel("Junior")
                .studyStyle("Solo").preferredMode("Online").state("MN").city("St. Paul")
                .bio("Focused on systems programming and security. Prefer quiet, focused study.")
                .courses(Set.of("Algorithms", "Database Systems", "Operating Systems", "Computer Architecture"))
                .build());

            userRepository.save(User.builder()
                .fullName("Sarah Chen").email("sarah@test.com").password(pw)
                .school("University of Minnesota").major("Data Science").academicLevel("Senior")
                .studyStyle("Discussion-based").preferredMode("In-person").state("MN").city("Minneapolis")
                .bio("Data science senior working on my capstone. Happy to help underclassmen!")
                .courses(Set.of("Machine Learning", "Data Mining", "Advanced Algorithms", "Capstone Project"))
                .build());

            userRepository.save(User.builder()
                .fullName("James Williams").email("james@test.com").password(pw)
                .school("Macalester College").major("Mathematics").academicLevel("Sophomore")
                .studyStyle("Problem-solving").preferredMode("Hybrid").state("MN").city("St. Paul")
                .bio("Math major who loves proofs and puzzles. Looking for study partners for analysis.")
                .courses(Set.of("Linear Algebra", "Calculus III", "Discrete Mathematics", "Data Structures"))
                .build());

            userRepository.save(User.builder()
                .fullName("Emily Rodriguez").email("emily@test.com").password(pw)
                .school("University of St Thomas").major("Biology").academicLevel("Sophomore")
                .studyStyle("Visual learner").preferredMode("In-person").state("MN").city("St. Paul")
                .bio("Pre-med student. I make detailed diagrams and flashcards for everything!")
                .courses(Set.of("Organic Chemistry I", "Anatomy & Physiology I", "Genetics", "General Physics II"))
                .build());

            userRepository.save(User.builder()
                .fullName("David Kim").email("david@test.com").password(pw)
                .school("UCLA").major("Computer Engineering").academicLevel("Junior")
                .studyStyle("Hands-on").preferredMode("Hybrid").state("CA").city("Los Angeles")
                .bio("Hardware meets software. Working on embedded systems projects.")
                .courses(Set.of("Computer Architecture", "Circuit Analysis", "Signals & Systems", "Software Engineering"))
                .build());

            userRepository.save(User.builder()
                .fullName("Priya Patel").email("priya@test.com").password(pw)
                .school("Stanford University").major("Artificial Intelligence").academicLevel("Graduate")
                .studyStyle("Teaching others").preferredMode("Online").state("CA").city("Palo Alto")
                .bio("PhD student researching NLP. I learn best by explaining to others.")
                .courses(Set.of("Deep Learning", "Advanced Machine Learning", "Natural Language Processing", "Thesis Research"))
                .build());

            userRepository.save(User.builder()
                .fullName("Marcus Thompson").email("marcus@test.com").password(pw)
                .school("Georgia Institute of Technology").major("Mechanical Engineering").academicLevel("Senior")
                .studyStyle("Collaborative").preferredMode("In-person").state("GA").city("Atlanta")
                .bio("Senior ME student. Let's grind through problem sets together!")
                .courses(Set.of("Thermodynamics", "Capstone Project", "Senior Thesis", "Numerical Methods"))
                .build());

            userRepository.save(User.builder()
                .fullName("Olivia Martinez").email("olivia@test.com").password(pw)
                .school("University of Texas at Austin").major("Business Administration").academicLevel("Junior")
                .studyStyle("Discussion-based").preferredMode("Hybrid").state("TX").city("Austin")
                .bio("Business major with a focus on entrepreneurship and marketing strategy.")
                .courses(Set.of("Corporate Finance", "Operations Management", "Consumer Behavior", "International Business"))
                .build());

            userRepository.save(User.builder()
                .fullName("Aiden O'Brien").email("aiden@test.com").password(pw)
                .school("Boston University").major("Psychology").academicLevel("Sophomore")
                .studyStyle("Flashcards & Repetition").preferredMode("Online").state("MA").city("Boston")
                .bio("Psych major interested in cognitive science. I make Anki decks for everything.")
                .courses(Set.of("Abnormal Psychology", "Developmental Psychology", "Research Methods", "Introduction to Statistics"))
                .build());

            userRepository.save(User.builder()
                .fullName("Jasmine Washington").email("jasmine@test.com").password(pw)
                .school("Howard University").major("Computer Science").academicLevel("Freshman")
                .studyStyle("Collaborative").preferredMode("In-person").state("DC").city("Washington")
                .bio("First-year CS student excited to learn! Looking for study buddies.")
                .courses(Set.of("Intro to Computer Science", "Calculus I", "English Composition", "Introduction to Psychology"))
                .build());

            userRepository.save(User.builder()
                .fullName("Ethan Nguyen").email("ethan@test.com").password(pw)
                .school("University of Washington").major("Software Engineering").academicLevel("Senior")
                .studyStyle("Problem-solving").preferredMode("Online").state("WA").city("Seattle")
                .bio("Into full-stack dev and cloud computing. Building cool stuff for my capstone.")
                .courses(Set.of("Cloud Computing", "Mobile App Development", "Distributed Systems", "Capstone Project"))
                .build());

            System.out.println("Seed data loaded: 12 demo users");
        };
    }
}
