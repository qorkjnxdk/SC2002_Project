package entities;
import java.util.ArrayList;

public class InternshipOpportunity{
    private String internshipTitle;
    private String description;
    public enum InternshipLevel {BASIC,INTERMEDIATE,ADVANCED};
    public enum Majors {MATH, ENGINEERING, CHEMISTRY};
    public enum Status {PENDING, APPROVED, REJECTED, FILLED}
    private InternshipLevel internshipLevel;
    private ArrayList<Majors> preferredMajors;
    private String applicationOpeningDate; 
    private String applicationClosingDate; 
    private String companyName;
    private String department;
    private ArrayList<CompanyRep> companyRepsInCharge;
    private int noOfSlots;
    public InternshipOpportunity(String name, String companyName, String department, String position){
        this.companyName = companyName;
        this.department = department;
    }
    public String getCompanyName(){
        return companyName;
    }
    public String getDepartment(){
        return department;
    }
}
