package com.studymatchmaker;

import com.studymatchmaker.model.User;
import com.studymatchmaker.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveUser() {
        User user = User.builder()
                .fullName("Test User")
                .email("test@example.com")
                .password("123456")
                .school("University of St. Thomas")
                .courses(Set.of("SEIS 739"))
                .build();

        User saved = userRepository.save(user);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getEmail()).isEqualTo("test@example.com");
        assertThat(saved.getSchool()).isEqualTo("University of St. Thomas");
    }
}
