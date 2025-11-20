package util;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import entities.*;
import repositories.UserRepository;
import repositories.OpportunityRepository;
import repositories.RequestRepository;
import java.util.ArrayList;

/**
 * Utility class responsible for saving all in-memory repository data
 * back into their corresponding CSV files.
 *
 * <p>This class acts as the counterpart to {@link LoadFiles}, ensuring that
 * all user accounts, internship opportunities, and request records are
 * persisted to disk whenever updates occur.</p>
 *
 * <p>The following CSV files are written:</p>
 * <ul>
 *     <li>{@code student_list.csv}</li>
 *     <li>{@code staff_list.csv}</li>
 *     <li>{@code company_rep_list.csv}</li>
 *     <li>{@code company_rep_req_list.csv}</li>
 *     <li>{@code withdrawal_req_list.csv}</li>
 *     <li>{@code internship_opps.csv}</li>
 * </ul>
 *
 * <p>All files are saved using UTF-8 encoding.</p>
 */
public class SaveFiles {
     /**
     * Saves all registered students to {@code data/student_list.csv}.
     *
     * @param userRepository the repository containing the student list
     */
    public void saveStudentCSV(UserRepository userRepository){
        ArrayList<Student> studentList = userRepository.getStudentList();
        String path = "data/student_list.csv";
        Path filePath = Paths.get(path);
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(filePath, StandardCharsets.UTF_8))) {
            pw.println("StudentID,Name,Major,Year,Email,Password");
            for (Student s : studentList) {
                String major = s.getMajor() == null ? "" : s.getMajor().name();
                pw.printf("%s,%s,%s,%d,%s,%s%n",
                s.getUserId(),
                s.getName(),
                major,
                s.getYearOfStudy(),
                s.getEmail(),
                s.getPassword());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves all career staff users to {@code data/staff_list.csv}.
     *
     * @param userRepository the repository containing the staff list
     */
    public void saveStaffCSV(UserRepository userRepository){
        ArrayList<CareerStaff> staffList = userRepository.getCareerStaffList();
        String path = "data/staff_list.csv";
        Path filePath = Paths.get(path);
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(filePath, StandardCharsets.UTF_8))) {
            pw.println("StaffID,Name,Department,Email,Password");
            for (CareerStaff s : staffList) {
                pw.printf("%s,%s,%s,%s,%s%n",
                s.getUserId(),
                s.getName(),
                s.getStaffDepartment(),
                s.getEmail(),
                s.getPassword());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
     /**
     * Saves all career staff users to {@code data/staff_list.csv}.
     *
     * @param userRepository the repository containing the staff list
     */
    public void saveCompanyRepCSV(UserRepository userRepository){
        ArrayList<CompanyRep> companyRepList = userRepository.getCompanyRepList();
        String path = "data/company_rep_list.csv";
        Path filePath = Paths.get(path);
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(filePath, StandardCharsets.UTF_8))) {
            pw.println("CompanyRepID,Name,CompanyName,Department,Position,Password");
            for (CompanyRep s : companyRepList) {
                pw.printf("%s,%s,%s,%s,%s,%s%n",
                s.getUserId(),
                s.getName(),
                s.getCompanyName(),
                s.getDepartment(),
                s.getPosition(),
                s.getPassword());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves all company representatives to {@code data/company_rep_list.csv}.
     *
     * @param userRepository the repository containing the company rep list
     */
    public void saveCompanyRepReqCSV(RequestRepository requestRepository){
        ArrayList<CompanyRepCreationReq> companyRepReqList = requestRepository.getAllCompanyRepCreationReq();
        String path = "data/company_rep_req_list.csv";
        Path filePath = Paths.get(path);
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(filePath, StandardCharsets.UTF_8))) {
            pw.println("CompanyRepID,Name,CompanyName,Department,Position");
            for (CompanyRepCreationReq s : companyRepReqList) {
                pw.printf("%s,%s,%s,%s,%s%n",
                s.getUserID(),
                s.getName(),
                s.getCompanyName(),
                s.getDepartment(),
                s.getPosition());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves all internship withdrawal requests into
     * {@code data/withdrawal_req_list.csv}.
     *
     * @param requestRepository repository containing withdrawal requests
     */
    public void saveInternshipWithdrawalRequests(RequestRepository requestRepository){
        ArrayList<InternshipWithdrawalReq> internshipWithdrawalReqs = requestRepository.getInternshipWithdrawalReqList();
        String path = "data/withdrawal_req_list.csv";
        Path filePath = Paths.get(path);
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(filePath, StandardCharsets.UTF_8))) {
            pw.println("StudentID,InternshipTitle,CompanyName,WithdrawalReason");
            for (InternshipWithdrawalReq s : internshipWithdrawalReqs) {
                pw.printf("%s,%s,%s,%s",
                s.getUserID(),
                s.getInternshipTitle(),
                s.getCompanyName(),
                s.getWithdrawalReason());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves all internship opportunities to {@code data/internship_opps.csv},
     * including a serialized representation of their application lists.
     *
     * @param opportunities repository containing all internship opportunities
     */
    public void saveInternshipOpportunityCSV(OpportunityRepository opportunities){
        ArrayList<InternshipOpportunity> opportunityList = opportunities.getInternshipOpportunityList();
        String path = "data/internship_opps.csv";
        Path filePath = Paths.get(path);
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(filePath, StandardCharsets.UTF_8))) {
            pw.println("Title,Description,Level,PreferredMajor,OpeningDate,ClosingDate,CompanyName,Department,CompanyRepInCharge,Slots,Status,Visible,ApplicationList");
            for (InternshipOpportunity s : opportunityList) {
                String applicationList = serializeApplications(s.getApplicationList());
                String level = s.getInternshipLevel() == null ? "" : s.getInternshipLevel().name();
                String preferredMajors = s.getPreferredMajor() == null ? "" : s.getPreferredMajor().name();
                String status = s.getStatus() == null ? "" : s.getStatus().name();
                pw.printf("%s,%s,%s,%s,%s,%s,%s,%s,%s,%d,%s,%b,%s%n",
                    s.getInternshipTitle(),
                    s.getDescription(),
                    level,
                    preferredMajors,
                    s.getApplicationOpeningDate(),
                    s.getApplicationClosingDate(),
                    s.getCompanyName(),
                    s.getDepartment(),
                    s.getCompanyRepInCharge(),
                    s.getNoOfSlots(),
                    status,
                    s.getVisible(),
                    applicationList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Serializes a list of {@link Application} objects into a single CSV-compatible string.
     *
     * <p>The format for each application entry is:</p>
     * <pre>
     * studentId;appliedDate;STATUS
     * </pre>
     * Entries are separated using {@code |}.
     *
     * @param apps the list of applications to serialize
     * @return a compact string representation of all applications
     */
    private String serializeApplications(ArrayList<Application> apps) {
        if (apps == null || apps.isEmpty()) return "";
        return apps.stream()
            .map(a -> {
                String status = a.getApplicationStatus() == null ? "" : a.getApplicationStatus().name();
                return a.getStudentID() + ";" + a.getAppliedDateTime() + ";" + status;
            })
            .reduce((a, b) -> a + "|" + b)
            .orElse("");
    }
}
