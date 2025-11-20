import repositories.OpportunityRepository;
import repositories.RequestRepository;
import repositories.UserRepository;
import util.AppContext;
import util.LoadFiles;
import boundaries.*;
import controllers.*;

import java.util.Scanner;
/**
 * Entry point of the NTU Internship Management System.
 *
 * <p>This class is responsible for bootstrapping the entire application by:</p>
 * <ul>
 *     <li>Initializing all repositories used to store system data</li>
 *     <li>Loading persisted data from CSV files through {@link LoadFiles}</li>
 *     <li>Constructing all controllers that contain business logic</li>
 *     <li>Constructing boundary (view) components for user interaction</li>
 *     <li>Creating the application context and authentication controller</li>
 *     <li>Starting the system via the main login screen</li>
 * </ul>
 *
 * <p>The application follows a simplified MVC architecture:</p>
 * <ul>
 *     <li><b>Repositories</b>: store entities in memory</li>
 *     <li><b>Controllers</b>: manipulate data and enforce system logic</li>
 *     <li><b>Views</b>: handle user input/output through the console</li>
 *     <li><b>AppContext</b>: stores session and exposes shared controllers</li>
 * </ul>
 *
 * <p>This class contains only the main method and should not hold
 * any business logic.</p>
 */
public class App{
    /**
     * Launches the Internship Management System.
     *
     * <p>The startup sequence includes:</p>
     * <ol>
     *     <li>Initializing repositories</li>
     *     <li>Loading CSV data for users, opportunities, and requests</li>
     *     <li>Setting up controllers for each system role</li>
     *     <li>Creating view classes for user-facing interaction</li>
     *     <li>Initializing the application context</li>
     *     <li>Handing control over to {@link LoginView}</li>
     * </ol>
     *
     * @param args console arguments (not used)
     */
    public static void main(String[] args) {
        UserRepository userRepository = new UserRepository();
        RequestRepository requestRepository = new RequestRepository();
        OpportunityRepository opportunityRepository = new OpportunityRepository();

        LoadFiles ld = new LoadFiles();
        ld.loadCSVs(userRepository, requestRepository, opportunityRepository);

        CompanyRepController companyRepController = new CompanyRepController(requestRepository, userRepository, opportunityRepository);
        StudentController studentController = new StudentController(requestRepository, userRepository, opportunityRepository);
        CareerStaffController careerStaffController = new CareerStaffController(requestRepository, userRepository, opportunityRepository);
        AuthController authController = new AuthController(userRepository);
        Scanner sc = new Scanner(System.in);

        AppContext Context = new AppContext(authController);

        StudentView studentView = new StudentView(studentController);
        CompanyRepView companyRepView = new CompanyRepView(companyRepController);
        CareerStaffView careerStaffView = new CareerStaffView(careerStaffController);
        LoginView loginView = new LoginView(careerStaffView, companyRepView, studentView,requestRepository);
        loginView.run(Context, sc);
    }
}