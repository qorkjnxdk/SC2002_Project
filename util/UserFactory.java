package util;
import entities.CareerStaff;
import entities.CompanyRep;
import entities.CompanyRepCreationReq;
import entities.Student;
import entities.User.Role;
import entities.User;

public class UserFactory {
    public UserFactory(){

    }
    public User addUser(String line, Role role){
        switch (role) {
            case STUDENT:
                String[] data = line.split(",");
                int yearOfStudy = Integer.valueOf(data[3]);
                return new Student(data[0],data[1],yearOfStudy,data[2]);
            case CAREER_STAFF:
                String[] data2 = line.split(",");
                return new CareerStaff(data2[0],data2[1],data2[3],data2[4]);
            default:
                throw new IllegalArgumentException("Unknown role: " + role);        
        } 
    }

    public CompanyRep addCompanyRep(CompanyRepCreationReq req){
        return new CompanyRep(req.getUserID(), req.getName(), req.getCompanyName(), req.getDepartment(), req.getPosition());
    }

    public CompanyRepCreationReq addCompanyRepCreationReq(String line){
        String[] data = line.split(",");
        return new CompanyRepCreationReq(data[0],data[1],data[3],data[4],data[5]);
    }
}
