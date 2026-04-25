package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest // This annotation tells Spring Boot to look for a main configuration class (one with @SpringBootApplication, for instance) and use that to start a Spring application context. It is used to indicate that the class is a test class that should run with the Spring Boot test support.
public class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    public void testFindByUsername() {
        assertNotNull(userRepository.findByUsername("Manish"));
    }

    @Test
    public void testFindEntires()
    {
        User user = userRepository.findByUsername("Manish");
        assertTrue(!user.getEntries().isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Manish",
            "Lulu",
            "Zayn"
    })
    public void testFindByUsernameParameterized(String username) {
        assertNotNull(userRepository.findByUsername(username), "failed for: " + username);
    }

    @ParameterizedTest
    @ArgumentsSource(UserArgumentsProvider.class)
    public void testSaveNewUser(User user) {
        assertNotNull(userRepository.findByUsername(user.getUsername()), "failed for: " + user.getUsername());
    }
}
