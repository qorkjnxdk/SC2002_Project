package boundaries;
import java.util.ArrayList;
import java.util.Scanner;

import controllers.CareerStaffController;
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
import util.ConsoleColors;

public class StudentView{
    private StudentController studentController;
    public StudentView(StudentController studentController){
        this.studentController = studentController;
    };
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
    public void viewProfile(StudentController studentController, Scanner sc, AppContext context){
        System.out.println("\nYour Student Profile:");
        String userId = context.getSession().getUserId();
        Student student = studentController.findStudentByUserID(userId);
        System.out.println("Name: " + student.getName());
        System.out.println("Email: " + student.getEmail());
        System.out.println("Major: " + student.getMajor());
        System.out.println("Year Of Study: " + student.getYearOfStudy());
        switch(student.getYearOfStudy()){
            case 1,2 -> System.out.println("You are eligible for Basic Internships only!");
            default -> System.out.println("You are eligible for Basic, Intermediate & Advanced Internships!");
        }
    }
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
            System.out.println("\nYou have already accepted "+opp.getInternshipTitle() + " from "+opp.getCompanyName());
            return;
        }
        if (appliedList.size()-withdrawnOpps.size()-rejectedOpps.size()>=3){
            System.out.println("\nYou already have 3 internship applications. You can't apply for anymore!");
            return;
        }
        if (availableList.isEmpty()){
            System.out.println("\nYou have no new relevant internship opportunities you can apply for (all have been applied to or none match your profile).");
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
                    System.out.println("Invalid selection. Please enter a valid index or -1 to return.");
                    continue; 
                }
                InternshipOpportunity selected = availableList.get(choice4 - 1);
                studentController.applyInternship(student,selected);
                System.out.println("Applied to: " + selected.getInternshipTitle() + " (" + selected.getCompanyName() + ")");
                break;
            }
        }
    }
    public void viewAppliedInternshipOpportunities(StudentController studentController, Scanner sc, AppContext context){
        String UserID = context.getSession().getUserId();
        ArrayList<InternshipOpportunity> opportunityList = studentController.getAppliedInternshipOpportunityList(UserID);
        ArrayList<InternshipOpportunity> acceptedOpps = studentController.getOppByStatus(UserID,ApplicationStatus.ACCEPTED);
        if (acceptedOpps.size()>0){
            InternshipOpportunity opp = acceptedOpps.get(0);
            System.out.println("You have already accepted "+opp.getInternshipTitle() + " from "+opp.getCompanyName());
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
                        System.out.println("Invalid selection. Please enter a valid index or -1 to return.");
                        continue; 
                    }
                    InternshipOpportunity selected = successfulOpps.get(choice3 - 1);
                    studentController.acceptOpportunity(selected, UserID);
                    System.out.println("Accepted Internship Opportunity : " + selected.getInternshipTitle() + " (" + selected.getCompanyName() + ")");
                    break;
                }
            }
        } else {
            System.out.println("\nYou have no successful offers at the moment.");
        }
    }
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

        System.out.println("\nWould you like to make a new withdrawal request? (Y/N)");
        String choice3 = sc.next();
        if (choice3.equalsIgnoreCase("Y")){
            ArrayList<InternshipOpportunity> opportunityList = studentController.getAppliedInternshipOpportunityList(UserID);
            for (int i = 0; i < opportunityList.size(); i++) {
                InternshipOpportunity req = opportunityList.get(i);
                System.out.println((i + 1) + ". " + req.getInternshipTitle() + ", "
                    + req.getCompanyName() + ", "
                    + req.getDepartment());
            }
            while (true) {
                System.out.println("\nWhich internship do you want to withdraw? (enter the index number, or -1 to return)");
                int choice4 = sc.nextInt();
                if (choice4 == -1) {
                    return;
                }
                if (choice4 < 1 || choice4 > opportunityList.size()){
                    System.out.println("Invalid selection. Please enter a valid index or -1 to return.");
                    continue; 
                }
                InternshipOpportunity selected = opportunityList.get(choice4 - 1);
                System.out.println("\nWhat is your reason for withdrawing");
                String reason = sc.nextLine();
                studentController.withdrawRequest(UserID,selected, reason);
                System.out.println("Sent request to withdrawn from: " + selected.getInternshipTitle() + " (" + selected.getCompanyName() + ")");
                break;
            }
        }
    }
    public void changePassword(AppContext context, Scanner sc, StudentController controller){
        System.out.println("\nPlease re-enter your password");
        sc.nextLine();
        String password = sc.nextLine();
        User user = context.getSession().getUser();
        while (!(password.equals(user.getPassword()))){
            System.out.println("\nThat is not your password, try again!");
            password = sc.next();
        }
        System.out.println("Enter your new password:");
        String newPassword = sc.nextLine();
        user.changePassword(newPassword);
        controller.saveFiles();
        System.out.println("Password successfully changed!");
    }
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
                    default -> System.out.println("Invalid choice. Please try again.\n");
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
                    default -> System.out.println("Invalid choice. Please try again.\n");
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
                    System.out.println("Invalid format. Please use YYYY-MM-DD.\n");
                    continue;
                }

                String[] parts = input.split("-");
                int month = Integer.parseInt(parts[1]);
                int day   = Integer.parseInt(parts[2]);
                if (month < 1 || month > 12) {
                    System.out.println("Invalid month. Must be between 01 and 12.\n");
                    continue;
                }
                int[] daysInMonth = {
                        31,28,31,30,
                        31,30,31,31,
                        30,31,30,31
                };
                int maxDay = daysInMonth[month - 1];
                if (day < 1 || day > maxDay) {
                    System.out.println("Invalid day for that month. Max is " + maxDay + ".\n");
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
                    System.out.println("Invalid format. Please use YYYY-MM-DD.\n");
                    continue;
                }

                String[] parts = input.split("-");
                int month = Integer.parseInt(parts[1]);
                int day   = Integer.parseInt(parts[2]);
                if (month < 1 || month > 12) {
                    System.out.println("Invalid month. Must be between 01 and 12.\n");
                    continue;
                }
                int[] daysInMonth = {
                        31,28,31,30,
                        31,30,31,31,
                        30,31,30,31
                };
                int maxDay = daysInMonth[month - 1];
                if (day < 1 || day > maxDay) {
                    System.out.println("Invalid day for that month. Max is " + maxDay + ".\n");
                    continue;
                }
                if (applicationOpeningDate != null && input.compareTo(applicationOpeningDate) <= 0) {
                    System.out.println("Closing date must be after opening date.\n");
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
