package controllers;
import repositories.*;

public class CompanyRepController {
    private RequestRepository requestRepository;
    private UserRepository userRepository;
    public CompanyRepController(RequestRepository requestRepository, UserRepository userRepository){
        this.requestRepository = requestRepository;
    }
    public void addInternshipOpportunity(String name){
        
    }
}
