package repositories;
import java.util.ArrayList;
import entities.Student;
import entities.CompanyRep;
import entities.CareerStaff;
import entities.User;

public class UserRepository {
    private ArrayList<Student> studentList = new ArrayList<>();
    private ArrayList<CompanyRep> companyRepList = new ArrayList<>();
    private ArrayList<CareerStaff> careerStaffList = new ArrayList<>();
    public UserRepository(){}
    public void viewAllStudents(){
        for(Student student: studentList){
            System.out.println(student.getMajors());
        }
    }
    public void addStudent(Student student){
        studentList.add(student);
    }
    public User findStudentByUserID(String userID){
        for(Student student : studentList){
            if(student.getUserId().equals(userID)){
                return student;
            }
        }
        return null;
    }
    public void viewAllCareerStaffList(){
        for(CareerStaff careerStaff: careerStaffList){
            System.out.println(careerStaff.getStaffDepartment());
        }
    }
    public void addCareerStaff(CareerStaff careerStaff){
        careerStaffList.add(careerStaff);
    }
    public User findCareerStaffByUserID(String userID){
        for(CareerStaff careerStaff : careerStaffList){
            if(careerStaff.getUserId().equals(userID)){
                return careerStaff;
            }
        }
        return null;
    }
    public void viewAllCompanyRepList(){
        for(CompanyRep companyRep: companyRepList){
            System.out.println(companyRep.getCompanyName());
        }
    }
    public void addCompanyRep(CompanyRep companyRep){
        companyRepList.add(companyRep);
    }
    public User findCompanyRepByUserID(String userID){
        for(CompanyRep companyRep : companyRepList){
            if(companyRep.getUserId().equals(userID)){
                return companyRep;
            }
        }
        return null;
    }
}
