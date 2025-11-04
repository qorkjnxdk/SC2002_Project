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
    private ArrayList<Application> applicationList;

    public InternshipOpportunity(String internshipTitle, String description, InternshipLevel internshipLevel, Major preferredMajor, String applicationOpeningDate,String applicationClosingDate,String companyName,String department,String companyRep,int noOfSlots, ArrayList<Application> applicationList) {
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
        this.status = Status.PENDING;
        this.applicationList = applicationList;
    }

    public String getInternshipTitle() {
        return internshipTitle;
    }
    public String getDescription() {
        return description;
    }
    public InternshipLevel getInternshipLevel() {
        return internshipLevel;
    }
    public Major getPreferredMajor() {
        return preferredMajor;
    }
    public String getApplicationOpeningDate() {
        return applicationOpeningDate;
    }
    public String getApplicationClosingDate() {
        return applicationClosingDate;
    }
    public String getCompanyName() {
        return companyName;
    }
    public String getDepartment() {
        return department;
    }
    public String getCompanyRepInCharge() {
        return companyRepInCharge;
    }
    public int getNoOfSlots() {
        return noOfSlots;
    }
    public Status getStatus() {
        return status;
    }
    public ArrayList<Application> getApplicationList() {
        return applicationList;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public void addApplication(Application application){
        applicationList.add(application);
    }
}
