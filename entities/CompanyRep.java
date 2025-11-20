package entities;

/**
 * Represents a Company Representative user in the internship system.
 *
 * <p>Company Representatives are responsible for:
 * <ul>
 *     <li>Creating internship opportunities</li>
 *     <li>Editing and managing pending opportunity submissions</li>
 *     <li>Reviewing and approving student applications</li>
 *     <li>Managing visibility and status of their posted opportunities</li>
 * </ul>
 *
 * <p>This class extends {@link User} by adding company-specific information.</p>
 */
public class CompanyRep extends User{
    /** The name of the company the representative belongs to. */
    private String companyName;
    /** The department within the company. */
    private String department;
    /** The representative's position or job title. */
    private String position;

    /**
     * Constructs a new CompanyRep user with the given details.
     *
     * @param userID      the unique ID of the company representative
     * @param name        the representative's full name
     * @param companyName the name of the company
     * @param department  the company department the representative works in
     * @param position    the representative's job title or role
     * @param password    the password used for system authentication
     */
    public CompanyRep(String userID, String name, String companyName, String department, String position, String password){
        super(userID, name, password);
        this.role = Role.COMPANY_REP;
        this.companyName = companyName;
        this.department = department;
        this.position = position;
    }

    /**
     * Returns the name of the company represented by this user.
     *
     * @return the company name
     */
    public String getCompanyName(){
        return companyName;
    }
    /**
     * Returns the department the representative belongs to.
     *
     * @return the department name
     */
    public String getDepartment(){
        return department;
    }
    /**
     * Returns the representative's job title.
     *
     * @return the position or job title
     */
    public String getPosition(){
        return position;
    }
}
