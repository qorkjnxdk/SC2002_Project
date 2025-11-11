package controllers;
import repositories.*;
import util.SaveFiles;

import java.util.ArrayList;

import entities.InternshipOpportunity;
import entities.Student;

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
    public ArrayList<InternshipOpportunity> getInternshipOpportunityList(){
        return opportunities.getInternshipOpportunityList();
    }
    public void addInternshipOpportunity(InternshipOpportunity internshipOpportunity){
        opportunities.addInternshipOpportunity(internshipOpportunity);
    }
    public void saveFiles(){
        SaveFiles saveFiles = new SaveFiles();
        saveFiles.saveInternshipOpportunityCSV(opportunities);
    }
    public Student findStudentByUserID(String userID){
        return users.findStudentByUserID(userID);
    }
}
