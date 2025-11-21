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

/**
 * Entry-point view class for handling all login interactions and role-based routing.
 *
 * <p>This boundary class:
 * <ul>
 *   <li>Displays login menu</li>
 *   <li>Authenticates users via {@link AuthController}</li>
 *   <li>Routes authenticated users to their respective views:
 *      <ul>
 *        <li>{@link StudentView}</li>
 *        <li>{@link CareerStaffView}</li>
 *        <li>{@link CompanyRepView}</li>
 *      </ul>
 *   </li>
 *   <li>Handles Company Representative account creation requests</li>
 * </ul>
 */
public class LoginView {
    private CareerStaffView careerStaffView;
    private CompanyRepView companyRepView;
    private StudentView studentView;
    private AuthController authController;
    private RequestRepository requests;
     /**
     * Constructs a LoginView containing the different user views and request storage.
     *
     * @param careerStaffView view for career staff functions
     * @param companyRepView  view for company representative functions
     * @param studentView     view for student functions
     * @param requests        repository storing account creation requests
     */
    public LoginView(CareerStaffView careerStaffView, CompanyRepView companyRepView, StudentView studentView, RequestRepository requests){
        this.careerStaffView = careerStaffView;
        this.companyRepView = companyRepView;
        this.studentView = studentView;
        this.requests = requests;
    }

     /**
     * Main login interface loop.  
     * Displays menu for selecting a role, handles authentication, and routes
     * the user to the appropriate view based on their role.
     *
     * <p>Supported roles:
     * <ul>
     *     <li>Student</li>
     *     <li>Career Centre Staff</li>
     *     <li>Company Representative</li>
     *     <li>Company Representative Account Application</li>
     * </ul>
     *
     * <p>This method will continue looping until:
     * <ul>
     *     <li>A Company Rep application is created, or</li>
     *     <li>The user logs in and subsequently logs out.</li>
     * </ul>
     *
     * @param context application context containing global data and the session manager
     * @param sc      scanner used for user input
     */
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
                continue;
            }
            if(role<1 || role>4){
                System.out.println(ConsoleColors.RED+"\nInvalid choice!"+ConsoleColors.RESET);
                continue;
            }
            System.out.println("Enter Your Username");
            String username = sc.next();
            System.out.println("Enter Your Password (Default is password for first-time users)");
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
