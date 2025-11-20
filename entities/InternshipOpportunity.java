package entities;

import java.util.ArrayList;

/**
 * Represents an internship opportunity offered by a company in the NTU Internship System.
 *
 * <p>Each opportunity contains detailed information such as:</p>
 * <ul>
 *     <li>Title and description</li>
 *     <li>Internship level (Basic/Intermediate/Advanced)</li>
 *     <li>Preferred major</li>
 *     <li>Application opening and closing dates</li>
 *     <li>Company details (name, department, company representative)</li>
 *     <li>Number of available slots</li>
 *     <li>Approval status (Pending, Approved, Rejected, Filled)</li>
 *     <li>Visibility (shown/hidden from students)</li>
 *     <li>Applications submitted for this internship</li>
 * </ul>
 *
 * <p>This class acts as the foundation for all internship-related operations, including:</p>
 * <ul>
 *     <li>Filtering opportunities</li>
 *     <li>Student applications</li>
 *     <li>Company representative management</li>
 *     <li>Career Staff review and approval</li>
 * </ul>
 */

public class InternshipOpportunity{
    /** Title of the internship opportunity. */
    private String internshipTitle;

    /** Detailed description of the internship role. */
    private String description;

    /**
     * Represents the required experience level for the internship.
     */
    public enum InternshipLevel { BASIC, INTERMEDIATE, ADVANCED }

    /**
     * Represents the preferred major of students eligible for the opportunity.
     */
    public enum Major { CCDS, IEEE, DSAI }

    /**
     * Represents the approval status of the internship opportunity.
     */
    public enum Status { PENDING, APPROVED, REJECTED, FILLED }

    /** The required internship level. */
    private InternshipLevel internshipLevel;

    /** The preferred major of eligible applicants. */
    private Major preferredMajor;

    /** Application opening date in YYYY-MM-DD format. */
    private String applicationOpeningDate;

    /** Application closing date in YYYY-MM-DD format. */
    private String applicationClosingDate;

    /** Name of the company offering the internship. */
    private String companyName;

    /** Department responsible for the internship. */
    private String department;

    /** The User ID of the company representative managing the opportunity. */
    private String companyRepInCharge;

    /** Total number of available internship slots. */
    private int noOfSlots;

    /** The approval or filled status of the internship. */
    private Status status;

    /** Indicates whether the opportunity is visible to students. */
    private Boolean visible;

    /** List of applications submitted for this opportunity. */
    private ArrayList<Application> applicationList;

    /**
     * Constructs a new InternshipOpportunity with all required fields.
     *
     * @param internshipTitle          the title of the internship role
     * @param description              a detailed description of the role
     * @param internshipLevel          the level of the internship
     * @param preferredMajor           the preferred major of applicants
     * @param applicationOpeningDate   the date applications open (YYYY-MM-DD)
     * @param applicationClosingDate   the date applications close (YYYY-MM-DD)
     * @param companyName              the name of the offering company
     * @param department               the department offering the internship
     * @param companyRep               the user ID of the representative in charge
     * @param noOfSlots                number of internship slots available
     * @param status                   the approval status of the opportunity
     * @param visible                  whether the opportunity is visible to students
     * @param applicationList          the list of student applications
     */
    public InternshipOpportunity(String internshipTitle, String description, InternshipLevel internshipLevel, Major preferredMajor, String applicationOpeningDate,String applicationClosingDate,String companyName,String department,String companyRep,int noOfSlots, Status status, boolean visible, ArrayList<Application> applicationList) {
        this.internshipTitle = internshipTitle;
        this.description = description;
        this.internshipLevel = internshipLevel;
        this.preferredMajor = preferredMajor;
        this.applicationOpeningDate = applicationOpeningDate;
        this.applicationClosingDate = applicationClosingDate;
        this.companyName = companyName;
        this.department = department;
        this.companyRepInCharge = companyRep;
        this.noOfSlots = noOfSlots;
        this.status = status;
        this.visible = visible;
        this.applicationList = applicationList;
    }

    /** @return the title of the internship opportunity */
    public String getInternshipTitle() {
        return internshipTitle;
    }

    /** @param internshipTitle new title for the opportunity */
    public void setInternshipTitle(String internshipTitle) {
        this.internshipTitle = internshipTitle;
    }

    /** @return the role description */
    public String getDescription() {
        return description;
    }

    /** @param description updates the role description */
    public void setDescription(String description) {
        this.description = description;
    }

    /** @return the internship experience level */
    public InternshipLevel getInternshipLevel() {
        return internshipLevel;
    }

    /** @param internshipLevel updates the internship level */
    public void setInternshipLevel(InternshipLevel internshipLevel) {
        this.internshipLevel = internshipLevel;
    }

    /** @return the preferred major for applicants */
    public Major getPreferredMajor() {
        return preferredMajor;
    }

    /** @param preferredMajor updates the preferred major */
    public void setPreferredMajor(Major preferredMajor) {
        this.preferredMajor = preferredMajor;
    }

    /** @return the application opening date */
    public String getApplicationOpeningDate() {
        return applicationOpeningDate;
    }

    /** @param applicationOpeningDate updates the opening date */
    public void setApplicationOpeningDate(String applicationOpeningDate) {
        this.applicationOpeningDate = applicationOpeningDate;
    }

    /** @return the application closing date */
    public String getApplicationClosingDate() {
        return applicationClosingDate;
    }

     /** @param applicationClosingDate updates the closing date */
    public void setApplicationClosingDate(String applicationClosingDate) {
        this.applicationClosingDate = applicationClosingDate;
    }

    /** @return the company name */
    public String getCompanyName() {
        return companyName;
    }

    /** @return the department offering the internship */
    public String getDepartment() {
        return department;
    }

    /** @param department updates the department */
    public void setDepartment(String department) {
        this.department = department;
    }

    /** @return the company representative's user ID */
    public String getCompanyRepInCharge() {
        return companyRepInCharge;
    }

     /** @return total number of available slots */
    public int getNoOfSlots() {
        return noOfSlots;
    }

    /** @param noOfSlots updates the number of slots */
    public void setNoOfSlots(int noOfSlots) {
        this.noOfSlots = noOfSlots;
    }

    /** @return the current approval or filled status */
    public Status getStatus() {
        return status;
    }

     /** @return visibility of the opportunity */
    public Boolean getVisible() {
        return visible;
    }

     /** @return visibility of the opportunity */
    public ArrayList<Application> getApplicationList() {
        return applicationList;
    }

    /** @param status updates the opportunity status */
    public void setStatus(Status status) {
        this.status = status;
    }

    /** @param noOfSlots alias to update slot count */
    public void setSlots(int noOfSlots){
        this.noOfSlots=noOfSlots;
    }

     /**
     * Toggles visibility of the opportunity.
     * <p>If visible, it becomes hidden; if hidden, it becomes visible.</p>
     */
    public void toggleVisibility(){
        if(visible){
            visible=false;
        }
        else{
            visible = true;
        }
    }

    /**
     * Adds a new student application to the opportunity.
     *
     * @param application the application to add
     */
    public void addApplication(Application application){
        applicationList.add(application);
    }
}
