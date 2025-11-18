package boundaries;
import java.util.Scanner;
import controllers.AuthController;
import exceptions.*;
import repositories.OpportunityRepository;
import repositories.RequestRepository;
import repositories.UserRepository;
import util.AppContext;
import util.ConsoleColors;
import util.SaveFiles;
import entities.CompanyRepCreationReq;
import entities.Session;

public class LoginView {
    private CareerStaffView careerStaffView;
    private CompanyRepView companyRepView;
    private StudentView studentView;
    private AuthController authController;
    private RequestRepository requests;
    private UserRepository users;
    private OpportunityRepository opportunities;
    public LoginView(CareerStaffView careerStaffView, CompanyRepView companyRepView, StudentView studentView, RequestRepository requests){
        this.careerStaffView = careerStaffView;
        this.companyRepView = companyRepView;
        this.studentView = studentView;
        this.requests = requests;
    }
    public void run(AppContext Context, Scanner sc){
        authController = Context.getAuthController();
        Session session = null;
        while (true) {
            System.out.println("\n===== NTU Internship System =====");
            System.out.println("Please indicate your role:");
            System.out.println("1. Student");
            System.out.println("2. Career Centre Staff");
            System.out.println("3. Company Representative");
            System.out.println("4. For Company Rep Account Creation");
            System.out.println("=================================");
            if (!sc.hasNextInt()) {
                System.out.println(ConsoleColors.RED+"\nInvalid choice!"+ConsoleColors.RESET);
                sc.nextLine();
                continue;
            }
            int role = sc.nextInt();
            if(role==4){
                System.out.println("======================================");
                System.out.println("\nWelcome to the application page for Company Reps!\n");
                System.out.println("======================================");
                System.out.println("Enter Your Username:");
                sc.nextLine();
                String username = sc.nextLine();
                System.out.println("Enter Your Name:");
                String name = sc.nextLine();
                System.out.println("Enter Your Company Name:");
                String companyName = sc.nextLine();
                System.out.println("Enter Your Department:");
                String department = sc.nextLine();
                System.out.println("Enter Your Position:");
                String position = sc.nextLine();
                CompanyRepCreationReq req = new CompanyRepCreationReq(username,name,companyName,department,position);
                requests.addCompanyReqCreationReq(req);
                SaveFiles savefiles = new SaveFiles();
                savefiles.saveCompanyRepReqCSV(requests);
                System.out.println(ConsoleColors.GREEN+"\nRequest successfully created!"+ConsoleColors.RESET);
                break;
            }
            if(role<1 || role>4){
                System.out.println(ConsoleColors.RED+"\nInvalid choice!"+ConsoleColors.RESET);
                continue;
            }
            System.out.println("Enter Your Username");
            String username = sc.next();
            System.out.println("Enter Your Password");
            String password = sc.next();
            System.out.println();
            try{
                session = authController.login(username, password, role);
                Context.setSession(session);
            } catch(InvalidInputException e){
                System.out.println(ConsoleColors.RED+e.getMessage()+ConsoleColors.RESET);
                continue;
            } catch(UserDoesntExistException e){
                System.out.println(ConsoleColors.RED+e.getMessage()+ConsoleColors.RESET);
                continue;
            } catch(WrongPasswordException e){
                System.out.println(ConsoleColors.RED+e.getMessage()+ConsoleColors.RESET);
                continue;
            }
            if (session == null) continue;

            switch (session.getRole()) {
                case STUDENT:
                    studentView.run(Context, sc);
                    break;
                case CAREER_STAFF:
                    careerStaffView.run(Context, sc);
                    break;
                case COMPANY_REP:
                    companyRepView.run(Context, sc);
                    break;
            }
        }
    }
}
