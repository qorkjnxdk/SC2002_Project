package controllers;
import repositories.UserRepository;
import util.ConsoleColors;
import entities.Session;
import entities.User;
import entities.User.Role;
import exceptions.InvalidInputException;
import exceptions.UserDoesntExistException;
import exceptions.WrongPasswordException;

/**
 * Controller responsible for handling user authentication.
 *
 * <p>This controller validates login credentials, checks user roles, and returns
 * a {@link Session} object upon successful authentication. It interfaces with the
 * {@link UserRepository} to locate users based on their ID and role.
 *
 * <p>Supported roles:
 * <ul>
 *     <li>1 — Student</li>
 *     <li>2 — Career Centre Staff</li>
 *     <li>3 — Company Representative</li>
 * </ul>
 */
public class AuthController {
    private final UserRepository users;
    public AuthController(UserRepository users){
        this.users = users;
    };

    /**
     * Attempts to authenticate a user based on provided credentials and role selection.
     *
     * <p>The authentication process performs the following checks:
     * <ol>
     *     <li>Ensures both user ID and password are non-empty</li>
     *     <li>Validates that the role selection is one of the supported values</li>
     *     <li>Retrieves the user based on the selected role</li>
     *     <li>Checks if the user exists</li>
     *     <li>Validates that the password matches the stored one</li>
     * </ol>
     *
     * <p>If successful, a new {@link Session} object is returned representing
     * the authenticated user.
     *
     * @param userID  the user ID provided by the user attempting to log in
     * @param password the password provided by the user
     * @param choice  the role selection (1 = student, 2 = career staff, 3 = company rep)
     * @return a {@link Session} object containing the authenticated user's details
     *
     * @throws InvalidInputException       if the input fields are empty or the role is invalid
     * @throws UserDoesntExistException    if no user exists with the given ID
     * @throws WrongPasswordException      if the password is incorrect
     */
    
    public Session login(String userID, String password, int choice)
        throws InvalidInputException, UserDoesntExistException, WrongPasswordException
    {
        if(userID.isEmpty()||password.isEmpty()){
            throw new InvalidInputException(ConsoleColors.RED+"UserID and Password cannot be empty!"+ConsoleColors.RESET);
        }
        if(!(choice==1||choice==2||choice==3)){
            throw new InvalidInputException(ConsoleColors.RED+"Choose your role again!"+ConsoleColors.RESET);
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
                throw new InvalidInputException(ConsoleColors.RED+"Choose your role properly!"+ConsoleColors.RESET);
        }
        if (user==null){
            throw new UserDoesntExistException(ConsoleColors.RED+"No account found with this User ID: " + userID+ConsoleColors.RESET);
        }
        if (!user.getPassword().equals(password)){
            throw new WrongPasswordException(ConsoleColors.RED+"Incorrent password entered"+ConsoleColors.RESET);
        }
        System.out.println(ConsoleColors.GREEN+"Login successful!"+ConsoleColors.RESET);
        return new Session(userID, user, role);
    }
}
