import repositories.RequestRepository;
import repositories.UserRepository;
import util.AppContext;
import util.LoadFiles;
import boundaries.*;
import controllers.*;

import java.util.Scanner;

public class internshipPlacementManagementSystem{
    public static void main(String[] args) {
        UserRepository userRepository = new UserRepository();
        RequestRepository requestRepository = new RequestRepository();

        LoadFiles ld = new LoadFiles();
        ld.loadStudentCSV(userRepository);
        ld.loadCareerStaffCSV(userRepository);
        ld.loadCompanyRepCSV(requestRepository);

        CompanyRepController companyRepController = new CompanyRepController(requestRepository, userRepository);
        StudentController studentController = new StudentController(userRepository);
        CareerStaffController careerStaffController = new CareerStaffController(requestRepository, userRepository);
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