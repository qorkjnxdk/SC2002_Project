package controllers;

import repositories.UserRepository;
import util.SaveFiles;

import java.util.ArrayList;
import java.util.stream.Collectors;

import entities.InternshipOpportunity;
import entities.InternshipWithdrawalReq;
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
    public ArrayList<InternshipOpportunity> getRelevantInternshipOpportunityList(){
        //Add Filters
        ArrayList<InternshipOpportunity> opps = opportunities.getInternshipOpportunityList();
        return opps;
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
    }
}
