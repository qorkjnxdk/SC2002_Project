package entities;

import java.util.ArrayList;

public class InternshipOpportunity{
    private String internshipTitle;
    private String description;
    public enum InternshipLevel {BASIC, INTERMEDIATE, ADVANCED};
    public enum Major {CCDS, IEEE, DSAI};
    public enum Status {PENDING, APPROVED, REJECTED, FILLED};
    private InternshipLevel internshipLevel;
    private Major preferredMajor;
    private String applicationOpeningDate;
    private String applicationClosingDate;
    private String companyName;
    private String department;
    private String companyRepInCharge;
    private int noOfSlots;
    private Status status;
    private Boolean visible;
    private ArrayList<Application> applicationList;

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

    public String getInternshipTitle() {
        return internshipTitle;
    }

    public void setInternshipTitle(String internshipTitle) {
        this.internshipTitle = internshipTitle;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public InternshipLevel getInternshipLevel() {
        return internshipLevel;
    }
    public void setInternshipLevel(InternshipLevel internshipLevel) {
        this.internshipLevel = internshipLevel;
    }
    public Major getPreferredMajor() {
        return preferredMajor;
    }
    public void setPreferredMajor(Major preferredMajor) {
        this.preferredMajor = preferredMajor;
    }
    public String getApplicationOpeningDate() {
        return applicationOpeningDate;
    }
    public void setApplicationOpeningDate(String applicationOpeningDate) {
        this.applicationOpeningDate = applicationOpeningDate;
    }
    public String getApplicationClosingDate() {
        return applicationClosingDate;
    }
    public void setApplicationClosingDate(String applicationClosingDate) {
        this.applicationClosingDate = applicationClosingDate;
    }
    public String getCompanyName() {
        return companyName;
    }
    public String getDepartment() {
        return department;
    }
    public void setDepartment(String department) {
        this.department = department;
    }
    public String getCompanyRepInCharge() {
        return companyRepInCharge;
    }
    public int getNoOfSlots() {
        return noOfSlots;
    }
    public void setNoOfSlots(int noOfSlots) {
        this.noOfSlots = noOfSlots;
    }
    public Status getStatus() {
        return status;
    }
    public Boolean getVisible() {
        return visible;
    }
    public ArrayList<Application> getApplicationList() {
        return applicationList;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public void setSlots(int noOfSlots){
        this.noOfSlots=noOfSlots;
    }
    public void toggleVisibility(){
        if(visible){
            visible=false;
        }
        else{
            visible = true;
        }
    }
    public void addApplication(Application application){
        applicationList.add(application);
    }
}
