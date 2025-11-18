package controllers;
import repositories.*;
import util.SaveFiles;

import java.util.ArrayList;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import entities.Filter;
import entities.InternshipOpportunity;
import entities.Student;
import entities.InternshipOpportunity.Status;

public class CompanyRepController {
    private RequestRepository requests;
    private UserRepository users;
    private OpportunityRepository opportunities;
    public CompanyRepController(RequestRepository requests, UserRepository users, OpportunityRepository opportunities){
        this.requests = requests;
        this.users = users;
        this.opportunities = opportunities;
    }
    public ArrayList<InternshipOpportunity> getRelevantInternshipOpportunities(String userID){
        return opportunities.getRelevantInternshipOpportunities(userID);
    }
    public ArrayList<InternshipOpportunity> getInternshipByStatus(String userID, Status status){
        ArrayList<InternshipOpportunity> oppList = opportunities.getRelevantInternshipOpportunities(userID).stream()
            .filter(opp->opp.getStatus().equals(status))
            .collect(Collectors.toCollection(ArrayList::new));
        return oppList;
    }
    public ArrayList<InternshipOpportunity> getNoOfApplications(String userID){
        return opportunities.getRelevantInternshipOpportunities(userID);
    }
    public ArrayList<InternshipOpportunity> getInternshipOpportunityList(){
        return opportunities.getInternshipOpportunityList();
    }
    public void addInternshipOpportunity(InternshipOpportunity internshipOpportunity){
        opportunities.addInternshipOpportunity(internshipOpportunity);
    }
    public void saveFiles(){
        SaveFiles saveFiles = new SaveFiles();
        saveFiles.saveInternshipOpportunityCSV(opportunities);
        saveFiles.saveCompanyRepCSV(users);
    }
    public Student findStudentByUserID(String userID){
        return users.findStudentByUserID(userID);
    }
    public ArrayList<InternshipOpportunity> filterOpportunities(Filter filter){
        return opportunities.filterOpportunities(filter);
    }
}
