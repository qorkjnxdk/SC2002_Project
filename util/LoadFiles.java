package util;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import entities.User.Role;
import entities.User;
import entities.Application;
import entities.Application.ApplicationStatus;
import entities.CareerStaff;
import entities.CompanyRep;
import entities.CompanyRepCreationReq;
import entities.InternshipOpportunity;
import entities.InternshipWithdrawalReq;
import entities.InternshipOpportunity.InternshipLevel;
import entities.InternshipOpportunity.Major;
import entities.Student;
import repositories.UserRepository;
import repositories.OpportunityRepository;
import repositories.RequestRepository;
import entities.InternshipOpportunity.Status;

/**
 * Utility class responsible for loading all CSV files into the system's repositories.
 *
 * <p>This includes:</p>
 * <ul>
 *     <li>Student accounts</li>
 *     <li>Career staff accounts</li>
 *     <li>Company representative accounts</li>
 *     <li>Company representative account creation requests</li>
 *     <li>Internship opportunities (with deserialized application lists)</li>
 *     <li>Internship withdrawal requests</li>
 * </ul>
 *
 * <p>All CSV files are expected to follow the formats defined by the project.
 * This class is typically called at system startup to populate the repositories
 * before any operations begin.</p>
 */
public class LoadFiles {
    /**
     * Loads all required CSV files into the corresponding repositories.
     *
     * @param users the repository storing user accounts
     * @param requests the repository storing account creation and withdrawal requests
     * @param opportunities the repository storing internship opportunities
     */
    public void loadCSVs(UserRepository users, RequestRepository requests, OpportunityRepository opportunities){
        loadStudentCSV(users);
        loadCareerStaffCSV(users);
        loadCompanyRepCSV(users);
        loadCompanyRepReqCSV(requests);
        loadWithdrawalRequestCSV(requests);
        loadOpportunityCSV(opportunities);
    }
    /**
     * Loads student accounts from {@code data/student_list.csv} into the UserRepository.
     *
     * <p>Each row is delegated to {@link UserFactory} to parse and construct a Student object.</p>
     *
     * @param userRepository the repository where students will be added
     */
    public void loadStudentCSV(UserRepository userRepository){
        UserFactory userFactory = new UserFactory();
        String pathString = "data/student_list.csv";
        String line;
        try(BufferedReader br = new BufferedReader(new FileReader(pathString))){
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
    /**
     * Loads career staff accounts from {@code data/staff_list.csv}.
     *
     * @param userRepository the repository where staff accounts will be added
     */
    public void loadCareerStaffCSV(UserRepository userRepository){
        UserFactory userFactory = new UserFactory();
        String pathString = "data/staff_list.csv";
        String line;
        try(BufferedReader br = new BufferedReader(new FileReader(pathString))){
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
    /**
     * Loads company representative accounts from {@code data/company_rep_list.csv}.
     *
     * @param userRepository the repository where company reps will be added
     */
    public void loadCompanyRepCSV(UserRepository userRepository){
        UserFactory userFactory = new UserFactory();
        String pathString = "data/company_rep_list.csv";
        String line;
        try(BufferedReader br = new BufferedReader(new FileReader(pathString))){
            line = br.readLine();
            while ((line = br.readLine())!=null){
                User user = userFactory.addUser(line, Role.COMPANY_REP);
                CompanyRep companyRep = (CompanyRep)user;
                userRepository.addCompanyRep(companyRep);
            }
        } catch (IOException e){
            e.printStackTrace();
        } 
    }
    
    /**
     * Loads company representative account creation requests
     * from {@code data/company_rep_req_list.csv}.
     *
     * @param requestRepository the repository where the requests will be stored
     */
    public void loadCompanyRepReqCSV(RequestRepository requestRepository){
        String pathString = "data/company_rep_req_list.csv";
        UserFactory userFactory = new UserFactory();
        String line;
        try(BufferedReader br = new BufferedReader(new FileReader(pathString))){
            line = br.readLine();
            while ((line = br.readLine())!=null){
                String[] data = line.split(",");
                CompanyRepCreationReq req = new CompanyRepCreationReq(data[0],data[1],data[2],data[3],data[4]);
                requestRepository.addCompanyReqCreationReq(req);
            }
        } catch (IOException e){
            e.printStackTrace();
        } 
    }

    /**
     * Loads internship opportunities from {@code data/internship_opps.csv},
     * including deserialization of any stored application lists.
     *
     * @param opportunityRepository the repository where opportunities will be added
     */
    public void loadOpportunityCSV(OpportunityRepository opportunityRepository){
        String pathString = "data/internship_opps.csv";
        String line;
        try(BufferedReader br = new BufferedReader(new FileReader(pathString))){
            line = br.readLine();
            while ((line = br.readLine())!=null){
                String[] data = line.split(",", -1);
                InternshipLevel level = InternshipLevel.valueOf(data[2]);
                Major major = Major.valueOf(data[3]);
                Status status = Status.valueOf(data[10]);
                ArrayList<Application> applicationList = new ArrayList<>();
                if(data.length >= 13 && !data[12].isEmpty()){
                    applicationList = deserializeApplications(data[12]);
                }
                InternshipOpportunity internshipOpp = new InternshipOpportunity(data[0], data[1], level, major, data[4], data[5], data[6], data[7], data[8], Integer.parseInt(data[9]),status, Boolean.parseBoolean(data[11]),applicationList);
                opportunityRepository.addInternshipOpportunity(internshipOpp);
            }
        } catch (IOException e){
            e.printStackTrace();
        } 
    }

     /**
     * Converts a serialized application list (stored in CSV as a single field)
     * back into a list of {@link Application} objects.
     *
     * <p>The format for each application entry is:</p>
     * <pre>
     * studentId;appliedDate;STATUS
     * </pre>
     * Entries are separated by {@code |}.
     *
     * @param data the serialized application string
     * @return a list of Application objects
     */
    private ArrayList<Application> deserializeApplications(String data) {
        ArrayList<Application> apps = new ArrayList<>();
        if (data == null || data.isEmpty()) return apps;

        String[] tokens = data.split("\\|");
        for (String token : tokens) {
            String[] parts = token.split(";");
            if (parts.length < 3) continue;
            String studentId = parts[0];
            String appliedDate = parts[1];
            ApplicationStatus level = ApplicationStatus.valueOf(parts[2]);
            Application app = new Application(studentId, appliedDate, level);
            apps.add(app);
        }
        return apps;
    }

    /**
     * Loads internship withdrawal requests from
     * {@code data/withdrawal_req_list.csv}.
     *
     * @param requestRepository the repository where withdrawal requests will be stored
     */
    public void loadWithdrawalRequestCSV(RequestRepository requestRepository){
        String pathString = "data/withdrawal_req_list.csv";
        String line;
        try(BufferedReader br = new BufferedReader(new FileReader(pathString))){
            line = br.readLine();
            while ((line = br.readLine())!=null){
                String[] data = line.split(",");
                InternshipWithdrawalReq withdrawalReq = new InternshipWithdrawalReq(data[0], data[1], data[2], data[3]);
                requestRepository.addInternshipWithdrawalReq(withdrawalReq);
            }
        } catch (IOException e){
            e.printStackTrace();
        } 
    }
}
