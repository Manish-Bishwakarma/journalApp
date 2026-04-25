package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.mockito.Mockito.*;

import java.util.ArrayList;

import static org.mockito.Mockito.when;

//@SpringBootTest //We will not use @SpringBootTest here because we want to test the CustomUserDetailServiceImpl class in isolation without loading the entire Spring application context. Instead, we will use Mockito to mock the dependencies of the CustomUserDetailServiceImpl class, allowing us to focus on testing its behavior without relying on the actual database or other components.
public class CustomUserDetailServiceImplTest {

//    @Autowired
    @InjectMocks // This annotation is used to create an instance of the CustomUserDetailServiceImpl class and inject the mocked dependencies (in this case, the UserRepository) into it. It allows you to test the CustomUserDetailServiceImpl class in isolation by providing mock implementations of its dependencies.
    private CustomUserDetailServiceImpl customUserDetailService; //@InjectMocks are automatically initialized by Mockito, so we don't need to manually create an instance of CustomUserDetailServiceImpl. Mockito will handle the instantiation and injection of the mocked dependencies for us.

//    @MockBean // Replaces the actual UserRepository bean with a mock instance for testing purposes. This allows you to define the behavior of the UserRepository in your test without relying on the actual database.
    @Mock // Will be initiated via setUp()
    private UserRepository userRepository;

    @BeforeEach
    void setUp() { // Initialize the mocks before each test method is executed. This ensures that the mock objects are properly set up and ready to be used in the test methods.
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void loadUserByUsernameTets() {
        when(userRepository.findByUsername(ArgumentMatchers.any())).thenReturn(User.builder().username("Manish").password("test").roles(new ArrayList<>()).build());
        UserDetails userDetails = customUserDetailService.loadUserByUsername("Manish");
        Assertions.assertNotNull(userDetails);
    }
}