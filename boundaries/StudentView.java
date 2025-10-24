package boundaries;
import java.util.Scanner;
import util.AppContext;

import repositories.UserRepository;

public class StudentView{
    private UserRepository users;
    public StudentView(UserRepository users){
        this.users = users;
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
