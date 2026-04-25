package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

// Creating Login-out Functionality
@Component
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class); // to avoid accidental reassignment of the logger.

    @Autowired
    private UserRepository userRepository;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

   public boolean saveNewUser(User user) {
       try {
           user.setPassword(passwordEncoder.encode(user.getPassword())); // It's encode the password which will be sent via request body and then set that encoded password back to the user
           user.setRoles(Arrays.asList("USER")); // Set the default role for the user, in this case, "USER". You can modify this to assign different roles based on your application's requirements.
           userRepository.save(user);
           return true;
       }  catch (Exception e) {
              logger.error("Error saving new user: {}", e.getMessage());
              logger.error("Stack trace: ", e);
              return false;
       }
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(ObjectId id) {
        return userRepository.findById(id);
    }

    public void deleteById(ObjectId id) {
        userRepository.deleteById(id);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void saveAdmin(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // It's encode the password which will be sent via request body and then set that encoded password back to the user
        user.setRoles(Arrays.asList("USER","ADMIN")); // Set the default role for the user, in this case, "USER". You can modify this to assign different roles based on your application's requirements.
        userRepository.save(user);
    }
}