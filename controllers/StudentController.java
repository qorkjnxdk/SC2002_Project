package controllers;

import repositories.UserRepository;
import util.SaveFiles;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import entities.Application;
import entities.InternshipOpportunity;
import entities.InternshipWithdrawalReq;
import entities.Student;
import entities.Application.ApplicationStatus;
import entities.InternshipOpportunity.Major;
import repositories.OpportunityRepository;
import repositories.RequestRepository;

public class StudentController {
    private UserRepository users;
    private OpportunityRepository opportunities;
    private RequestRepository requests;
    public StudentController(RequestRepository requests, UserRepository users, OpportunityRepository opportunities){
        this.requests = requests;
        this.users = users;
        this.opportunities = opportunities;
    }
    public ArrayList<InternshipOpportunity> getInternshipOpportunityList(){
        return opportunities.getInternshipOpportunityList();
    }
    public ArrayList<InternshipWithdrawalReq> getInternshipWithdrawalReqList(String UserID){
        ArrayList<InternshipWithdrawalReq> reqs = requests.getInternshipWithdrawalReqList();
        ArrayList<InternshipWithdrawalReq> filteredReqs = reqs.stream()
        .filter(req->req.getUserID().equals(UserID))
        .collect(Collectors.toCollection(ArrayList::new));
        return filteredReqs;
    }
    public ArrayList<InternshipOpportunity> getRelevantInternshipOpportunityList(Student student){
        ArrayList<InternshipOpportunity> opps = opportunities.getInternshipOpportunityList();
        int yearofstudy = student.getYearOfStudy();
        Major major = student.getMajor();
        
        ArrayList<InternshipOpportunity> filteredOpps = opps.stream()
        .filter(opp -> {
            if (yearofstudy <= 2) {
                return opp.getInternshipLevel() == InternshipOpportunity.InternshipLevel.BASIC 
                    && opp.getPreferredMajor() == major;
            }           
            else {
                return opp.getPreferredMajor() == major;
            }
        })
        .collect(Collectors.toCollection(ArrayList::new));        
        return filteredOpps;
    }

    public void applyInternship(Student student, InternshipOpportunity opportunity){
        String appliedDateTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        Application studentApp = new Application(student.getUserId(), appliedDateTime, Application.ApplicationStatus.PENDING);
        opportunity.addApplication(studentApp);
    }

    public void acceptOpportunity(InternshipOpportunity opportunity, String UserId){
        Optional<Application> application = opportunity.getApplicationList().stream()
            .filter(app -> app.getStudentID().equals(UserId))
            .findFirst();
        if (application.isPresent()) {
            Application app = application.get();
            app.setApplicationStatus(Application.ApplicationStatus.ACCEPTED);
            opportunity.setSlots(opportunity.getNoOfSlots() - 1);
            if (opportunity.getNoOfSlots() == 0) {
                opportunity.setStatus(InternshipOpportunity.Status.FILLED);
            }
        }
    }

    public void withdrawRequest(String UserID, InternshipOpportunity opportunity, String reason){
        InternshipWithdrawalReq req = new InternshipWithdrawalReq(UserID, opportunity.getInternshipTitle(), opportunity.getCompanyName(), reason);
        requests.addInternshipWithdrawalReq(req);
    }
    public ArrayList<InternshipOpportunity> getAppliedInternshipOpportunityList(String userID){
        ArrayList<InternshipOpportunity> opps = opportunities.getInternshipOpportunityList();
        ArrayList<InternshipOpportunity> appliedOpps = opps.stream()
        .filter(s->s.getApplicationList().stream()
            .anyMatch(app->app.getStudentID().equals(userID)))
        .collect(Collectors.toCollection(ArrayList::new));
        return appliedOpps;
    }
    public void saveFiles(){
        SaveFiles saveFiles = new SaveFiles();
        saveFiles.saveInternshipWithdrawalRequests(requests);
        saveFiles.saveInternshipOpportunityCSV(opportunities);
    }
    public Student findStudentByUserID(String userID){
        return users.findStudentByUserID(userID);
    }
}
