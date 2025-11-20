package entities;

/**
 * Represents a student's application for an internship opportunity.
 *
 * <p>This class stores:
 * <ul>
 *     <li>The student ID of the applicant</li>
 *     <li>The date the student applied</li>
 *     <li>The current application status</li>
 * </ul>
 *
 * <p>Each application is uniquely tied to a specific internship opportunity
 * through the opportunity's application list.
 */
public class Application {
    /** The ID of the student who submitted the application. */
    private String studentID;
    /** The date when the application was submitted (ISO-8601 format: YYYY-MM-DD). */
    private String appliedDateTime;
    /**
     * Represents the status of a student's internship application.
     */
    public enum ApplicationStatus {PENDING, REJECTED, SUCCESSFUL, ACCEPTED, WITHDRAWN};
    private ApplicationStatus applicationStatus;
    /**
     * Constructs a new Application instance.
     *
     * @param studentID          the ID of the student applying
     * @param appliedDateTime    the date the application was submitted (YYYY-MM-DD)
     * @param applicationStatus  the initial status of the application
     */
    public Application(String studentID, String appliedDateTime, ApplicationStatus applicationStatus){
        this.studentID = studentID;
        this.appliedDateTime = appliedDateTime;
        this.applicationStatus = applicationStatus;
    }

    /**
     * Returns the ID of the student who submitted the application.
     *
     * @return the student ID
     */
    public String getStudentID(){
        return studentID;
    }

     /**
     * Returns the date when the student applied.
     *
     * @return the applied date (YYYY-MM-DD)
     */
    public String getAppliedDateTime(){
        return appliedDateTime;
    }

     /**
     * Returns the current status of the application.
     *
     * @return the application status
     */
    public ApplicationStatus getApplicationStatus(){
        return applicationStatus;
    }

    /**
     * Updates the application's status.
     *
     * @param applicationStatus the new status to assign
     */
    public void setApplicationStatus(ApplicationStatus applicationStatus){
        this.applicationStatus = applicationStatus;
    }
}
