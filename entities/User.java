package entities;

/**
 * Represents a generic user in the NTU Internship System.
 *
 * <p>The {@code User} class serves as the abstract parent class for all user types,
 * including:</p>
 * <ul>
 *     <li>{@link Student}</li>
 *     <li>{@link CompanyRep}</li>
     *     <li>{@link CareerStaff}</li>
 * </ul>
 *
 * <p>Each user has basic identity information such as:</p>
 * <ul>
 *     <li>User ID</li>
 *     <li>Name</li>
 *     <li>Password</li>
 *     <li>Role (Student, Company Representative, or Career Staff)</li>
 * </ul>
 *
 * <p>Subclasses must assign the {@link Role} field appropriately in their constructors.</p>
 */
public abstract class User {
    public enum Role {STUDENT,COMPANY_REP,CAREER_STAFF};
    protected Role role;
    private String userID;
    private String name;
    private String password;

     /**
     * Constructs a new user with basic identity fields.
     *
     * @param userID   the unique ID of the user
     * @param name     the full name of the user
     * @param password the user's login password
     */
    public User(String userID, String name, String password){
        this.userID = userID;
        this.name = name;
        this.password = password;
    }

     
    public void logIn(String password){
        
    }
    public void logOut(){

    }

     /**
     * Returns the role of the user.
     *
     * @return the user's role
     */
    public Role getRole(){
        return role;
    }

     /**
     * Returns the user's unique identifier.
     *
     * @return the user ID
     */
    public String getUserId(){
        return userID;
    }

     /**
     * Returns the full name of the user.
     *
     * @return the user’s name
     */
    public String getName(){
        return name;
    }
    /**
     * Returns the user's password.
     *
     * @return the user's password
     */
    public String getPassword(){
        return password;
    }
    /**
     * Changes the user's password to a new value.
     *
     * @param password the new password
     */
    public void changePassword(String password){
        this.password = password;
    }
}
