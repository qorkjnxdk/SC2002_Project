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

/**
 * Controller responsible for all student-related operations in the internship system.
 *
 * <p>This includes:
 * <ul>
 *     <li>Retrieving available, relevant, and applied internship opportunities</li>
 *     <li>Handling internship applications and acceptances</li>
 *     <li>Creating internship withdrawal requests</li>
 *     <li>Filtering opportunities based on student-defined criteria</li>
 *     <li>Persisting all student-related data</li>
 * </ul>
 *
 * <p>This controller acts as the logic layer used by {@code StudentView}.
 */
public class StudentController {
     /** Repository containing student and other user records. */
    private UserRepository users;
    /** Repository containing all internship opportunities in the system. */
    private OpportunityRepository opportunities;
    /** Repository containing all pending withdrawal and request records. */
    private RequestRepository requests;
    /**
     * Constructs a StudentController using the required repositories.
     *
     * @param requests      repository storing internship withdrawal requests
     * @param users         repository storing user information
     * @param opportunities repository storing internship opportunities
     */
    public StudentController(RequestRepository requests, UserRepository users, OpportunityRepository opportunities){
        this.requests = requests;
        this.users = users;
        this.opportunities = opportunities;
    }
     /**
     * Retrieves all internship opportunities in the system.
     *
     * @return list of {@link InternshipOpportunity}
     */
    public ArrayList<InternshipOpportunity> getInternshipOpportunityList(){
        return opportunities.getInternshipOpportunityList();
    }

    /**
     * Retrieves all internship opportunities that are available for a given student.
     *
     * <p>An opportunity is considered available if:
     * <ul>
     *     <li>It matches the student's major and internship level eligibility</li>
     *     <li>The student has not already applied for it</li>
     *     <li>The opportunity is visible</li>
     *     <li>The application window is currently open</li>
     * </ul>
     *
     * @param student the student searching for opportunities
     * @return list of available {@link InternshipOpportunity}
     */
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

    /**
     * Retrieves withdrawal requests submitted by a specific student.
     *
     * @param userID the student's ID
     * @return list of {@link InternshipWithdrawalReq} submitted by the student
     */
    public ArrayList<InternshipWithdrawalReq> getInternshipWithdrawalReqList(String UserID){
        ArrayList<InternshipWithdrawalReq> reqs = requests.getInternshipWithdrawalReqList();
        ArrayList<InternshipWithdrawalReq> filteredReqs = reqs.stream()
        .filter(req->req.getUserID().equals(UserID))
        .collect(Collectors.toCollection(ArrayList::new));
        return filteredReqs;
    }

    /**
     * Retrieves internship opportunities relevant to a student's major and year.
     *
     * <p>Eligibility rules:
     * <ul>
     *     <li>Year 1–2 students → BASIC opportunities only</li>
     *     <li>Year 3+ students → All levels matching major</li>
     * </ul>
     *
     * @param student the student whose eligibility is checked
     * @return list of relevant {@link InternshipOpportunity}
     */
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

     /**
     * Creates a pending application for a student for a specific opportunity.
     *
     * @param student     the student applying
     * @param opportunity the internship opportunity
     */
    public void applyInternship(Student student, InternshipOpportunity opportunity){
        String appliedDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        Application studentApp = new Application(student.getUserId(), appliedDate, Application.ApplicationStatus.PENDING);
        opportunity.addApplication(studentApp);
    }

    /**
     * Accepts an internship opportunity for a student and withdraws all other applications.
     *
     * <p>Steps performed:
     * <ol>
     *     <li>Find the student's application for the selected opportunity</li>
     *     <li>Mark it as ACCEPTED</li>
     *     <li>Update the opportunity's status to FILLED if all slots are taken</li>
     *     <li>Withdraw all other applications by the same student</li>
     * </ol>
     *
     * @param opportunity the internship being accepted
     * @param userId      the student ID
     */
    public void acceptOpportunity(InternshipOpportunity opportunity, String UserId){
        Optional<Application> application = opportunity.getApplicationList().stream()
            .filter(app -> app.getStudentID().equals(UserId))
            .findFirst();
        if (application.isPresent()) {
            Application app = application.get();
            app.setApplicationStatus(Application.ApplicationStatus.ACCEPTED);
            long acceptedCount = opportunity.getApplicationList().stream()
                .filter(a -> a.getApplicationStatus() == ApplicationStatus.ACCEPTED)
                .count();
            if (acceptedCount >= opportunity.getNoOfSlots()) {
                opportunity.setStatus(InternshipOpportunity.Status.FILLED);
            }
        }
        ArrayList<InternshipOpportunity> opps = getAppliedInternshipOpportunityList(UserId);
        for(InternshipOpportunity opp:opps){
            if(!(opp.getInternshipTitle().equals(opportunity.getInternshipTitle()) &&
                 opp.getCompanyName().equals(opportunity.getCompanyName()) &&
                 opp.getDepartment().equals(opportunity.getDepartment()))){
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

     /**
     * Submits a new internship withdrawal request for a student.
     *
     * @param userID     the student ID
     * @param opportunity the opportunity the student wishes to withdraw from
     * @param reason      the withdrawal reason
     */
    public void withdrawRequest(String UserID, InternshipOpportunity opportunity, String reason){
        InternshipWithdrawalReq req = new InternshipWithdrawalReq(UserID, opportunity.getInternshipTitle(), opportunity.getCompanyName(), reason);
        requests.addInternshipWithdrawalReq(req);
    }

    /**
     * Retrieves all internship opportunities the student has applied to.
     *
     * @param userID the student ID
     * @return list of applied {@link InternshipOpportunity}
     */
    public ArrayList<InternshipOpportunity> getAppliedInternshipOpportunityList(String userID){
        ArrayList<InternshipOpportunity> opps = opportunities.getInternshipOpportunityList();
        ArrayList<InternshipOpportunity> appliedOpps = opps.stream()
        .filter(s->s.getApplicationList().stream()
            .anyMatch(app->app.getStudentID().equals(userID)))
        .collect(Collectors.toCollection(ArrayList::new));
        return appliedOpps;
    }

     /**
     * Retrieves all opportunities applied by a student with a specific application status.
     *
     * @param userId student ID
     * @param status desired application status
     * @return list of {@link InternshipOpportunity} matching the status
     */
    public ArrayList<InternshipOpportunity> getOppByStatus(String userId, ApplicationStatus Status){
    ArrayList<InternshipOpportunity> opps = opportunities.getInternshipOpportunityList();
        ArrayList<InternshipOpportunity> appliedOpps = opps.stream()
        .filter(s->s.getApplicationList().stream()
            .anyMatch(app->app.getStudentID().equals(userId)&&app.getApplicationStatus().equals(Status))
            )
        .collect(Collectors.toCollection(ArrayList::new));
        return appliedOpps;
    }

    /**
     * Saves all student-related data to persistent storage.
     *
     * <p>This includes:
     * <ul>
     *     <li>Internship withdrawal requests</li>
     *     <li>Internship opportunities</li>
     *     <li>Student account data</li>
     * </ul>
     */
    public void saveFiles(){
        SaveFiles saveFiles = new SaveFiles();
        saveFiles.saveInternshipWithdrawalRequests(requests);
        saveFiles.saveInternshipOpportunityCSV(opportunities);
        saveFiles.saveStudentCSV(users);
    }

     /**
     * Finds a student by their user ID.
     *
     * @param userID the student ID
     * @return the matching {@link Student}, or null if not found
     */
    public Student findStudentByUserID(String userID){
        return users.findStudentByUserID(userID);
    }

    /**
     * Filters internship opportunities based on user-defined criteria.
     *
     * @param filter filter describing desired opportunity attributes
     * @return filtered list of {@link InternshipOpportunity}
     */
    public ArrayList<InternshipOpportunity> filterOpportunities(Filter filter){
        return opportunities.filterOpportunities(filter);
    }
}
