package boundaries;
import java.util.Scanner;

import controllers.StudentController;
import util.AppContext;

public class StudentView{
    private StudentController studentController;
    public StudentView(StudentController studentController){
        this.studentController = studentController;
    };
    public void run(AppContext Context, Scanner sc){
        System.out.println("Student View Here");
        int choice = sc.nextInt();
        if(choice==1){
            Context.clearSession();
            System.out.println("You have been logged out!");
            return;
        }
    }
}
