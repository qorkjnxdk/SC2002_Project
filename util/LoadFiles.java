package util;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import entities.User.Role;
import entities.User;
import entities.CareerStaff;
import entities.CompanyRepCreationReq;
import entities.Student;
import repositories.UserRepository;
import repositories.RequestRepository;

public class LoadFiles {
    public void loadStudentCSV(UserRepository userRepository){
        UserFactory userFactory = new UserFactory();
        String studentListCSVFilePath = "data/sample_student_list.csv";
        String line;
        try(BufferedReader br = new BufferedReader(new FileReader(studentListCSVFilePath))){
            line = br.readLine();
            while ((line = br.readLine())!=null){
                User user = userFactory.addUser(line, Role.STUDENT);
                Student student = (Student)user;
                userRepository.addStudent(student);
            }
        } catch (IOException e){
            e.printStackTrace();
        } 
    }
    public void loadCareerStaffCSV(UserRepository userRepository){
        UserFactory userFactory = new UserFactory();
        String staffListCSVFilePath = "data/sample_staff_list.csv";
        String line;
        try(BufferedReader br = new BufferedReader(new FileReader(staffListCSVFilePath))){
            line = br.readLine();
            while ((line = br.readLine())!=null){
                User user = userFactory.addUser(line, Role.CAREER_STAFF);
                CareerStaff careerStaff = (CareerStaff)user;
                userRepository.addCareerStaff(careerStaff);
            }
        } catch (IOException e){
            e.printStackTrace();
        } 
    }
    public void loadCompanyRepCSV(RequestRepository requestRepository){
        String companyRepListCSVFilePath = "data/sample_company_representative_list.csv";
        UserFactory userFactory = new UserFactory();
        String line;
        try(BufferedReader br = new BufferedReader(new FileReader(companyRepListCSVFilePath))){
            line = br.readLine();
            while ((line = br.readLine())!=null){
                CompanyRepCreationReq companyRepCreationReq = userFactory.addCompanyRepCreationReq(line);
                requestRepository.addCompanyReqCreationReq(companyRepCreationReq);
            }
        } catch (IOException e){
            e.printStackTrace();
        } 
    }
}
