import repositories.RequestRepository;
import repositories.UserRepository;
import util.AppContext;
import util.LoadFiles;
import boundaries.*;
import controllers.AuthController;
import java.util.Scanner;

public class internshipPlacementManagementSystem{
    public static void main(String[] args) {
        UserRepository userRepository = new UserRepository();
        RequestRepository requestRepository = new RequestRepository();

        LoadFiles ld = new LoadFiles();
        ld.loadStudentCSV(userRepository);
        ld.loadCareerStaffCSV(userRepository);
        ld.loadCompanyRepCSV(requestRepository);

        AuthController authController = new AuthController(userRepository);
        Scanner sc = new Scanner(System.in);

        AppContext Context = new AppContext(authController);

        StudentView studentView = new StudentView(userRepository);
        CompanyRepView companyRepView = new CompanyRepView(userRepository);
        CareerStaffView careerStaffView = new CareerStaffView(userRepository, requestRepository);
        LoginView loginView = new LoginView(careerStaffView, companyRepView, studentView);
        loginView.run(Context, sc);
    }
}