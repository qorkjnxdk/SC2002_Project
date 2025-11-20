package boundaries;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;

import util.AppContext;
import util.ComplexityChecker;
import util.ConsoleColors;
import controllers.CompanyRepController;
import controllers.StudentController;
import entities.*;
import entities.Application.ApplicationStatus;
import entities.InternshipOpportunity.InternshipLevel;
import entities.InternshipOpportunity.Major;
import entities.InternshipOpportunity.Status;;

/**
 * Boundary (UI) class for Company Representatives.
 *
 * <p>This class handles all user interactions for company representatives:
 * <ul>
 *   <li>Create internship opportunities</li>
 *   <li>Edit pending opportunities</li>
 *   <li>View and toggle visibility of existing opportunities</li>
 *   <li>Filter all opportunities</li>
 *   <li>View applications for their internships</li>
 *   <li>Approve applicants</li>
 *   <li>Change password</li>
 *   <li>Logout</li>
 * </ul>
 *
 * It acts as the presentation layer between user input and the
 * {@link CompanyRepController} business logic.
 */

public class CompanyRepView {
     /**
     * Controller that handles business logic for company representatives.
     */
    private CompanyRepController companyRepController;
     /**
     * Constructs a new CompanyRepView.
     *
     * @param companyRepController controller for performing company representative operations
     */
    public CompanyRepView(CompanyRepController companyRepController){
        this.companyRepController =  companyRepController;
    };

    /**
     * Main menu loop for company representatives.
     *
     * <p>Displays options and calls the respective methods until the user logs out.
     *
     * @param context application context containing the active session
     * @param sc      scanner for reading user input
     */

    public void run(AppContext Context, Scanner sc){
        while (true) {
            System.out.println("\n===============================");
            System.out.println("Welcome Back " + Context.getSession().getUser().getName());
            System.out.println("Please indicate your action:");
            System.out.println("1. Add Internship Opportunity");
            System.out.println("2. View Your Internship Opportunities");
            System.out.println("3. Edit Your Pending Internship Opportunities");
            System.out.println("4. Toggle Your Internship Opportunities");
            System.out.println("5. Filter All Opportunities");
            System.out.println("6. Change Password");
            System.out.println("7. Logout");
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
                    editPendingInternshipOpportunities(companyRepController, Context, sc);
                    break;
                case 4:
                    toggleOpps(companyRepController, Context, sc);
                    break;
                case 5:
                    filterOpportunities(sc, companyRepController, Context);
                    break;
                case 6:
                    changePassword(Context,sc,companyRepController);
                    break;
                case 7:
                    Context.clearSession();
                    companyRepController.saveFiles();
                    System.out.println(ConsoleColors.RED+"You have been logged out."+ConsoleColors.RESET);
                    return;
                default:
                    System.out.println(ConsoleColors.RED+"Invalid choice. Please enter 1-7."+ConsoleColors.RESET);
                    break;
            }
        }
    }

    /**
     * Creates a new internship opportunity.
     *
     * <p>Prompts the company representative for:
     * <ul>
     *   <li>Title</li>
     *   <li>Description</li>
     *   <li>Internship level</li>
     *   <li>Preferred major</li>
     *   <li>Opening and closing dates</li>
     *   <li>Department</li>
     *   <li>Number of slots</li>
     * </ul>
     *
     * Performs full validation on dates and slot counts.
     *
     * @param sc                     scanner for user input
     * @param companyRepController   controller for opportunity creation
     * @param context                current app context
     */
    public void createInternshipOpportunity(Scanner sc, CompanyRepController companyRepController, AppContext context){             
        CompanyRep companyRep = (CompanyRep)context.getSession().getUser();
        ArrayList<InternshipOpportunity> opportunityList = companyRepController.getRelevantInternshipOpportunities(companyRep.getUserId());
        if(opportunityList.size()>=5){
            System.out.println(ConsoleColors.RED+"Only a maximum of 5 opportunities can be created!"+ConsoleColors.RESET);
            return;
        }
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
                default -> System.out.println(ConsoleColors.RED+"Invalid choice. Please try again.\n"+ConsoleColors.RESET);
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
                default -> System.out.println(ConsoleColors.RED+"Invalid choice. Please try again.\n"+ConsoleColors.RESET);
            }
        }
        String applicationOpeningDate;
        String applicationClosingDate;

        while (true) {
            System.out.print("Input Application Opening Date (YYYY-MM-DD): ");
            applicationOpeningDate = sc.nextLine().trim();
            if (!applicationOpeningDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                System.out.println(ConsoleColors.RED+"Invalid format. Please use YYYY-MM-DD.\n"+ConsoleColors.RESET);
                continue;
            }

            String[] parts = applicationOpeningDate.split("-");
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
            break;
        }
        while (true) {
            System.out.print("Input Application Closing Date (YYYY-MM-DD): ");
            applicationClosingDate = sc.nextLine().trim();

            // Basic format check
            if (!applicationClosingDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                System.out.println(ConsoleColors.RED+"Invalid format. Please use YYYY-MM-DD.\n"+ConsoleColors.RESET);
                continue;
            }

            String[] parts = applicationClosingDate.split("-");
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
            if (applicationClosingDate.compareTo(applicationOpeningDate) <= 0) {
                System.out.println(ConsoleColors.RED+"Closing date must be after opening date.\n"+ConsoleColors.RESET);
                continue;
            }
            break;
        }

        System.out.println("Input Department: ");
        String department = sc.nextLine();
        String companyName = companyRep.getCompanyName();
        String companyRepID = companyRep.getUserId();

        System.out.println("Input number of slots available: ");
        int noOfSlots = sc.nextInt();
        while(noOfSlots>10){
            System.out.println(ConsoleColors.RED+"Maximum number of slots is 10! Enter a lower number:"+ConsoleColors.RESET);
            noOfSlots = sc.nextInt();
        }
        sc.nextLine(); 

        InternshipOpportunity internship = new InternshipOpportunity(internshipTitle, internshipDescription, internshipLevel, preferredMajor, applicationOpeningDate, 
        applicationClosingDate, companyName, department, companyRepID, noOfSlots, Status.PENDING,true, new ArrayList<>());
        companyRepController.addInternshipOpportunity(internship);
        System.out.println(ConsoleColors.GREEN+"Internship Opportunity Created Successfully!"+ConsoleColors.RESET);
    }

    /**
     * Toggles visibility of a company's internship opportunities.
     *
     * <p>Allows company representative to turn visibility ON/OFF for any opportunity.
     *
     * @param companyRepController controller performing updates
     * @param context              application context
     * @param sc                   scanner for reading input
     */
    public void toggleOpps(CompanyRepController companyRepController, AppContext context, Scanner sc){
        System.out.println("\nYour Internship Opportunities:");
        CompanyRep companyRep = (CompanyRep)context.getSession().getUser();
        ArrayList<InternshipOpportunity> opportunityList = companyRepController.getRelevantInternshipOpportunities(companyRep.getUserId());
        for (int i = 0; i < opportunityList.size(); i++) {
            InternshipOpportunity opp = opportunityList.get(i);
            System.out.println((i + 1) + ". " + opp.getInternshipTitle() + ", "
                + opp.getCompanyName() + ", "
                + opp.getDepartment() + "  Visibility: "
                + (opp.getVisible()? "On":"Off")
            );
        }
        while (true) {
            System.out.println("\nWhich opportunity would you like to toggle visibility for? Please enter a valid index or -1 to return.");
            int choice3 = sc.nextInt();
            if (choice3 == -1) {
                return;
            }
            if (choice3 < 1 || choice3 > opportunityList.size()){
                System.out.println(ConsoleColors.RED+"Invalid selection. Please enter a valid index or -1 to return."+ConsoleColors.RESET);
            }
            InternshipOpportunity opp = opportunityList.get(choice3-1);
            opp.toggleVisibility();
            System.out.println(ConsoleColors.GREEN+"Visibility for "+ opp.getInternshipTitle() + " is now "+(opp.getVisible()? "on":"off")+ConsoleColors.RESET);
            break;
        }
    }

    /**
     * Allows company representative to edit details of internship opportunities
     * that are still pending approval.
     *
     * <p>Editable fields include:
     * <ul>
     *   <li>Title</li>
     *   <li>Description</li>
     *   <li>Opening date</li>
     *   <li>Closing date</li>
     *   <li>Preferred major</li>
     *   <li>Slots</li>
     *   <li>Department</li>
     *   <li>Internship level</li>
     * </ul>
     *
     * @param companyRepController controller performing updates
     * @param context              application context
     * @param sc                   scanner for reading input
     */
    public void editPendingInternshipOpportunities(CompanyRepController companyRepController, AppContext context, Scanner sc){
        CompanyRep companyRep = (CompanyRep)context.getSession().getUser();
        ArrayList<InternshipOpportunity> pendingOppList = companyRepController.getInternshipByStatus(companyRep.getUserId(),Status.PENDING);
        if(pendingOppList.size()==0){
            System.out.println("You have no pending internship opportunities!");
            return;
        }
        else{
            System.out.println("\nInternship Opportunities Waiting for Career Staff Approval:");
            for (int i = 0; i < pendingOppList.size(); i++) {
                InternshipOpportunity req = pendingOppList.get(i);
                System.out.println((i + 1) + ". " + req.getInternshipTitle() + ", "
                    + req.getCompanyName() + ", "
                    + req.getDepartment());
                System.out.println("Application Description: "+ req.getDescription());
                System.out.println("Application Opening Date: "+ req.getApplicationOpeningDate());
                System.out.println("Application Closing Date: "+ req.getApplicationClosingDate());
                System.out.println("Number Of Slots: "+ req.getNoOfSlots());
                System.out.println("Preferred Major: "+ req.getPreferredMajor().toString());
                System.out.println("");
                }
            }
            while (true) {
                System.out.println("\nWhich application do you want to edit? (enter the index number, or -1 to return)");
                int choice4 = sc.nextInt();
                if (choice4 == -1) {
                    return;
                }
                if (choice4 < 1 || choice4 > pendingOppList.size()){
                    System.out.println(ConsoleColors.RED+"Invalid selection. Please enter a valid index or -1 to return."+ConsoleColors.RESET);
                    continue; 
                }
                InternshipOpportunity opp = pendingOppList.get(choice4 - 1);
                System.out.println(ConsoleColors.GREEN+"\nEdited Internship Opportunity Successfully"+ConsoleColors.RESET);
                while (true) {
                    System.out.println("\nEditing: " + opp.getInternshipTitle());
                    System.out.println("Choose field to edit: ");
                    System.out.println("1. Internship Title: " + opp.getInternshipTitle());
                    System.out.println("2. Description: " + opp.getDescription());
                    System.out.println("3. Opening Date (YYYY-MM-DD): " + opp.getApplicationOpeningDate());
                    System.out.println("4. Closing Date (YYYY-MM-DD): " + opp.getApplicationClosingDate());
                    System.out.println("5. Preferred Major: " + opp.getPreferredMajor().toString());
                    System.out.printf("6. Number of Slots: %d",opp.getNoOfSlots());
                    System.out.println("7. Department: " + opp.getDepartment());
                    System.out.println("8. Internship Level: " + opp.getInternshipLevel().toString());
                    System.out.println("9. Finish Editing");

                    System.out.print("Enter choice: ");
                    int editChoice = sc.nextInt();
                    sc.nextLine();

                    switch (editChoice) {
                        case 1:
                            System.out.print("Enter new Internship Title: ");
                            opp.setInternshipTitle(sc.nextLine().trim());
                            System.out.println(ConsoleColors.GREEN+"Updated Title!"+ConsoleColors.RESET);
                            break;

                        case 2:
                            System.out.print("Enter new Description: ");
                            opp.setDescription(sc.nextLine().trim());
                            System.out.println(ConsoleColors.GREEN+"Updated Description!"+ConsoleColors.RESET);
                            break;

                        case 3:
                            System.out.print("Enter new Opening Date (YYYY-MM-DD): ");
                            opp.setApplicationOpeningDate(sc.nextLine().trim());
                            System.out.println(ConsoleColors.GREEN+"Updated Opening Date!"+ConsoleColors.RESET);
                            break;

                        case 4:
                            System.out.print("Enter new Closing Date (YYYY-MM-DD): ");
                            opp.setApplicationClosingDate(sc.nextLine().trim());
                            System.out.println(ConsoleColors.GREEN+"Updated Closing Date!"+ConsoleColors.RESET);
                            break;

                        case 5:
                            System.out.println("Select new Preferred Major:");
                            System.out.println("1. CCDS");
                            System.out.println("2. IEEE");
                            System.out.println("3. DSAI");
                            System.out.print("Enter choice: ");
                            int majorChoice = sc.nextInt();
                            sc.nextLine();
                            Major newMajor = null;
                            switch (majorChoice) {
                                case 1 -> newMajor = Major.CCDS;
                                case 2 -> newMajor = Major.IEEE;
                                case 3 -> newMajor = Major.DSAI;
                                default -> System.out.println(ConsoleColors.RED+"Invalid choice."+ConsoleColors.RESET);
                            }
                            if (newMajor != null) {
                                opp.setPreferredMajor(newMajor);
                                System.out.println(ConsoleColors.GREEN+"Updated Preferred Major!"+ConsoleColors.RESET);
                            }
                            break;

                        case 6:
                            System.out.print("Enter new number of slots: ");
                            int slots = sc.nextInt();
                            sc.nextLine();
                            opp.setNoOfSlots(slots);
                            System.out.println(ConsoleColors.GREEN+"Updated Number of Slots!"+ConsoleColors.RESET);
                            break;

                        case 7:
                            System.out.print("Enter new Department: ");
                            opp.setDepartment(sc.nextLine().trim());
                            System.out.println(ConsoleColors.GREEN+"Updated Department!"+ConsoleColors.RESET);
                            break;

                        case 8:
                            System.out.println("Select new Internship Level:");
                            System.out.println("1. Basic");
                            System.out.println("2. Intermediate");
                            System.out.println("3. Advanced");
                            System.out.print("Enter choice: ");
                            int levelChoice = sc.nextInt();
                            sc.nextLine();
                            InternshipLevel newLevel = null;
                            switch (levelChoice) {
                                case 1 -> newLevel = InternshipLevel.BASIC;
                                case 2 -> newLevel = InternshipLevel.INTERMEDIATE;
                                case 3 -> newLevel = InternshipLevel.ADVANCED;
                                default -> System.out.println(ConsoleColors.RED+"Invalid choice."+ConsoleColors.RESET);
                            }
                            if (newLevel != null) {
                                opp.setInternshipLevel(newLevel);
                                System.out.println(ConsoleColors.GREEN+"Updated Internship Level!"+ConsoleColors.RESET);
                            }
                            break;

                        case 9:
                            System.out.println(ConsoleColors.GREEN+"\nFinished editing this opportunity."+ConsoleColors.RESET);
                            return;

                        default:
                            System.out.println(ConsoleColors.RED+"Invalid choice, please try again."+ConsoleColors.RESET);
                    }
                }
            }
        }

    /**
     * Displays a company representative’s pending, filled,
     * and approved internship opportunities.
     *
     * <p>Additionally allows them to:
     * <ul>
     *   <li>View applicants</li>
     *   <li>Approve applicants</li>
     * </ul>
     *
     * @param companyRepController controller accessing opportunities and applicants
     * @param context              current application context
     * @param sc                   scanner for reading input
     */
    public void viewRelevantInternshipOpportunities(CompanyRepController companyRepController, AppContext context, Scanner sc){
        CompanyRep companyRep = (CompanyRep)context.getSession().getUser();
        ArrayList<InternshipOpportunity> pendingOppList = companyRepController.getInternshipByStatus(companyRep.getUserId(),Status.PENDING);
        ArrayList<InternshipOpportunity> filledOppList = companyRepController.getInternshipByStatus(companyRep.getUserId(),Status.FILLED);
        ArrayList<InternshipOpportunity> currentOppList = companyRepController.getInternshipByStatus(companyRep.getUserId(),Status.APPROVED);
        if(pendingOppList.size()+filledOppList.size()+currentOppList.size()==0){
            System.out.println(ConsoleColors.RED+"You have not created any internship opportunities!"+ConsoleColors.RESET);
            return;
        }
        if(pendingOppList.size()>0){
            System.out.println("\nInternship Opportunities Waiting for Career Staff Approval:");
            for (int i = 0; i < pendingOppList.size(); i++) {
                InternshipOpportunity req = pendingOppList.get(i);
                System.out.println((i + 1) + ". " + req.getInternshipTitle() + ", "
                    + req.getCompanyName() + ", "
                    + req.getDepartment());
            }
        }
        if(filledOppList.size()>0){
            System.out.println("\nInternship Opportunities That Are Filled:");
            for (int i = 0; i < filledOppList.size(); i++) {
                InternshipOpportunity req = filledOppList.get(i);
                System.out.println((i + 1) + ". " + req.getInternshipTitle() + ", "
                    + req.getCompanyName() + ", "
                    + req.getDepartment());
            }
        }
        if(currentOppList.size()>0){
            System.out.println("\nYour Current Internship Opportunities:");
            for (int i = 0; i < currentOppList.size(); i++) {
                InternshipOpportunity req = currentOppList.get(i);
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
                if (choice3 < 1 || choice3 > currentOppList.size()){
                    System.out.println(ConsoleColors.RED+"Invalid selection. Please enter a valid index or -1 to return."+ConsoleColors.RESET);
                    continue; 
                }
                InternshipOpportunity selected = currentOppList.get(choice3 - 1);
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
                    for (int j = 0; j < acceptedApplicants.size(); j++) {
                        Application application = acceptedApplicants.get(j);
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
                        String major = student.getMajor().name();
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
                                System.out.println(ConsoleColors.RED+"Invalid selection. Please enter a valid index or -1 to return."+ConsoleColors.RESET);
                                continue; 
                            }
                            Application approved = pendingApplicants.get(choice4 - 1);
                            Student student = companyRepController.findStudentByUserID(approved.getStudentID());
                            approved.setApplicationStatus(ApplicationStatus.SUCCESSFUL);
                            System.out.println(ConsoleColors.GREEN+"Approved "+student.getName()+"\'s application for " + selected.getInternshipTitle()+ConsoleColors.RESET);
                            break;
                        }
                    break;
                }
                }
            return;
            }
        }
    }

    /**
     * Lists all internship opportunities in the system.
     *
     * @param companyRepController controller retrieving opportunity list
     */
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

    /**
     * Allows the logged-in company representative to change their password.
     *
     * <p>Validates the old password before updating to a new one
     * and saves the change to persistent storage.
     *
     * @param context   application context containing the logged-in user
     * @param sc        scanner for user input
     * @param controller controller responsible for saving user data
     */
    public void changePassword(AppContext context, Scanner sc, CompanyRepController controller){
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
     * Allows company representatives to filter all internship opportunities in the system.
     *
     * <p>Filterable fields:
     * <ul>
     *   <li>Internship level</li>
     *   <li>Preferred major</li>
     *   <li>Status</li>
     *   <li>Opening date (earliest)</li>
     *   <li>Closing date (latest)</li>
     * </ul>
     *
     * <p>This method:
     * <ol>
     *   <li>Prompts for filter criteria</li>
     *   <li>Validates all user input</li>
     *   <li>Saves the filter to session</li>
     *   <li>Displays filtered results</li>
     *   <li>Allows recursive editing of filters</li>
     * </ol>
     *
     * @param sc                     scanner for user input
     * @param companyRepController   controller providing filtered results
     * @param context                application context storing the filter
     */
    public void filterOpportunities(Scanner sc, CompanyRepController companyRepController, AppContext context){
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
                    default -> System.out.println(ConsoleColors.RED+"Invalid choice. Please try again.\n"+ConsoleColors.RESET);
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
        ArrayList<InternshipOpportunity> list = companyRepController.filterOpportunities(filter);
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
            filterOpportunities(sc, companyRepController, context);
        }
    }
}
