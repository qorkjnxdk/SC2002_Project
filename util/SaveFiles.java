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

public class SaveFiles {
    public void saveStudentCSV(UserRepository userRepository){
        ArrayList<Student> studentList = userRepository.getStudentList();
        String path = "data/student_list.csv";
        Path filePath = Paths.get(path);
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(filePath, StandardCharsets.UTF_8))) {
            pw.println("StudentID,Name,Major,Year,Email,Password");
            for (Student s : studentList) {
                String major = s.getMajor() == null ? "" : s.getMajor().name();
                pw.printf("%s,%s,%s,%d,%s%n",
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
    public void saveInternshipOpportunityCSV(OpportunityRepository opportunities){
        ArrayList<InternshipOpportunity> opportunityList = opportunities.getInternshipOpportunityList();
        String path = "data/internship_opps.csv";
        Path filePath = Paths.get(path);
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(filePath, StandardCharsets.UTF_8))) {
            pw.println("Title,Description,Level,PreferredMajor,OpeningDate,ClosingDate,CompanyName,Department,CompanyRepInCharge,Slots,Status,ApplicationList");
            for (InternshipOpportunity s : opportunityList) {
                String applicationList = serializeApplications(s.getApplicationList());
                String level = s.getInternshipLevel() == null ? "" : s.getInternshipLevel().name();
                String preferredMajors = s.getPreferredMajor() == null ? "" : s.getPreferredMajor().name();
                String status = s.getStatus() == null ? "" : s.getStatus().name();
                pw.printf("%s,%s,%s,%s,%s,%s,%s,%s,%s,%d,%s,%s%n",
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
                    applicationList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String serializeApplications(ArrayList<Application> apps) {
        if (apps == null || apps.isEmpty()) return "";
        return apps.stream()
            .map(a -> {
                String status = a.getApplicationStatus() == null ? "" : a.getApplicationStatus().name();
                return a.getStudentID() + ":" + a.getAppliedDateTime() + ":" + status;
            })
            .reduce((a, b) -> a + "|" + b)
            .orElse("");
    }
}
