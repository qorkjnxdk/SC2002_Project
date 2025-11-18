package boundaries;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import controllers.CareerStaffController;
import controllers.StudentController;
import entities.CompanyRepCreationReq;
import entities.InternshipOpportunity;
import entities.InternshipOpportunity.InternshipLevel;
import entities.InternshipOpportunity.Major;
import entities.InternshipOpportunity.Status;
import entities.InternshipWithdrawalReq;
import entities.User;
import entities.Filter;
import util.AppContext;


public class CareerStaffView {
    private CareerStaffController careerStaffController;
    public CareerStaffView(CareerStaffController careerStaffController){
        this.careerStaffController = careerStaffController;
    };
    public void run(AppContext Context, Scanner sc){
        while (true) {
            System.out.println("\n===============================");
            System.out.println("Welcome Back " + Context.getSession().getUser().getName());
            System.out.println("Please indicate your action:");
            System.out.println("1. View All Internship Opportunity Creation Requests");
            System.out.println("2. View All Company Representative Account Creation Requests");
            System.out.println("3. View All Withdrawal Requests");
            System.out.println("4. Generate Report on Opportunities");
            System.out.println("5. Filter All Opportunities");
            System.out.println("6. Change Password");
            System.out.println("7. Logout");
            System.out.println("=================================");
            int choice = sc.nextInt();
            switch (choice) {
                case 1 -> viewPendingOpportunities(sc, careerStaffController);
                case 2 -> viewPendingAccountCreationReqs(sc, careerStaffController);
                case 3 -> viewWithdrawalReqs(sc, careerStaffController);
                case 4 -> generateReport(careerStaffController);
                case 5 -> filterOpportunities(sc, careerStaffController, Context);
                case 6 -> changePassword(Context, sc, careerStaffController);
                case 7 -> {
                    Context.clearSession();
                    careerStaffController.saveFiles();
                    System.out.println("You have been logged out.");
                    return;
                }
                default -> System.out.println("Invalid choice. Please enter 1-7.");
            }
        }
    }
    public void viewPendingAccountCreationReqs(Scanner sc, CareerStaffController careerStaffController){
        ArrayList<CompanyRepCreationReq> companyRepCreationReqList = careerStaffController.getPendingAccountCreationReqs();
        if(companyRepCreationReqList.size()==0){
            System.out.println("\nThere are no pending account creation requests");
            return;
        }
        System.out.println("\nAll Pending Account Creation Requests:");
        for (int i = 0; i < companyRepCreationReqList.size(); i++) {
            CompanyRepCreationReq req = companyRepCreationReqList.get(i);
            System.out.println((i + 1) + ". " + req.getName() + ", "
                + req.getCompanyName() + ", "
                + req.getDepartment() + ", "
                + req.getUserID());
        }
        System.out.println("\nWould you like to make a new withdrawal request? (Y/N)");
        String choice2 = sc.next();
        if (choice2.equalsIgnoreCase("Y")){
            while (true) {
                System.out.println("\nWhich account do you want to approve? (enter the index number, or -1 to return)");
                int choice3 = sc.nextInt();
                if (choice3 == -1) {
                    return;
                }
                if (choice3 < 1 || choice3 > companyRepCreationReqList.size()){
                    System.out.println("Invalid selection. Please enter a valid index or -1 to return.");
                    continue; 
                }
                CompanyRepCreationReq selected = companyRepCreationReqList.get(choice3 - 1);
                careerStaffController.addCompanyRepAcct(selected);
                System.out.println("Approved account for: " + selected.getName() + " (" + selected.getUserID() + ")");
                break;
            }
        }
    }
    public void viewPendingOpportunities(Scanner sc, CareerStaffController careerStaffController){
        ArrayList<InternshipOpportunity> opps = careerStaffController.getPendingInternshipOpportunities();
        if(opps.size()==0){
            System.out.println("\nThere are no pending internship opportunity creation requests");
            return;
        }
        System.out.println("\nAll Pending Internship Opportunity Creation Requests:");
        for (int i = 0; i < opps.size(); i++) {
            InternshipOpportunity req = opps.get(i);
            System.out.println((i + 1) + ". " + req.getInternshipTitle() + ", "
                + req.getCompanyName() + ", "
                + req.getDepartment());
        }
        System.out.println("\nWould you like to approve an internship opportunity creation request? (Y/N)");
        String choice2 = sc.next();
        if (choice2.equalsIgnoreCase("Y")){
            while (true) {
                System.out.println("\nWhich account do you want to approve? (enter the index number, or -1 to return)");
                int choice3 = sc.nextInt();
                if (choice3 == -1) {
                    return;
                }
                if (choice3 < 1 || choice3 > opps.size()){
                    System.out.println("Invalid selection. Please enter a valid index or -1 to return.");
                    continue; 
                }
                InternshipOpportunity selected = opps.get(choice3 - 1);
                careerStaffController.approveInternshipOpportunity(selected);
                System.out.println("Approved creation of opportunity - " + selected.getInternshipTitle() + " ("+ selected.getCompanyName()+")");
                break;
            }
        }
    }
    public void viewWithdrawalReqs(Scanner sc, CareerStaffController careerStaffController){
        ArrayList<InternshipWithdrawalReq> reqs = careerStaffController.getInternshipWithdrawalReqs();
        if(reqs.size()==0){
            System.out.println("\nThere are no pending internship withdrawal requests");
            return;
        }
        System.out.println("\nAll Pending Internship Withdrawal Requests:");
        for (int i = 0; i < reqs.size(); i++) {
            InternshipWithdrawalReq req = reqs.get(i);
            System.out.println((i + 1) + ". " + req.getUserID() + ", "
                + req.getCompanyName() + ", "
                + req.getInternshipTitle() + ", \nReason: "
                + req.getWithdrawalReason());
        }
        System.out.println("\nWould you like to approve any? (Y/N)");
        String choice2 = sc.next();
        if (choice2.equalsIgnoreCase("Y")){
            while (true) {
                System.out.println("\nWhich request do you want to approve? (enter the index number, or -1 to return)");
                int choice3 = sc.nextInt();
                if (choice3 == -1) {
                    return;
                }
                if (choice3 < 1 || choice3 > reqs.size()){
                    System.out.println("Invalid selection. Please enter a valid index or -1 to return.");
                    continue; 
                }
                InternshipWithdrawalReq selected = reqs.get(choice3 - 1);
                careerStaffController.approveWithrawReq(selected);
                System.out.println("Approved withdrawal request for: " + selected.getInternshipTitle() + " (" + selected.getUserID() + ")");
                break;
            }
        }
    }
    public void changePassword(AppContext context, Scanner sc, CareerStaffController controller){
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
    public void generateReport(CareerStaffController careerStaffController){
        ArrayList<InternshipOpportunity> list = careerStaffController.getInternshipOpportunityList();
        if (list.isEmpty()){
            System.out.println("\nThere are no internship opportunities to display.");
            return;
        }
        System.out.println("\nAll Internship Opportunities:");
        StringBuilder report = new StringBuilder();
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
            System.out.println(line1);
            System.out.println(line2);
            System.out.println(line3);
            report.append(line1).append(System.lineSeparator())
                .append(line2).append(System.lineSeparator())
                .append(line3).append(System.lineSeparator())
                .append(System.lineSeparator());
            i++;
        }
        Path reportPath = Path.of("output", "internship_report.txt");
        try {
            Files.writeString(reportPath, report.toString(), StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("\nReport saved to " + reportPath.toAbsolutePath());
        } catch (IOException e) {
            System.out.println("Failed to save report: " + e.getMessage());
        }
    }
    public void filterOpportunities(Scanner sc, CareerStaffController careerStaffController, AppContext context){
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
        ArrayList<InternshipOpportunity> list = careerStaffController.filterOpportunities(filter);
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
            filterOpportunities(sc, careerStaffController, context);
        }
    }
}
