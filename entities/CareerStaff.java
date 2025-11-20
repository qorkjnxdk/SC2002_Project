package entities;

/**
 * Represents a Career Staff user in the internship system.
 *
 * <p>Career Staff members are responsible for:
 * <ul>
 *     <li>Managing internship postings</li>
 *     <li>Approving or rejecting internship opportunities</li>
 *     <li>Handling student withdrawal requests</li>
 *     <li>Managing Company Representative account creation requests</li>
 * </ul>
 *
 * <p>This class extends {@link User} and adds staff-specific attributes.
 */
public class CareerStaff extends User{
    /** The department in which the career staff member works. */
    private String staffDepartment;
    /**
     * The staff member's email address.
     *
     * <p><b>Note:</b> The constructor currently assigns {@code staffDepartment} to this field
     * instead of {@code email}. This appears to be a bug in the original implementation.
     */
    private String email;
    /**
     * Constructs a new CareerStaff user with the given details.
     *
     * @param userID          the unique ID of the staff member
     * @param name            the staff member's name
     * @param staffDepartment the department the staff member belongs to
     * @param email           the email address of the staff member
     * @param password        the password for login authentication
     */
    public CareerStaff(String userID, String name, String staffDepartment, String email, String password){
        super(userID, name, password);
        this.role = Role.CAREER_STAFF;
        this.staffDepartment = staffDepartment;
        this.email = staffDepartment;
    }
    /**
     * Returns the staff member's department.
     *
     * @return the staff department
     */
    public String getStaffDepartment(){
        return staffDepartment;
    }
     /**
     * Returns the staff member's email.
     *
     * @return the email address (note: may be incorrect due to constructor bug)
     */
    public String getEmail(){
        return email;
    }
}
