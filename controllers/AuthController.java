package controllers;
import repositories.UserRepository;
import entities.Session;
import entities.User;
import entities.User.Role;
import exceptions.InvalidInputException;
import exceptions.UserDoesntExistException;
import exceptions.WrongPasswordException;

public class AuthController {
    private final UserRepository users;
    public AuthController(UserRepository users){
        this.users = users;
    };
    public Session login(String userID, String password, int choice)
        throws InvalidInputException, UserDoesntExistException, WrongPasswordException
    {
        if(userID.isEmpty()||password.isEmpty()){
            throw new InvalidInputException("UserID and Password cannot be empty!");
        }
        if(!(choice==1||choice==2||choice==3)){
            throw new InvalidInputException("Choose your role again!");
        }
        User user;
        Role role;
        switch (choice) {
            case 1:
                user = users.findStudentByUserID(userID);
                role = Role.STUDENT;
                break;
            case 2:
                user = users.findCareerStaffByUserID(userID);
                role = Role.CAREER_STAFF;
                break;
            case 3:
                user = users.findCompanyRepByUserID(userID);
                role = Role.COMPANY_REP;
                break;
            default:
                throw new InvalidInputException("Choose your role properly!");
        }
        if (user==null){
            throw new UserDoesntExistException("No account found with this User ID: " + userID);
        }
        if (!user.getPassword().equals(password)){
            throw new WrongPasswordException("Incorrent password entered");
        }
        System.out.println("Login successful!");
        return new Session(userID, role);
    }
}
