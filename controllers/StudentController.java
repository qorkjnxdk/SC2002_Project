package controllers;

import repositories.UserRepository;

public class StudentController {
    private UserRepository userRepository;
    public StudentController(UserRepository userRepository){
        this.userRepository = userRepository;
    }
}
