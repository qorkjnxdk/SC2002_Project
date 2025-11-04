import repositories.OpportunityRepository;
import repositories.RequestRepository;
import repositories.UserRepository;
import util.AppContext;
import util.LoadFiles;
import boundaries.*;
import controllers.*;

import java.util.Scanner;

public class App{
    public static void main(String[] args) {
        UserRepository userRepository = new UserRepository();
        RequestRepository requestRepository = new RequestRepository();
        OpportunityRepository opportunityRepository = new OpportunityRepository();

        LoadFiles ld = new LoadFiles();
        ld.loadStudentCSV(userRepository);
        ld.loadCareerStaffCSV(userRepository);
        ld.loadCompanyRepCSV(userRepository);
        ld.loadCompanyRepReqCSV(requestRepository);
        ld.loadOpportunityCSV(opportunityRepository);

        CompanyRepController companyRepController = new CompanyRepController(requestRepository, userRepository, opportunityRepository);
        StudentController studentController = new StudentController(userRepository, opportunityRepository);
        CareerStaffController careerStaffController = new CareerStaffController(requestRepository, userRepository, opportunityRepository);
        AuthController authController = new AuthController(userRepository);
        Scanner sc = new Scanner(System.in);

        AppContext Context = new AppContext(authController);

        StudentView studentView = new StudentView(studentController);
        CompanyRepView companyRepView = new CompanyRepView(companyRepController);
        CareerStaffView careerStaffView = new CareerStaffView(careerStaffController);
        LoginView loginView = new LoginView(careerStaffView, companyRepView, studentView);
        loginView.run(Context, sc);
    }
}