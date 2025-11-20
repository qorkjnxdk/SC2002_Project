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

/**
 * Controller responsible for handling all business logic related to
 * Company Representatives.
 *
 * <p>This controller allows Company Reps to:
 * <ul>
 *     <li>View internship opportunities they created</li>
 *     <li>Filter opportunities by status</li>
 *     <li>Create new internship opportunities</li>
 *     <li>Access student information for applications</li>
 *     <li>Persist updated internship and user data</li>
 * </ul>
 *
 * <p>It serves as the logic layer between the CompanyRepView and the underlying repositories.
 */
public class CompanyRepController {
    /** Repository containing pending requests (not heavily used here). */
    private RequestRepository requests;
    /** Repository storing all user information, including students and company reps. */
    private UserRepository users;
    /** Repository storing all internship opportunities. */
    private OpportunityRepository opportunities;

    /**
     * Constructs a CompanyRepController with the required repositories.
     *
     * @param requests      repository for pending requests
     * @param users         repository containing user information
     * @param opportunities repository containing internship opportunities
     */
    public CompanyRepController(RequestRepository requests, UserRepository users, OpportunityRepository opportunities){
        this.requests = requests;
        this.users = users;
        this.opportunities = opportunities;
    }

    /**
     * Retrieves all internship opportunities created by a specific Company Representative.
     *
     * @param userID ID of the Company Representative
     * @return list of relevant {@link InternshipOpportunity}
    */
    public ArrayList<InternshipOpportunity> getRelevantInternshipOpportunities(String userID){
        return opportunities.getRelevantInternshipOpportunities(userID);
    }

    /**
     * Retrieves internship opportunities created by the Company Rep that match a specific status.
     *
     * @param userID ID of the Company Representative
     * @param status opportunity status to filter by
     * @return list of {@link InternshipOpportunity} with the given status
     */
    public ArrayList<InternshipOpportunity> getInternshipByStatus(String userID, Status status){
        ArrayList<InternshipOpportunity> oppList = opportunities.getRelevantInternshipOpportunities(userID).stream()
            .filter(opp->opp.getStatus().equals(status))
            .collect(Collectors.toCollection(ArrayList::new));
        return oppList;
    }

     /**
     * Retrieves opportunities created by the Company Rep.
     * (Method name suggests count, but currently returns the full list.)
     *
     * @param userID ID of the Company Representative
     * @return list of {@link InternshipOpportunity}
     */
    public ArrayList<InternshipOpportunity> getNoOfApplications(String userID){
        return opportunities.getRelevantInternshipOpportunities(userID);
    }

    /**
     * Retrieves all internship opportunities in the system.
     *
     * @return list of all {@link InternshipOpportunity}
     */
    public ArrayList<InternshipOpportunity> getInternshipOpportunityList(){
        return opportunities.getInternshipOpportunityList();
    }

    /**
     * Adds a new internship opportunity created by the Company Rep.
     *
     * @param internshipOpportunity the opportunity to add
     */
    public void addInternshipOpportunity(InternshipOpportunity internshipOpportunity){
        opportunities.addInternshipOpportunity(internshipOpportunity);
    }

     /**
     * Saves updated internship opportunities and company representative data
     * to persistent storage.
     *
     * <p>This method should be called when the Company Rep logs out
     * or after major modifications.
     */
    public void saveFiles(){
        SaveFiles saveFiles = new SaveFiles();
        saveFiles.saveInternshipOpportunityCSV(opportunities);
        saveFiles.saveCompanyRepCSV(users);
    }

    /**
     * Finds a student by user ID.
     *
     * @param userID the ID of the student
     * @return the matching {@link Student}, or null if not found
     */
    public Student findStudentByUserID(String userID){
        return users.findStudentByUserID(userID);
    }

    /**
     * Filters internship opportunities based on the given filter criteria.
     *
     * @param filter the filter conditions (status, level, major, dates, etc.)
     * @return list of filtered {@link InternshipOpportunity}
     */
    public ArrayList<InternshipOpportunity> filterOpportunities(Filter filter){
        return opportunities.filterOpportunities(filter);
    }
}
