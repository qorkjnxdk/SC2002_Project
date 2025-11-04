package boundaries;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;

import util.AppContext;
import controllers.CompanyRepController;
import entities.*;
import entities.Application.ApplicationStatus;
import entities.InternshipOpportunity.InternshipLevel;
import entities.InternshipOpportunity.Major;;

public class CompanyRepView {
    private CompanyRepController companyRepController;
    public CompanyRepView(CompanyRepController companyRepController){
        this.companyRepController =  companyRepController;
    };
    public void run(AppContext Context, Scanner sc){
        while (true) {
            System.out.println("\n===============================");
            System.out.println("Welcome Back " + Context.getSession().getUserId());
            System.out.println("Please indicate your action:");
            System.out.println("1. Add Internship Opportunity");
            System.out.println("2. View Your Internship Opportunities");
            System.out.println("3. View All Internship Opportunities");
            System.out.println("4. Logout");
            System.out.println("=================================");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    createInternshipOpportunity(sc, companyRepController, Context);
                    break;
                case 2:
                    viewRelevantInternshipOpportunities(companyRepController, Context, sc);
                    break;
                case 3:
                    viewAllInternshipOpportunities(companyRepController);
                    break;
                case 4:
                    Context.clearSession();
                    companyRepController.saveFiles();
                    System.out.println("You have been logged out.");
                    return;
                default:
                    System.out.println("Invalid choice. Please enter 1-4.");
                    break;
            }
        }
    }
    public void createInternshipOpportunity(Scanner sc, CompanyRepController companyRepController, AppContext context){                    
        System.out.println("Create Internship Opportunity Here!");
        System.out.println("\nInput Internship Title: ");
        sc.nextLine();
        String internshipTitle = sc.nextLine();
        System.out.println("Input Internship Description: ");
        String internshipDescription = sc.nextLine();

        InternshipLevel internshipLevel = null;
        while (internshipLevel == null) {
            System.out.println("Select Internship Level: ");
            System.out.println("1. Basic");
            System.out.println("2. Intermediate");
            System.out.println("3. Advanced");
            System.out.print("Enter choice: ");
            int levelChoice = sc.nextInt();
            sc.nextLine();
            switch (levelChoice) {
                case 1 -> internshipLevel = InternshipLevel.BASIC;
                case 2 -> internshipLevel = InternshipLevel.INTERMEDIATE;
                case 3 -> internshipLevel = InternshipLevel.ADVANCED;
                default -> System.out.println("Invalid choice. Please try again.\n");
            }
        }

        Major preferredMajor = null;
        while (preferredMajor == null) {
            System.out.println("Select Preferred Major: ");
            System.out.println("1. CCDS");
            System.out.println("2. IEEE");
            System.out.println("3. DSAI");
            System.out.print("Enter choice: ");
            int majorChoice = sc.nextInt();
            sc.nextLine();
            switch (majorChoice) {
                case 1 -> preferredMajor = Major.CCDS;
                case 2 -> preferredMajor = Major.IEEE;
                case 3 -> preferredMajor = Major.DSAI;
                default -> System.out.println("Invalid choice. Please try again.\n");
            }
        }

        System.out.println("Input Application Opening Date (YYYY-MM-DD): ");
        String applicationOpeningDate = sc.nextLine();
        System.out.println("Input Application Closing Date (YYYY-MM-DD): ");
        String applicationClosingDate = sc.nextLine();
        System.out.println("Input Department: ");
        String department = sc.nextLine();
        
        CompanyRep companyRep = (CompanyRep)context.getSession().getUser();
        String companyName = companyRep.getCompanyName();
        String companyRepID = companyRep.getUserId();

        System.out.println("Input number of slots available: ");
        int noOfSlots = sc.nextInt();
        sc.nextLine(); 

        InternshipOpportunity internship = new InternshipOpportunity(internshipTitle, internshipDescription, internshipLevel, preferredMajor, applicationOpeningDate, 
        applicationClosingDate, companyName, department, companyRepID, noOfSlots, new ArrayList<>());
        companyRepController.addInternshipOpportunity(internship);
        internship.addApplication(new Application("student1", "appliedDateTime", ApplicationStatus.PENDING));
        internship.addApplication(new Application("student2", "appliedDateTime", ApplicationStatus.PENDING));
        System.out.println("Internship Opportunity Created Successfully!");
    }
    public void viewRelevantInternshipOpportunities(CompanyRepController companyRepController, AppContext context, Scanner sc){
        System.out.println("\nYour Internship Opportunities:");
        CompanyRep companyRep = (CompanyRep)context.getSession().getUser();
        ArrayList<InternshipOpportunity> opportunityList = companyRepController.getRelevantInternshipOpportunities(companyRep.getUserId());
        for (int i = 0; i < opportunityList.size(); i++) {
            InternshipOpportunity req = opportunityList.get(i);
            System.out.println((i + 1) + ". " + req.getInternshipTitle() + ", "
                + req.getCompanyName() + ", "
                + req.getDepartment());
        }
        while (true) {
            System.out.println("\nWhich opportunity would you like to see applications for? Please enter a valid index or -1 to return.");
            int choice3 = sc.nextInt();
            if (choice3 == -1) {
                return;
            }
            if (choice3 < 1 || choice3 > opportunityList.size()){
                System.out.println("Invalid selection. Please enter a valid index or -1 to return.");
                continue; 
            }
            InternshipOpportunity selected = opportunityList.get(choice3 - 1);
            System.out.printf("\nTitle: %s, Department: %s, Description: %s \n", selected.getInternshipTitle(), selected.getDepartment(), selected.getDescription());
            ArrayList<Application> applicationList = selected.getApplicationList();
            ArrayList<Application> pendingApplicants = selected.getApplicationList().stream()
                .filter(s -> s.getApplicationStatus().equals(ApplicationStatus.PENDING))
                .collect(Collectors.toCollection(ArrayList::new));
            ArrayList<Application> successfulApplicants = selected.getApplicationList().stream()
                .filter(s -> s.getApplicationStatus().equals(ApplicationStatus.SUCCESSFUL))
                .collect(Collectors.toCollection(ArrayList::new));
            ArrayList<Application> acceptedApplicants = selected.getApplicationList().stream()
                .filter(s -> s.getApplicationStatus().equals(ApplicationStatus.ACCEPTED))
                .collect(Collectors.toCollection(ArrayList::new));
            ArrayList<Application> withdrawnApplicants = selected.getApplicationList().stream()
                .filter(s -> s.getApplicationStatus().equals(ApplicationStatus.WITHDRAWN))
                .collect(Collectors.toCollection(ArrayList::new));
            System.out.printf("Total Number of Students Applied: %d \n", applicationList.size());
            System.out.printf("Number of Slots Left: %d \n\n", selected.getNoOfSlots()-acceptedApplicants.size());
            if(successfulApplicants.size()!=0){
                System.out.printf("\nThese are the %d applicants you have chosen, but yet to accept the offer: \n", successfulApplicants.size());
                for (int j = 0; j < successfulApplicants.size(); j++) {
                    Application application = successfulApplicants.get(j);
                    System.out.println((j + 1) + ". Student ID: " + application.getStudentID());
                } 
            }  
            if(acceptedApplicants.size()!=0){
                System.out.printf("\nThese are the %d applicants that have accepted the offer: \n", acceptedApplicants.size());
                for (int j = 0; j < successfulApplicants.size(); j++) {
                    Application application = successfulApplicants.get(j);
                    System.out.println((j + 1) + ". Student ID: " + application.getStudentID());
                }   
            }
            if(withdrawnApplicants.size()!=0){
                System.out.printf("\nThese are the %d withdrawn applicants: \n",withdrawnApplicants.size());
                for (int j = 0; j < withdrawnApplicants.size(); j++) {
                    Application application = withdrawnApplicants.get(j);
                    System.out.println((j + 1) + ". Student ID: " + application.getStudentID());
                }   
            }
            if(pendingApplicants.size()!=0){
                System.out.printf("\nThese are the %d pending applicants: \n",pendingApplicants.size());
                for (int j = 0; j < pendingApplicants.size(); j++) {
                    Application application = pendingApplicants.get(j);
                    Student student = companyRepController.findStudentByUserID(application.getStudentID());
                    String major = student.getMajor() == null ? "" : student.getMajor().name();
                    System.out.printf("%d. Name: %s, Major: %s (Year %d) ", (j+1), student.getName(), major, student.getYearOfStudy());
                    System.out.println("");
                    }   
                System.out.println("\nWould you like to approve applications? (Y/N)");
                String choice2 = sc.next();
                if (choice2.equalsIgnoreCase("Y")){

                    while (true) {
                        System.out.println("\nWhich application do you want to approve? (enter the index number, or -1 to return)");
                        int choice4 = sc.nextInt();
                        if (choice4 == -1) {
                            return;
                        }
                        if (choice4 < 1 || choice4 > pendingApplicants.size()){
                            System.out.println("Invalid selection. Please enter a valid index or -1 to return.");
                            continue; 
                        }
                        Application approved = pendingApplicants.get(choice4 - 1);
                        Student student = companyRepController.findStudentByUserID(approved.getStudentID());
                        approved.setApplicationStatus(ApplicationStatus.SUCCESSFUL);
                        System.out.println("Approved "+student.getName()+"\'s application for " + selected.getInternshipTitle());
                        break;
                }
                break;
            }
            }
        return;
        }
    }
    public void viewAllInternshipOpportunities(CompanyRepController companyRepController){
        System.out.println("\nAll Internship Opportunities:");
        ArrayList<InternshipOpportunity> opportunityList = companyRepController.getInternshipOpportunityList();
        for (int i = 0; i < opportunityList.size(); i++) {
            InternshipOpportunity req = opportunityList.get(i);
            System.out.println((i + 1) + ". " + req.getInternshipTitle() + ", "
                + req.getCompanyName() + ", "
                + req.getDepartment());
        }
    }
}
