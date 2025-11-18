package controllers;

import repositories.UserRepository;
import util.SaveFiles;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import entities.Application;
import entities.InternshipOpportunity;
import entities.InternshipWithdrawalReq;
import entities.Student;
import entities.Application.ApplicationStatus;
import entities.Filter;
import entities.InternshipOpportunity.Major;
import entities.InternshipOpportunity.Status;
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
    public ArrayList<InternshipOpportunity> getAvailableOpps(Student student){
        String userId = student.getUserId();
        String currentDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        ArrayList<InternshipOpportunity> opportunityList = getRelevantInternshipOpportunityList(student);
        ArrayList<InternshipOpportunity> appliedList = getAppliedInternshipOpportunityList(userId);
        ArrayList<InternshipOpportunity> availableList = opportunityList.stream()
            .filter(opp -> appliedList.stream().noneMatch(a ->
                a.getInternshipTitle().equals(opp.getInternshipTitle()) &&
                a.getCompanyName().equals(opp.getCompanyName()) &&
                a.getDepartment().equals(opp.getDepartment())
            ))
            .filter(opp->opp.getVisible().equals(true))
            .filter(opp->opp.getApplicationOpeningDate().compareTo(currentDate) <= 0)
            .filter(opp->opp.getApplicationClosingDate().compareTo(currentDate) >= 0)
            .collect(Collectors.toCollection(ArrayList::new));
        return availableList;
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
        System.out.println(opps.size());
        int yearofstudy = student.getYearOfStudy();
        Major major = student.getMajor();
        ArrayList<InternshipOpportunity> filteredOpps = opps.stream()
        .filter(s->s.getStatus().equals(Status.APPROVED))
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
        String appliedDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        Application studentApp = new Application(student.getUserId(), appliedDate, Application.ApplicationStatus.PENDING);
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
            if (opportunity.getNoOfSlots() <= 0) {
                opportunity.setStatus(InternshipOpportunity.Status.FILLED);
            }
        }
        ArrayList<InternshipOpportunity> opps = getAppliedInternshipOpportunityList(UserId);
        for(InternshipOpportunity opp:opps){
            if(!(opp.equals(opportunity))){
                Optional<Application> application2 = opp.getApplicationList().stream()
                    .filter(app -> app.getStudentID().equals(UserId))
                    .findFirst();
                if (application2.isPresent()) {
                    Application app = application2.get();
                    app.setApplicationStatus(Application.ApplicationStatus.WITHDRAWN);
                }
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
    public ArrayList<InternshipOpportunity> getOppByStatus(String userId, ApplicationStatus Status){
    ArrayList<InternshipOpportunity> opps = opportunities.getInternshipOpportunityList();
        ArrayList<InternshipOpportunity> appliedOpps = opps.stream()
        .filter(s->s.getApplicationList().stream()
            .anyMatch(app->app.getStudentID().equals(userId)&&app.getApplicationStatus().equals(Status))
            )
        .collect(Collectors.toCollection(ArrayList::new));
        return appliedOpps;
    }
    public void saveFiles(){
        SaveFiles saveFiles = new SaveFiles();
        saveFiles.saveInternshipWithdrawalRequests(requests);
        saveFiles.saveInternshipOpportunityCSV(opportunities);
        saveFiles.saveStudentCSV(users);
    }
    public Student findStudentByUserID(String userID){
        return users.findStudentByUserID(userID);
    }
    public ArrayList<InternshipOpportunity> filterOpportunities(Filter filter){
        return opportunities.filterOpportunities(filter);
    }
}
