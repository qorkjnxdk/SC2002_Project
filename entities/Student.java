package entities;

import entities.InternshipOpportunity.Major;

public class Student extends User{
    private int yearOfStudy;
    private Major major;
    private Boolean acceptance_status;
    private String email;
    public Student(String userID, String name, String major,int yearOfStudy, String email, String password){
        super(userID, name, password);
        this.role = Role.STUDENT;
        this.yearOfStudy = yearOfStudy;
        this.email = email;
        this.acceptance_status = false;
        this.major = switch(major){
            case "CCDS" -> Major.CCDS;
            case "IEEE" -> Major.IEEE;
            case "DSAI" -> Major.DSAI;
            default -> Major.CCDS;
        };
    }
    public int getYearOfStudy(){
        return yearOfStudy;
    }
    public Major getMajor(){
        return major;
    }
    public String getEmail(){
        return email;
    }
    public Boolean getAcceptanceStatus(){
        return acceptance_status;
    }
}
