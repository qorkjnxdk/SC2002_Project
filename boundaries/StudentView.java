package boundaries;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.Optional;

import controllers.CompanyRepController;
import controllers.StudentController;
import entities.Application;
import entities.Application.ApplicationStatus;
import entities.CompanyRep;
import entities.CompanyRepCreationReq;
import entities.InternshipOpportunity;
import entities.InternshipWithdrawalReq;
import entities.Student;
import util.AppContext;

public class StudentView{
    private StudentController studentController;
    public StudentView(StudentController studentController){
        this.studentController = studentController;
    };
    public void run(AppContext Context, Scanner sc){
        while (true) {
            System.out.println("\n===============================");
            System.out.println("Welcome Back " + Context.getSession().getUserId());
            System.out.println("Please indicate your action:");
            System.out.println("1. View All Internships You Are Eligible For ");
            System.out.println("2. View All Internships");
            System.out.println("3. View All Internships You Have Applied For");
            System.out.println("4. View All Pending Withdrawal Requests / Make a Withdrawal Request");
            System.out.println("5. Logout");
            System.out.println("=================================");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    viewRelevantInternshipOpportunities(studentController,sc,Context);
                    break;
                case 2:
                    viewInternshipOpportunities(studentController,sc);
                    break;
                case 3:
                    viewAppliedInternshipOpportunities(studentController,sc,Context);
                    break;
                case 4:
                    viewWithdrawnReqs(studentController,sc,Context);
                    break;
                case 5:
                    Context.clearSession();
                    studentController.saveFiles();
                    System.out.println("You have been logged out.");
                    return;
                default:
                    System.out.println("Invalid choice. Please enter 1-5.");
                    break;
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
    public void viewRelevantInternshipOpportunities(StudentController studentController, Scanner sc, AppContext context){
        String userId = context.getSession().getUserId();
        Student student = studentController.findStudentByUserID(userId);
        System.out.println("\nInternship Opportunities You Can Apply For:");
        ArrayList<InternshipOpportunity> opportunityList = studentController.getRelevantInternshipOpportunityList(student);
        ArrayList<InternshipOpportunity> appliedList = studentController.getAppliedInternshipOpportunityList(userId);
        ArrayList<InternshipOpportunity> availableList = opportunityList.stream()
            .filter(opp -> appliedList.stream().noneMatch(a ->
                a.getInternshipTitle().equals(opp.getInternshipTitle()) &&
                a.getCompanyName().equals(opp.getCompanyName()) &&
                a.getDepartment().equals(opp.getDepartment())
            ))
            .collect(Collectors.toCollection(ArrayList::new));

        if (availableList.isEmpty()){
            System.out.println("You have no new relevant internship opportunities you can apply for (all have been applied to or none match your profile).");
            return;
        }

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
        System.out.println("\nInternship Opportunities You Have Applied For:");
        ArrayList<InternshipOpportunity> opportunityList = studentController.getAppliedInternshipOpportunityList(UserID);
        ArrayList<InternshipOpportunity> toBeAcceptedList = new ArrayList<>();
        for (int i = 0; i < opportunityList.size(); i++) {
            InternshipOpportunity opp = opportunityList.get(i);
            Optional<Application> application = opp.getApplicationList().stream()
            .filter(app->app.getStudentID().equals(UserID)).findFirst();
            System.out.println((i + 1) + ". " + opp.getInternshipTitle() + ", "
                + opp.getCompanyName() + ", "
                + opp.getDepartment());
            if (application.isPresent()){
                Application application2 = application.get();
                if(application2.getApplicationStatus().equals(ApplicationStatus.SUCCESSFUL)){
                    toBeAcceptedList.add(opp);
                }
                System.out.println("Applied: " + application2.getAppliedDateTime());
                System.out.println("Status: " + application2.getApplicationStatus().name());
            }
        }
        if (!toBeAcceptedList.isEmpty()) {
            System.out.println("\nYou can accept these offers:");
            for (int i = 0; i < toBeAcceptedList.size(); i++) {
                InternshipOpportunity opp = toBeAcceptedList.get(i);
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
                    if (choice3 < 1 || choice3 > toBeAcceptedList.size()){
                        System.out.println("Invalid selection. Please enter a valid index or -1 to return.");
                        continue; 
                    }
                    InternshipOpportunity selected = toBeAcceptedList.get(choice3 - 1);
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
}
