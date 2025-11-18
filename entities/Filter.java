package entities;

import entities.InternshipOpportunity.InternshipLevel;
import entities.InternshipOpportunity.Major;
import entities.InternshipOpportunity.Status;

public class Filter {
    private Status status;
    private Major major;
    private InternshipLevel level ;
    private String earliestOpeningDate;
    private String latestClosingDate;
    public Filter(Status status, Major major, InternshipLevel level, String earliestOpeningDate, String latestClosingDate){
        this.status = status;
        this.major = major;
        this.level = level;
        this.earliestOpeningDate = earliestOpeningDate;
        this.latestClosingDate = latestClosingDate;
    }    
    public Status getStatus() {
        return status;
    }
    public Major getMajor() {
        return major;
    }
    public InternshipLevel getLevel() {
        return level;
    }
    public String getEarliestOpeningDate() {
        return earliestOpeningDate;
    }
    public String getLatestClosingDate() {
        return latestClosingDate;
    }
}
