package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class PublicController {
    @Autowired
    private UserService userService;

    // Create User
    @PostMapping("/create-user") //now our POST end-point is "/public/create-user" and anyone can access this end-point without authentication to create an account.
    public void createUser(@RequestBody User user) { //this end-point is public so anyone can create an account.
        userService.saveNewUser(user);
    } // Because the public controller will remain unauthenticated, we can only allow the creation of new users through this controller.
    // This is a common practice in applications that require user registration, as it allows new users to sign up without needing to be authenticated first. By providing a public endpoint for user creation, we can ensure that anyone can create an account and start using the application without any barriers.

}
