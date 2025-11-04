package controllers;

import repositories.UserRepository;
import repositories.OpportunityRepository;

public class StudentController {
    private UserRepository users;
    private OpportunityRepository opportunities;
    public StudentController(UserRepository users, OpportunityRepository opportunities){
        this.users = users;
        this.opportunities = opportunities;
    }
}
