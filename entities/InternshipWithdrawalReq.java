package entities;

/**
 * Represents a student's request to withdraw from an internship they previously applied for.
 *
 * <p>A withdrawal request is submitted by a student and later reviewed by Career Staff,
 * who may approve or reject the request. Once approved, the student's application status
 * for the corresponding internship will be updated to {@code WITHDRAWN}.</p>
 *
 * <p>Each withdrawal request contains:</p>
 * <ul>
 *     <li>The user ID of the student requesting withdrawal</li>
 *     <li>The title of the internship</li>
 *     <li>The name of the company offering the internship</li>
 *     <li>The student's reason for withdrawing</li>
 * </ul>
 */
public class InternshipWithdrawalReq{
    private String userID;
    private String internshipTitle;
    private String companyName;
    private String withdrawalReason;
    /**
     * Constructs a new internship withdrawal request.
     *
     * @param userID            the ID of the student requesting withdrawal
     * @param internshipTitle   the title of the internship
     * @param companyName       the name of the company offering the internship
     * @param withdrawalReason  the reason for withdrawing the application
     */
    public InternshipWithdrawalReq(String userID, String internshipTitle, String companyName, String withdrawalReason){
        this.userID = userID;
        this.internshipTitle = internshipTitle;
        this.companyName = companyName;
        this.withdrawalReason = withdrawalReason;
    }
     /**
     * Returns the ID of the student who submitted the withdrawal request.
     *
     * @return the student's user ID
     */
    public String getUserID(){
        return userID;
    }
    /**
     * Returns the title of the internship being withdrawn from.
     *
     * @return the internship title
     */
    public String getInternshipTitle(){
        return internshipTitle;
    }
    /**
     * Returns the name of the company offering the internship.
     *
     * @return the company name
     */
    public String getCompanyName(){
        return companyName;
    }
     /**
     * Returns the student's reason for withdrawing the internship application.
     *
     * @return the withdrawal reason
     */
    public String getWithdrawalReason(){
        return withdrawalReason;
    }
}
