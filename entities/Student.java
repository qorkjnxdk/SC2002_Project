package entities;

public class Student extends User{
    private int yearOfStudy;
    private String majors;
    private Boolean acceptance_status;
    public Student(String userID, String name, int yearOfStudy, String majors){
        super(userID, name);
        this.role = Role.STUDENT;
        this.yearOfStudy = yearOfStudy;
        this.majors = majors;
        this.acceptance_status = false;
    }
    public int getYearOfStudy(){
        return yearOfStudy;
    }
    public String getMajors(){
        return majors;
    }
    public Boolean getAcceptanceStatus(){
        return acceptance_status;
    }
}
