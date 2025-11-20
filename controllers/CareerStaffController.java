package controllers;

import java.util.ArrayList;
import java.util.Optional;

import entities.CompanyRep;
import entities.CompanyRepCreationReq;
import entities.Filter;
import entities.InternshipWithdrawalReq;
import entities.InternshipOpportunity;
import entities.Application;
import entities.User.Role;
import repositories.OpportunityRepository;
import repositories.RequestRepository;
import repositories.UserRepository;
import util.SaveFiles;
import util.UserFactory;
import entities.User;
import entities.Application.ApplicationStatus;
import entities.InternshipOpportunity.Status;

/**
 * Controller responsible for handling actions performed by Career Centre Staff.
 *
 * <p>This includes:
 * <ul>
 *     <li>Managing company representative account creation requests</li>
 *     <li>Managing internship withdrawal requests</li>
 *     <li>Approving internship opportunities</li>
 *     <li>Filtering opportunities based on criteria</li>
 *     <li>Persisting updated information to storage</li>
 * </ul>
 *
 * <p>This controller acts as the business logic layer used by {@code CareerStaffView}.
 */

public class CareerStaffController {
    /** Repository containing all users. */
    private UserRepository users;
    /** Repository containing all request types (withdrawal, account creation, etc.). */
    private RequestRepository requests;
    /** Repository containing all internship opportunities. */
    private OpportunityRepository opportunities;
     /**
     * Constructs a CareerStaffController with the required repositories.
     *
     * @param requests      repository storing pending requests
     * @param users         repository storing users
     * @param opportunities repository storing internship opportunities
     */
    public CareerStaffController(RequestRepository requests, UserRepository users, OpportunityRepository opportunities){
        this.users = users;
        this.requests = requests;
        this.opportunities = opportunities;
    }

    /**
     * Retrieves all pending company representative account creation requests.
     *
     * @return list of {@link CompanyRepCreationReq}
     */
    public ArrayList<CompanyRepCreationReq> getPendingAccountCreationReqs(){
        return requests.getAllCompanyRepCreationReq();
    }

     /**
     * Retrieves all internship withdrawal requests awaiting approval.
     *
     * @return list of {@link InternshipWithdrawalReq}
     */
    public ArrayList<InternshipWithdrawalReq> getInternshipWithdrawalReqs(){
        return requests.getInternshipWithdrawalReqList();
    }

    /**
     * Retrieves all internship opportunities that have not yet been approved.
     *
     * @return list of pending {@link InternshipOpportunity}
     */
    public ArrayList<InternshipOpportunity> getPendingInternshipOpportunities(){
        return opportunities.getPendingInternshipOpportunities();
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
     * Approves a Company Representative account creation request and creates the account.
     *
     * <p>Steps performed:
     * <ol>
     *     <li>Removes request from pending list</li>
     *     <li>Constructs user data string for {@link UserFactory}</li>
     *     <li>Creates a new {@link CompanyRep}</li>
     *     <li>Adds the new CompanyRep to the user repository</li>
     * </ol>
     *
     * @param req the approved Company Representative account creation request
     */
    public void addCompanyRepAcct(CompanyRepCreationReq req){
        requests.deleteCompanyReqCreationReq(req);
        UserFactory userFactory = new UserFactory();
        String data = req.getUserID() + "," + req.getName() + "," + req.getCompanyName() + "," + req.getDepartment() + "," + req.getDepartment() + ",password";
        User user = userFactory.addUser(data, Role.COMPANY_REP);
        CompanyRep companyRep = (CompanyRep)user;
        users.addCompanyRep(companyRep);
    }

     /**
     * Approves an internship withdrawal request submitted by a student.
     *
     * <p>This method:
     * <ol>
     *     <li>Locates the matching internship opportunity</li>
     *     <li>Finds the student's application for that opportunity</li>
     *     <li>If the student had accepted and the opportunity was filled,
     *         adjusts the status back to APPROVED if slots become available</li>
     *     <li>Marks the application as WITHDRAWN</li>
     *     <li>Deletes the withdrawal request from the repository</li>
     * </ol>
     *
     * @param req the withdrawal request to approve
     */
    public void approveWithrawReq(InternshipWithdrawalReq req){
        ArrayList<InternshipOpportunity> opps = opportunities.getInternshipOpportunityList();
        InternshipOpportunity selected_opp =null;
        Application selected_app =null;
        String studentId = req.getUserID();
        for (InternshipOpportunity opp : opps) {
            if (opp.getInternshipTitle().equals(req.getInternshipTitle())
                && opp.getCompanyName().equals(req.getCompanyName())){
                selected_opp = opp;
                Optional<Application> application = selected_opp.getApplicationList().stream()
                    .filter(app -> app.getStudentID().equals(studentId))
                    .findFirst();
                if (application.isPresent()){
                    selected_app = application.get();
                }
                break;
            }
        }
        if (selected_app.getApplicationStatus() == ApplicationStatus.ACCEPTED) {
            if (selected_opp.getNoOfSlots() > 0 && selected_opp.getStatus() == InternshipOpportunity.Status.FILLED) {
                selected_opp.setStatus(InternshipOpportunity.Status.APPROVED);
            }
        }
        selected_app.setApplicationStatus(ApplicationStatus.WITHDRAWN);
        requests.deleteInternshipWithdrawalReq(req);
    }

    /**
     * Approves an internship opportunity submitted by a company.
     *
     * @param opp the internship opportunity to approve
     */
    public void approveInternshipOpportunity(InternshipOpportunity opp){
        opp.setStatus(Status.APPROVED);
    }

     /**
     * Saves all relevant data to persistent storage.
     *
     * <p>This includes:
     * <ul>
     *     <li>Company Representatives</li>
     *     <li>Company Representative Requests</li>
     *     <li>Internship Opportunities</li>
     *     <li>Internship Withdrawal Requests</li>
     *     <li>Career Staff User Records</li>
     * </ul>
     */
    public void saveFiles(){
        SaveFiles saveFiles = new SaveFiles();
        saveFiles.saveCompanyRepCSV(users);
        saveFiles.saveCompanyRepReqCSV(requests);
        saveFiles.saveInternshipOpportunityCSV(opportunities);
        saveFiles.saveInternshipWithdrawalRequests(requests);
        saveFiles.saveStaffCSV(users);
    }

     /**
     * Filters internship opportunities using the given filter object.
     *
     * @param filter filter criteria to apply
     * @return list of {@link InternshipOpportunity} that match the criteria
     */
    public ArrayList<InternshipOpportunity> filterOpportunities(Filter filter){
        return opportunities.filterOpportunities(filter);
    }
}
