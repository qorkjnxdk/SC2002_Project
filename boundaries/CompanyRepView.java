package boundaries;
import java.util.Scanner;

import util.AppContext;
import controllers.CompanyRepController;

public class CompanyRepView {
    private CompanyRepController companyRepController;
    public CompanyRepView(CompanyRepController companyRepController){
        this.companyRepController =  companyRepController;
    };
    public void run(AppContext Context, Scanner sc){
        System.out.println("Career Staff View Here");
        //Menu 
        //choice 1.
        createInternshipOpportunity();
        //choice 2.
        viewInternshipOpportunityRequests();
    }
    public void viewInternshipOpportunityRequests(){

    }
    public void createInternshipOpportunity(){
        companyRepController.addInternshipOpportunity("Helo");
    }
}
