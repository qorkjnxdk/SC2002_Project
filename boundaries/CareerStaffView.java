package boundaries;
import java.util.ArrayList;
import java.util.Scanner;

import controllers.CareerStaffController;
import entities.CompanyRepCreationReq;
import entities.InternshipWithdrawalReq;
import util.AppContext;


public class CareerStaffView {
    private CareerStaffController careerStaffController;
    public CareerStaffView(CareerStaffController careerStaffController){
        this.careerStaffController = careerStaffController;
    };
    public void run(AppContext Context, Scanner sc){
        while (true) {
            System.out.println("\n===============================");
            System.out.println("Welcome Back " + Context.getSession().getUserId());
            System.out.println("Please indicate your action:");
            System.out.println("1. View All Internship Opportunity Creation Requests");
            System.out.println("2. View All Company Representative Account Creation Requests");
            System.out.println("3. View All Withdrawal Requests");
            System.out.println("4. Logout");
            System.out.println("=================================");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Feature not implemented yet.");
                    break;
                case 2:
                    viewPendingAccountCreationReqs(sc, careerStaffController);
                    break;
                case 3:
                    viewWithdrawalReqs(sc, careerStaffController);
                    break;
                case 4:
                    Context.clearSession();
                    careerStaffController.saveFiles();
                    System.out.println("You have been logged out.");
                    return;
                default:
                    System.out.println("Invalid choice. Please enter 1-4.");
                    break;
            }
        }
    }
    public void viewPendingAccountCreationReqs(Scanner sc, CareerStaffController careerStaffController){
        System.out.println("\nAll Pending Account Creation Requests:");
        ArrayList<CompanyRepCreationReq> companyRepCreationReqList = careerStaffController.getPendingAccountCreationReqs();
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
    public void viewWithdrawalReqs(Scanner sc, CareerStaffController careerStaffController){
        System.out.println("\nAll Pending Internship Withdrawal Requests:");
        ArrayList<InternshipWithdrawalReq> reqs = careerStaffController.getInternshipWithdrawalReqs();
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
                System.out.println("Approved withdrawal request for: " + selected.getInternshipTitle() + " (" + selected.getUserID() + ")");
                break;
            }
        }
    }
}
