package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import net.engineeringdigest.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user") //our user must be authenticated to access this controller, so we will use this controller for updating user data and getting all users (which is only for ADMIN)
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    // We no longer need Get all Users Controller, it might be used by ADMIN (more importantly previous one user can see all users with this)
    /*@GetMapping
    public List<User> getAllUsers() {
        return userService.getAll();
    }*/


    // Update User
    @PutMapping
    public ResponseEntity<?>  updateUser(@RequestBody User user) {
        // When we hit "/user" with a PUT request using the username and password that should be automatically exist here through Security Context Holder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();// Get the username of the currently authenticated user from the security context. This allows us to identify which user's data we want to update based on their authentication information.
        // Till above line, the user has been authenticated already, Authenticate is automatically created by Spring Security when the user successfully authenticates, and it contains information about the authenticated user, such as their username and roles. By accessing the security context, we can retrieve this authentication object and use it to identify the user who is making the request to update their data.
        String username = authentication.getName(); // Get the username of the currently authenticated user from the authentication object. This username will be used to find the corresponding user in the database and update their information.
        User userInDb = userService.findByUsername(username);
        if (userInDb != null) {
            userInDb.setUsername(user.getUsername());
            userInDb.setPassword(user.getPassword());
            userService.saveNewUser(userInDb); // Save updated data in ID
        }
        return new  ResponseEntity<>(userInDb, HttpStatus.OK); // Return the updated user data in the response body with HTTP status code 200 (OK).
    }

    // Delete User
    @DeleteMapping
    public ResponseEntity<?> deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userRepository.deleteByUsername(authentication.getName()); // Delete the user from the database based on the username of the currently authenticated user. This ensures that only the authenticated user can delete their own account.
        return new  ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
