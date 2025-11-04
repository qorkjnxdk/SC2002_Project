package entities;

public class Application {
    private String studentID;
    private String appliedDateTime;
    public enum ApplicationStatus {PENDING, REJECTED, SUCCESSFUL, ACCEPTED, WITHDRAWN};
    private ApplicationStatus applicationStatus;
    public Application(String studentID, String appliedDateTime, ApplicationStatus applicationStatus){
        this.studentID = studentID;
        this.appliedDateTime = appliedDateTime;
        this.applicationStatus = applicationStatus;
    }
    public String getStudentID(){
        return studentID;
    }
    public String getAppliedDateTime(){
        return appliedDateTime;
    }
    public ApplicationStatus getApplicationStatus(){
        return applicationStatus;
    }
    public void setApplicationStatus(ApplicationStatus applicationStatus){
        this.applicationStatus = applicationStatus;
    }
}
