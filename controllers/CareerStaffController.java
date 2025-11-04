package controllers;

import java.util.ArrayList;

import entities.CompanyRep;
import entities.CompanyRepCreationReq;
import entities.User.Role;
import repositories.OpportunityRepository;
import repositories.RequestRepository;
import repositories.UserRepository;
import util.SaveFiles;
import util.UserFactory;
import entities.User;

public class CareerStaffController {
    private UserRepository users;
    private RequestRepository requests;
    private OpportunityRepository opportunities;
    public CareerStaffController(RequestRepository requests, UserRepository users, OpportunityRepository opportunities){
        this.users = users;
        this.requests = requests;
        this.opportunities = opportunities;
    }
    public ArrayList<CompanyRepCreationReq> getPendingAccountCreationReqs(){
        return requests.getAllCompanyRepCreationReq();
    }
    public void addCompanyRepAcct(CompanyRepCreationReq req){
        requests.deleteCompanyReqCreationReq(req);
        UserFactory userFactory = new UserFactory();
        String data = req.getUserID() + "," + req.getName() + "," + req.getCompanyName() + "," + req.getDepartment() + "," + req.getDepartment() + ",password";
        User user = userFactory.addUser(data, Role.COMPANY_REP);
        CompanyRep companyRep = (CompanyRep)user;
        users.addCompanyRep(companyRep);
    }
    public void saveFiles(){
        SaveFiles saveFiles = new SaveFiles();
        saveFiles.saveCompanyRepCSV(users);
        saveFiles.saveCompanyRepReqCSV(requests);
    }
}
