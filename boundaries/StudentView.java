package boundaries;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;

import controllers.StudentController;
import entities.Application.ApplicationStatus;
import entities.Filter;
import entities.InternshipOpportunity;
import entities.InternshipOpportunity.InternshipLevel;
import entities.InternshipOpportunity.Major;
import entities.InternshipOpportunity.Status;
import entities.InternshipWithdrawalReq;
import entities.Student;
import entities.User;
import util.AppContext;
import util.ComplexityChecker;
import util.ConsoleColors;

/**
 * Boundary/UI class that manages all interactions between the student user
 * and the application.
 *
 * <p>Supports features including:
 * <ul>
 *     <li>Applying for internship opportunities</li>
 *     <li>Viewing applied/withdrawn/accepted opportunities</li>
 *     <li>Submitting withdrawal requests</li>
 *     <li>Viewing student profile</li>
 *     <li>Filtering internship opportunities</li>
 *     <li>Changing password</li>
 *     <li>Logging out</li>
 * </ul>
 *
 * This class connects user input to the underlying business logic in
 * {@link StudentController}.
 */

public class StudentView{
     /**
     * Controller handling business logic for student operations.
     */
    private StudentController studentController;
     /**
     * Constructs a StudentView with the given controller.
     *
     * @param studentController the controller handling student operations
     */
    public StudentView(StudentController studentController){
        this.studentController = studentController;
    };

     /**
     * Main UI loop for student users.
     *
     * <p>Displays a menu and routes input to the appropriate functionality
     * until the student logs out.
     *
     * @param context application context containing the current session
     * @param sc      scanner for reading user input
     */
    public void run(AppContext Context, Scanner sc){
        while (true) {
            System.out.println("\n===============================");
            System.out.println("Welcome Back " + Context.getSession().getUser().getName());
            System.out.println("Please indicate your action:");
            System.out.println("1. Apply for Internships ");
            System.out.println("2. View All Internships You Have Applied For");
            System.out.println("3. View All Pending Withdrawal Requests / Make a Withdrawal Request");
            System.out.println("4. View Your Profile");
            System.out.println("5. Filter All Opportunities");
            System.out.println("6. Change Password");
            System.out.println("7. Logout");
            System.out.println("=================================");
            int choice = sc.nextInt();
            switch (choice) {
                case 1 -> viewRelevantInternshipOpportunities(studentController, sc, Context);
                case 2 -> viewAppliedInternshipOpportunities(studentController, sc, Context);
                case 3 -> viewWithdrawnReqs(studentController, sc, Context);
                case 4 -> viewProfile(studentController, sc, Context);
                case 5 -> filterOpportunities(sc, studentController, Context);
                case 6 -> changePassword(Context, sc, studentController);
                case 7 -> {
                    Context.clearSession();
                    studentController.saveFiles();
                    System.out.println("You have been logged out.");
                    return;
                }
                default -> System.out.println(ConsoleColors.RED+"Invalid choice. Please enter 1-7."+ConsoleColors.RESET);
            }
        }
    }

    /**
     * Displays all internship opportunities in the system.
     *
     * @param studentController controller providing the list of opportunities
     * @param sc                scanner for input (unused but included for consistency)
     */
    public void viewInternshipOpportunities(StudentController studentController, Scanner sc){
        System.out.println("\nAll Internship Opportunities:");
        ArrayList<InternshipOpportunity> opportunityList = studentController.getInternshipOpportunityList();
        for (int i = 0; i < opportunityList.size(); i++) {
            InternshipOpportunity req = opportunityList.get(i);
            System.out.println((i + 1) + ". " + req.getInternshipTitle() + ", "
                + req.getCompanyName() + ", "
                + req.getDepartment());
        }
    }

     /**
     * Displays the current student's profile information.
     *
     * @param studentController controller used for student lookup
     * @param sc                scanner for user input
     * @param context           application context containing session data
     */
    public void viewProfile(StudentController studentController, Scanner sc, AppContext context){
        System.out.println("\nYour Student Profile:");
        String userId = context.getSession().getUserId();
        Student student = studentController.findStudentByUserID(userId);
        System.out.println("Name: " + student.getName());
        System.out.println("Email: " + student.getEmail());
        System.out.println("Major: " + student.getMajor());
        System.out.println("Year Of Study: " + student.getYearOfStudy());
        switch(student.getYearOfStudy()){
            case 1,2 -> System.out.println(ConsoleColors.RED+"You are eligible for Basic Internships only!"+ConsoleColors.RESET);
            default -> System.out.println(ConsoleColors.GREEN+"You are eligible for Basic, Intermediate & Advanced Internships!"+ConsoleColors.RESET);
        }
    }

    /**
     * Displays internship opportunities that match the student's profile and
     * that they are allowed to apply for.
     *
     * <p>This includes:
     * <ul>
     *     <li>Filtering out opportunities already applied to</li>
     *     <li>Checking maximum application limit (3)</li>
     *     <li>Checking if student already accepted an internship</li>
     * </ul>
     *
     * Also handles user selection and application submission.
     *
     * @param studentController controller handling application logic
     * @param sc               scanner for user input
     * @param context          application context containing session data
     */
    public void viewRelevantInternshipOpportunities(StudentController studentController, Scanner sc, AppContext context){
        String userId = context.getSession().getUserId();
        Student student = studentController.findStudentByUserID(userId);
        ArrayList<InternshipOpportunity> appliedList = studentController.getAppliedInternshipOpportunityList(userId);
        ArrayList<InternshipOpportunity> availableList = studentController.getAvailableOpps(student);
        ArrayList<InternshipOpportunity> withdrawnOpps = studentController.getOppByStatus(userId,ApplicationStatus.WITHDRAWN);
        ArrayList<InternshipOpportunity> rejectedOpps = studentController.getOppByStatus(userId,ApplicationStatus.REJECTED);
        ArrayList<InternshipOpportunity> acceptedOpps = studentController.getOppByStatus(userId,ApplicationStatus.ACCEPTED);
        if (acceptedOpps.size()>0){
            InternshipOpportunity opp = acceptedOpps.get(0);
            System.out.println(ConsoleColors.RED+"\nYou have already accepted "+opp.getInternshipTitle() + " from "+opp.getCompanyName()+ConsoleColors.RESET);
            return;
        }
        if (appliedList.size()-withdrawnOpps.size()-rejectedOpps.size()>=3){
            System.out.println(ConsoleColors.RED+"\nYou already have 3 internship applications. You can't apply for anymore!"+ConsoleColors.RESET);
            return;
        }
        if (availableList.isEmpty()){
            System.out.println(ConsoleColors.RESET+"\nYou have no new relevant internship opportunities you can apply for (all have been applied to or none match your profile)."+ConsoleColors.RESET);
            return;
        }
        System.out.println("\nInternship Opportunities You Can Apply For:");
        for (int i = 0; i < availableList.size(); i++) {
            InternshipOpportunity req = availableList.get(i);
            System.out.println((i + 1) + ". " + req.getInternshipTitle() + ", "
                + req.getCompanyName() + ", "
                + req.getDepartment());
        }
        System.out.println("\nWould you like to apply to one of these opportunities? (Y/N)");
        String choice3 = sc.next();
        if (choice3.equalsIgnoreCase("Y")){
            while (true) {
                System.out.println("\nWhich internship do you want to apply for? (enter the index number, or -1 to return)");
                int choice4 = sc.nextInt();
                if (choice4 == -1) {
                    return;
                }
                if (choice4 < 1 || choice4 > availableList.size()){
                    System.out.println(ConsoleColors.RED+"Invalid selection. Please enter a valid index or -1 to return."+ConsoleColors.RESET);
                    continue; 
                }
                InternshipOpportunity selected = availableList.get(choice4 - 1);
                studentController.applyInternship(student,selected);
                System.out.println(ConsoleColors.GREEN+"Applied to: " + selected.getInternshipTitle() + " (" + selected.getCompanyName() + ")"+ConsoleColors.RESET);
                break;
            }
        }
    }

     /**
     * Displays all internships the student has applied for, grouped by:
     * <ul>
     *     <li>Withdrawn applications</li>
     *     <li>Pending applications</li>
     *     <li>Successful applications</li>
     * </ul>
     *
     * Also allows the student to accept successful applications.
     *
     * @param studentController controller retrieving application status data
     * @param sc               scanner for user input
     * @param context          application context
     */
    public void viewAppliedInternshipOpportunities(StudentController studentController, Scanner sc, AppContext context){
        String UserID = context.getSession().getUserId();
        ArrayList<InternshipOpportunity> opportunityList = studentController.getAppliedInternshipOpportunityList(UserID);
        ArrayList<InternshipOpportunity> acceptedOpps = studentController.getOppByStatus(UserID,ApplicationStatus.ACCEPTED);
        if (acceptedOpps.size()>0){
            InternshipOpportunity opp = acceptedOpps.get(0);
            System.out.println(ConsoleColors.RED+"You have already accepted "+opp.getInternshipTitle() + " from "+opp.getCompanyName()+ConsoleColors.RESET);
            return;
        }
        ArrayList<InternshipOpportunity> withdrawnOpps = studentController.getOppByStatus(UserID,ApplicationStatus.WITHDRAWN);
        ArrayList<InternshipOpportunity> rejectedOpps = studentController.getOppByStatus(UserID,ApplicationStatus.REJECTED);
        ArrayList<InternshipOpportunity> successfulOpps = studentController.getOppByStatus(UserID,ApplicationStatus.SUCCESSFUL);
        ArrayList<InternshipOpportunity> pendingOpps = studentController.getOppByStatus(UserID,ApplicationStatus.PENDING);
        if (withdrawnOpps.size()>0){
            System.out.println("Opportunities you have withdrawn from:");
            for (int i = 0; i < withdrawnOpps.size(); i++) {
                InternshipOpportunity opp = withdrawnOpps.get(i);
                System.out.println((i + 1) + ". " + opp.getInternshipTitle() + ", "
                    + opp.getCompanyName() + ", "
                    + opp.getDepartment());
            }   
        }
        if (rejectedOpps.size()>0){
            System.out.println("Opportunities you have been rejected from:");
            for (int i = 0; i < rejectedOpps.size(); i++) {
                InternshipOpportunity opp = rejectedOpps.get(i);
                System.out.println((i + 1) + ". " + opp.getInternshipTitle() + ", "
                    + opp.getCompanyName() + ", "
                    + opp.getDepartment());
            }   
        }
        if (pendingOpps.size()>0){
            System.out.println("Opportunities that have not gotten back:");
            for (int i = 0; i < pendingOpps.size(); i++) {
                InternshipOpportunity opp = pendingOpps.get(i);
                System.out.println((i + 1) + ". " + opp.getInternshipTitle() + ", "
                    + opp.getCompanyName() + ", "
                    + opp.getDepartment());
            }   
        }
        if (successfulOpps.size()>0){
            System.out.println("Opportunities you have been accepted for:");
            for (int i = 0; i < successfulOpps.size(); i++) {
                InternshipOpportunity opp = successfulOpps.get(i);
                System.out.println((i + 1) + ". " + opp.getInternshipTitle() + ", "
                    + opp.getCompanyName() + ", "
                    + opp.getDepartment());
            }   
            System.out.println("\nWould you like to accept any? (Y/N)");
            String choice2 = sc.next();
            if (choice2.equalsIgnoreCase("Y")){
                while (true) {
                    System.out.println("\nWhich internship do you want to accept? (enter the index number, or -1 to return)");
                    int choice3 = sc.nextInt();
                    if (choice3 == -1) {
                        return;
                    }
                    if (choice3 < 1 || choice3 > successfulOpps.size()){
                        System.out.println(ConsoleColors.RED+"Invalid selection. Please enter a valid index or -1 to return."+ConsoleColors.RESET);
                        continue; 
                    }
                    InternshipOpportunity selected = successfulOpps.get(choice3 - 1);
                    studentController.acceptOpportunity(selected, UserID);
                    System.out.println(ConsoleColors.GREEN+"Accepted Internship Opportunity : " + selected.getInternshipTitle() + " (" + selected.getCompanyName() + ")"+ConsoleColors.RESET);
                    break;
                }
            }
        } else {
            System.out.println(ConsoleColors.RED+"\nYou have no successful offers at the moment."+ConsoleColors.RESET);
        }
    }

    /**
     * Displays pending withdrawal requests and allows the student to submit
     * a new withdrawal request for one of their applied internships.
     *
     * <p>Ensures valid selection and non-empty withdrawal reason.
     *
     * @param studentController controller handling withdrawal requests
     * @param sc               scanner for user input
     * @param context          application context containing student session
     */
    public void viewWithdrawnReqs(StudentController studentController, Scanner sc, AppContext context){
         String UserID = context.getSession().getUserId();
        System.out.println("\nYour Pending Withdrawal Requests:");
        ArrayList<InternshipWithdrawalReq> reqs = studentController.getInternshipWithdrawalReqList(UserID);
        for (int i = 0; i < reqs.size(); i++) {
            InternshipWithdrawalReq req = reqs.get(i);
            System.out.println((i + 1) + ". " + req.getInternshipTitle() + ", "
                + req.getCompanyName() + ", "
                + req.getWithdrawalReason());
        }
        if (reqs.size() >= 3){
            System.out.println(ConsoleColors.RED+"\nYou have made 3 withdrawals requests, your account has been banned from making any further requests.");
            System.out.println("\nPlease contact your school administrator."+ConsoleColors.RESET);
            return;
        }
        System.out.println("\nWould you like to make a new withdrawal request? (Y/N)");
        String choice3 = sc.next();
        if (choice3.equalsIgnoreCase("Y")){
            ArrayList<InternshipOpportunity> opportunityList = studentController.getAppliedInternshipOpportunityList(UserID);
            ArrayList<InternshipOpportunity> filtered_opps = opportunityList.stream()
                .filter(opp -> reqs.stream().noneMatch(req -> 
                    req.getInternshipTitle().equals(opp.getInternshipTitle()) && 
                    req.getCompanyName().equals(opp.getCompanyName())
                ))
                .collect(Collectors.toCollection(ArrayList::new));
            for (int i = 0; i < filtered_opps.size(); i++) {
                InternshipOpportunity req = filtered_opps.get(i);
                System.out.println((i + 1) + ". " + req.getInternshipTitle() + ", "
                    + req.getCompanyName() + ", "
                    + req.getDepartment());
            }
            while (true) {
                System.out.println("\nWhich internship do you want to withdraw? (enter the index number, or -1 to return)");
                int choice4 = sc.nextInt();
                sc.nextLine();
                if (choice4 == -1) {
                    return;
                }
                if (choice4 < 1 || choice4 > filtered_opps.size()){
                    System.out.println(ConsoleColors.RED+"Invalid selection. Please enter a valid index or -1 to return."+ConsoleColors.RESET);
                    continue; 
                }
                InternshipOpportunity selected = filtered_opps.get(choice4 - 1);
                String reason;
                while (true) {
                    System.out.println("\nWhat is your reason for withdrawing");
                    reason = sc.nextLine().trim();                     
                    if (reason.isEmpty()) {
                        System.out.println(ConsoleColors.RED+"A reason for withdrawal is needed."+ConsoleColors.RESET);
                    } else {
                        break; 
                    }
                }
                studentController.withdrawRequest(UserID,selected, reason);
                System.out.println(ConsoleColors.GREEN+"Sent request to withdrawn from: " + selected.getInternshipTitle() + " (" + selected.getCompanyName() + ")"+ConsoleColors.RESET);
                break;
            }
        }
    }
    
     /**
     * Allows a student to change their password.
     *
     * <p>Requires reentry of current password before accepting a new one.
     *
     * @param context   application context containing logged-in user
     * @param sc        scanner for reading input
     * @param controller controller saving the updated file
     */
    public void changePassword(AppContext context, Scanner sc, StudentController controller){
        System.out.println("\nPlease re-enter your password");
        sc.nextLine();
        String password = sc.nextLine();
        User user = context.getSession().getUser();
        while (!(password.equals(user.getPassword()))){
            System.out.println(ConsoleColors.RED+"\nThat is not your password, try again!"+ConsoleColors.RESET);
            password = sc.next();
        }
        System.out.println(ConsoleColors.ITALICS+"\nTake note that the password needs to be at least 5 letters, with 1 Uppercase & 1 Lowercase Letter, as well as 1 number."+ConsoleColors.RESET);
        System.out.println("\nEnter your new password:");
        String newPassword = sc.nextLine();
        ComplexityChecker checker = new ComplexityChecker();
        while (!(checker.checkComplexity(newPassword).equals("Accepted"))){
            System.out.println(ConsoleColors.RED+checker.checkComplexity(newPassword)+ConsoleColors.RESET);
            newPassword = sc.nextLine();
        }
        user.changePassword(newPassword);
        controller.saveFiles();
        System.out.println(ConsoleColors.GREEN+"Password successfully changed!"+ConsoleColors.RESET);
    }

    /**
     * Filters internship opportunities according to criteria chosen by the student.
     *
     * <p>Filter criteria include:
     * <ul>
     *     <li>Internship level</li>
     *     <li>Preferred major</li>
     *     <li>Status</li>
     *     <li>Earliest opening date</li>
     *     <li>Latest closing date</li>
     * </ul>
     *
     * After filtering, displays matching opportunities and allows the student
     * to revise filters recursively.
     *
     * @param sc                scanner for user input
     * @param studentController controller performing filtering logic
     * @param context           app context containing session filter data
     */
    public void filterOpportunities(Scanner sc, StudentController studentController, AppContext context){
        Filter filter = context.getSession().getFilter();
        if(filter==null){
            System.out.println("\nSet Your Filters:");
            InternshipLevel internshipLevel = null;
            boolean internshipLevelSet = false;
            while (!internshipLevelSet) {
                System.out.println("Select Internship Level (-1 to skip): ");
                System.out.println("1. Basic");
                System.out.println("2. Intermediate");
                System.out.println("3. Advanced");
                System.out.print("Enter choice: ");
                int levelChoice = sc.nextInt();
                sc.nextLine();
                switch (levelChoice) {
                    case -1 -> internshipLevelSet = true;
                    case 1 -> { internshipLevel = InternshipLevel.BASIC; internshipLevelSet = true; }
                    case 2 -> { internshipLevel = InternshipLevel.INTERMEDIATE; internshipLevelSet = true; }
                    case 3 -> { internshipLevel = InternshipLevel.ADVANCED; internshipLevelSet = true; }
                    default -> System.out.println("Invalid choice. Please try again.\n");
                }
            }
            Major preferredMajor = null;
            boolean preferredMajorSet = false;
            while (!preferredMajorSet) {
                System.out.println("Select Preferred Major (-1 to skip): ");
                System.out.println("1. CCDS");
                System.out.println("2. IEEE");
                System.out.println("3. DSAI");
                System.out.print("Enter choice: ");
                int majorChoice = sc.nextInt();
                sc.nextLine();
                switch (majorChoice) {
                    case -1 -> preferredMajorSet = true;
                    case 1 -> { preferredMajor = Major.CCDS; preferredMajorSet = true; }
                    case 2 -> { preferredMajor = Major.IEEE; preferredMajorSet = true; }
                    case 3 -> { preferredMajor = Major.DSAI; preferredMajorSet = true; }
                    default -> System.out.println(ConsoleColors.RED+"Invalid choice. Please try again.\n"+ConsoleColors.RESET);
                }
            }
            Status status = null;
            boolean statusSet = false;
            while (!statusSet) {
                System.out.println("Select Internship Opportunity Status (-1 to skip): ");
                System.out.println("1. Pending");
                System.out.println("2. Approved");
                System.out.println("3. Rejected");
                System.out.print("Enter choice: ");
                int majorChoice = sc.nextInt();
                sc.nextLine();
                switch (majorChoice) {
                    case -1 -> statusSet = true;
                    case 1 -> { status = Status.PENDING; statusSet = true; }
                    case 2 -> { status = Status.APPROVED; statusSet = true; }
                    case 3 -> { status = Status.REJECTED; statusSet = true; }
                    default -> System.out.println(ConsoleColors.RED+"Invalid choice. Please try again.\n"+ConsoleColors.RESET);
                }
            }
            String applicationOpeningDate = null;
            String applicationClosingDate = null;
            while (true) {
                System.out.print("How early do you want the Application Opening Date (YYYY-MM-DD, -1 to skip): ");
                String input = sc.nextLine().trim();
                if (input.equals("-1")) {
                    break;
                }
                if (!input.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    System.out.println(ConsoleColors.RED+"Invalid format. Please use YYYY-MM-DD.\n"+ConsoleColors.RESET);
                    continue;
                }

                String[] parts = input.split("-");
                int month = Integer.parseInt(parts[1]);
                int day   = Integer.parseInt(parts[2]);
                if (month < 1 || month > 12) {
                    System.out.println(ConsoleColors.RED+"Invalid month. Must be between 01 and 12.\n"+ConsoleColors.RESET);
                    continue;
                }
                int[] daysInMonth = {
                        31,28,31,30,
                        31,30,31,31,
                        30,31,30,31
                };
                int maxDay = daysInMonth[month - 1];
                if (day < 1 || day > maxDay) {
                    System.out.println(ConsoleColors.RED+"Invalid day for that month. Max is " + maxDay + ".\n"+ConsoleColors.RESET);
                    continue;
                }
                applicationOpeningDate = input;
                break;
            }
            while (true) {
                System.out.print("How late do you want the Application Closing Date (YYYY-MM-DD, -1 to skip): ");
                String input = sc.nextLine().trim();
                if (input.equals("-1")) {
                    break;
                }
                if (!input.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    System.out.println(ConsoleColors.RED+"Invalid format. Please use YYYY-MM-DD.\n"+ConsoleColors.RESET);
                    continue;
                }

                String[] parts = input.split("-");
                int month = Integer.parseInt(parts[1]);
                int day   = Integer.parseInt(parts[2]);
                if (month < 1 || month > 12) {
                    System.out.println(ConsoleColors.RED+"Invalid month. Must be between 01 and 12.\n"+ConsoleColors.RESET);
                    continue;
                }
                int[] daysInMonth = {
                        31,28,31,30,
                        31,30,31,31,
                        30,31,30,31
                };
                int maxDay = daysInMonth[month - 1];
                if (day < 1 || day > maxDay) {
                    System.out.println(ConsoleColors.RED+"Invalid day for that month. Max is " + maxDay + ".\n"+ConsoleColors.RESET);
                    continue;
                }
                if (applicationOpeningDate != null && input.compareTo(applicationOpeningDate) <= 0) {
                    System.out.println(ConsoleColors.RED+"Closing date must be after opening date.\n"+ConsoleColors.RESET);
                    continue;
                }
                applicationClosingDate = input;
                break;
            }
            Filter new_filter = new Filter(status, preferredMajor, internshipLevel, applicationOpeningDate, applicationClosingDate);
            context.getSession().setFilter(new_filter);
        }
        filter = context.getSession().getFilter();
        ArrayList<InternshipOpportunity> list = studentController.filterOpportunities(filter);
        int i = 1;
        for (InternshipOpportunity opp: list){
            String line1 = i + ". " + opp.getInternshipTitle() + ", " + opp.getCompanyName() + ", " + opp.getDepartment();
            String line2 = "   " + opp.getInternshipLevel() + ", " + opp.getDescription();
            String line3 = "   Preferred Major: " + opp.getPreferredMajor()
                + ", Opening: " + opp.getApplicationOpeningDate()
                + ", Closing: " + opp.getApplicationClosingDate()
                + ", Company Rep: " + opp.getCompanyRepInCharge()
                + ", Slots: " + opp.getNoOfSlots()
                + ", Status: " + opp.getStatus()
                + ", Visible: " + opp.getVisible();
            System.out.println("");
            System.out.println(line1);
            System.out.println(line2);
            System.out.println(line3);
            i++;
        }
        
        System.out.println("\nWould you like to edit your filters? (Y/N)");
        String editChoice = sc.next();
        sc.nextLine();
        if (editChoice.equalsIgnoreCase("Y")) {
            context.getSession().setFilter(null);
            filterOpportunities(sc, studentController, context);
        }
    }
}
