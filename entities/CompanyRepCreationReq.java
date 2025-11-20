package entities;

/**
 * Represents a request submitted by an individual to create a Company Representative account.
 *
 * <p>This request is reviewed by Career Staff, who may approve or reject the creation
 * of a new {@link CompanyRep} user account. The request stores essential information
 * needed to set up the account if approved.</p>
 *
 * <p>Each request contains:</p>
 * <ul>
 *     <li>The applicant's desired user ID</li>
 *     <li>Their full name</li>
 *     <li>The company they represent</li>
 *     <li>The department they belong to</li>
 *     <li>Their job position</li>
 * </ul>
 */
public class CompanyRepCreationReq{
    /** The desired User ID of the applicant. */
    private String userID;
     /** The full name of the applicant. */
    private String name;
    /** The name of the company the applicant represents. */
    private String companyName;
     /** The department within the company. */
    private String department;
    /** The applicant's job title or position. */
    private String position;
    /**
     * Constructs a new Company Representative account creation request.
     *
     * @param userID      the desired user ID
     * @param name        the applicant's full name
     * @param companyName the company the applicant represents
     * @param department  the department within the company
     * @param position    the applicant's job position or title
     */
    public CompanyRepCreationReq(String userID, String name, String companyName, String department, String position){
        this.userID = userID;
        this.name = name;
        this.companyName = companyName;
        this.department = department;
        this.position = position;
    }
    /**
     * Returns the desired user ID of the applicant.
     *
     * @return the desired user ID
     */
    public String getUserID(){
        return userID;
    }
    /**
     * Returns the name of the applicant.
     *
     * @return the applicant's full name
     */
    public String getName(){
        return name;
    }
     /**
     * Returns the company name provided in the request.
     *
     * @return the company name
     */
    public String getCompanyName(){
        return companyName;
    }
    /**
     * Returns the department the applicant belongs to.
     *
     * @return the department name
     */
    public String getDepartment(){
        return department;
    }
    /**
     * Returns the job position of the applicant.
     *
     * @return the job title or position
     */
    public String getPosition(){
        return position;
    }
}
