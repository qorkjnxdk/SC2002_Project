package boundaries;
import java.util.ArrayList;
import java.util.Scanner;

import entities.CompanyRep;
import entities.CompanyRepCreationReq;
import repositories.RequestRepository;
import repositories.UserRepository;
import util.AppContext;
import util.UserFactory;

public class CareerStaffView {
    private UserRepository users;
    private RequestRepository requests;
    public CareerStaffView(UserRepository users, RequestRepository requests){
        this.users = users;
        this.requests = requests;
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
                    viewPendingAccountCreationReqs(users, requests, sc);
                    break;
                case 3:
                    System.out.println("Feature not implemented yet.");
                    break;
                case 4:
                    Context.clearSession();
                    System.out.println("You have been logged out.");
                    return;
                default:
                    System.out.println("Invalid choice. Please enter 1-4.");
                    break;
            }
        }
    }
    public static void viewPendingAccountCreationReqs(UserRepository users, RequestRepository requests, Scanner sc){
        System.out.println("\nAll Pending Account Creation Requests:");
        ArrayList<CompanyRepCreationReq> companyRepCreationReqList = requests.getAllCompanyRepCreationReq();
        for (int i = 0; i < companyRepCreationReqList.size(); i++) {
            CompanyRepCreationReq companyRepCreationReq = companyRepCreationReqList.get(i);
            System.out.println((i + 1) + ". " + companyRepCreationReq.getName() + ", "
                + companyRepCreationReq.getCompanyName() + ", "
                + companyRepCreationReq.getDepartment() + ", "
                + companyRepCreationReq.getUserID());
        }
        System.out.println("\nWould you like to approve any? (Y/N)");
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
                requests.getAllCompanyRepCreationReq().remove(choice3 - 1);
                UserFactory userFactory = new UserFactory();
                CompanyRep companyRep = userFactory.addCompanyRep(selected);
                users.addCompanyRep(companyRep);
                System.out.println("Approved account for: " + selected.getName() + " (" + selected.getUserID() + ")");
                break;
            }
        }
    }
}
