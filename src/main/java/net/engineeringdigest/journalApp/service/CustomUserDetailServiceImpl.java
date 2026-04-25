package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
public class CustomUserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository; // Inject the UserRepository to access user data from MongoDB

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Implement logic to load user details from the database using the username
        // For example, you can use a UserRepository to fetch the user and return a UserDetails object
        // If the user is not found, throw a UsernameNotFoundException

        User user = userRepository.findByUsername(username);
        if (user != null) {
            return org.springframework.security.core.userdetails.User.builder().username(user.getUsername())
                    .password(user.getPassword()) // These are general getter/setter User user = new User and using their getters and setters to access the data from the database
                    .roles(user.getRoles().toArray(new String[0]))
                    .build(); // When user is found, return the UserDetails object
        }
        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}
