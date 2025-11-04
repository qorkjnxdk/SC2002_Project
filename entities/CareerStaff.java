package entities;

public class CareerStaff extends User{
    private String staffDepartment;
    private String email;
    public CareerStaff(String userID, String name, String staffDepartment, String email, String password){
        super(userID, name, password);
        this.role = Role.CAREER_STAFF;
        this.staffDepartment = staffDepartment;
        this.email = staffDepartment;
    }
    public String getStaffDepartment(){
        return staffDepartment;
    }
    public String getEmail(){
        return email;
    }
}
