package boundaries;
import java.util.Scanner;

import repositories.UserRepository;
import util.AppContext;

public class CompanyRepView {
    private UserRepository users;
    public CompanyRepView(UserRepository users){
        this.users = users;
    };
    public void run(AppContext Context, Scanner sc){
        System.out.println("Career Staff View Here");
    }
}
