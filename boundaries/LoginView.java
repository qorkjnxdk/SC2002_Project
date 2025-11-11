package boundaries;
import java.util.Scanner;
import controllers.AuthController;
import exceptions.*;
import util.AppContext;
import entities.Session;
import entities.User.Role;

public class LoginView {
    private CareerStaffView careerStaffView;
    private CompanyRepView companyRepView;
    private StudentView studentView;
    private AuthController authController;
    public LoginView(CareerStaffView careerStaffView, CompanyRepView companyRepView, StudentView studentView){
        this.careerStaffView = careerStaffView;
        this.companyRepView = companyRepView;
        this.studentView = studentView;
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
            System.out.println("=================================");
            int role = sc.nextInt();
            System.out.println("Enter Your Username");
            String username = sc.next();
            System.out.println("Enter Your Password");
            String password = sc.next();
            System.out.println();
            try{
                session = authController.login(username, password, role);
                Context.setSession(session);
            } catch(InvalidInputException e){
                System.out.println(e.getMessage());
                continue;
            } catch(UserDoesntExistException e){
                System.out.println(e.getMessage());
                continue;
            } catch(WrongPasswordException e){
                System.out.println(e.getMessage());
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
