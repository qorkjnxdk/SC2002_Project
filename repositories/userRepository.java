package repositories;

import java.util.ArrayList;
import entities.Student;
import entities.CompanyRep;
import entities.CareerStaff;
import entities.User;

/**
 * Repository class responsible for storing and managing all user-related data in the system.
 *
 * <p>This includes three distinct user types:</p>
 * <ul>
 *     <li>{@link Student} – Students applying for internships</li>
 *     <li>{@link CompanyRep} – Company representatives in charge of internship postings</li>
 *     <li>{@link CareerStaff} – NTU career centre staff responsible for approvals</li>
 * </ul>
 *
 * <p>The repository provides methods to add new users and search for users by User ID.
 * It is used heavily by:</p>
 * <ul>
 *     <li>{@link controllers.AuthController} – during login</li>
 *     <li>{@link controllers.CareerStaffController}</li>
 *     <li>{@link controllers.CompanyRepController}</li>
 *     <li>{@link controllers.StudentController}</li>
 * </ul>
 *
 * <p>Internally, each user group is stored in its own {@code ArrayList}.</p>
 */
public class UserRepository {
    private ArrayList<Student> studentList = new ArrayList<>();
    private ArrayList<CompanyRep> companyRepList = new ArrayList<>();
    private ArrayList<CareerStaff> careerStaffList = new ArrayList<>();

    public UserRepository(){}

     /**
     * Returns the list of all registered students.
     *
     * @return list of students
     */
    public ArrayList<Student> getStudentList(){
        return studentList;
    }

    /**
     * Adds a new student to the repository.
     *
     * @param student the student to add
     */
    public void addStudent(Student student){
        studentList.add(student);
    }

     /**
     * Searches for a student by their User ID.
     *
     * @param userID the User ID of the student
     * @return the matching Student object, or {@code null} if not found
     */
    public Student findStudentByUserID(String userID){
        for(Student student : studentList){
            if(student.getUserId().equals(userID)){
                return student;
            }
        }
        return null;
    }

    /**
     * Returns the list of all registered career staff members.
     *
     * @return list of career staff
     */
    public ArrayList<CareerStaff> getCareerStaffList(){
        return careerStaffList;
    }

     /**
     * Adds a new career staff member to the repository.
     *
     * @param careerStaff the staff member to add
     */
    public void addCareerStaff(CareerStaff careerStaff){
        careerStaffList.add(careerStaff);
    }

    /**
     * Searches for a career staff user by User ID.
     *
     * @param userID the User ID to search for
     * @return the matching CareerStaff object, or {@code null} if not found
     */
    public User findCareerStaffByUserID(String userID){
        for(CareerStaff careerStaff : careerStaffList){
            if(careerStaff.getUserId().equals(userID)){
                return careerStaff;
            }
        }
        return null;
    }

    /**
     * Returns the list of all registered company representatives.
     *
     * @return list of company representatives
     */
    public ArrayList<CompanyRep> getCompanyRepList(){
        return companyRepList;
    }

    /**
     * Adds a new company representative to the repository.
     *
     * @param companyRep the company representative to add
     */
    public void addCompanyRep(CompanyRep companyRep){
        companyRepList.add(companyRep);
    }

     /**
     * Searches for a company representative by User ID.
     *
     * @param userID the User ID to search for
     * @return the matching CompanyRep object, or {@code null} if not found
     */
    public User findCompanyRepByUserID(String userID){
        for(CompanyRep companyRep : companyRepList){
            if(companyRep.getUserId().equals(userID)){
                return companyRep;
            }
        }
        return null;
    }

}
