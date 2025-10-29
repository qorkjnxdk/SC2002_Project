package controllers;

import java.util.ArrayList;
import java.util.Scanner;

import entities.CompanyRep;
import entities.CompanyRepCreationReq;
import repositories.RequestRepository;
import repositories.UserRepository;
import util.UserFactory;

public class CareerStaffController {
    private UserRepository users;
    private RequestRepository requests;
    public CareerStaffController(RequestRepository requests, UserRepository users){
        this.users = users;
        this.requests = requests;
    }
    public ArrayList<CompanyRepCreationReq> getPendingAccountCreationReqs(){
        return requests.getAllCompanyRepCreationReq();
    }
    public void addCompanyRepAcct(CompanyRepCreationReq companyRepCreationReq){
        requests.deleteCompanyReqCreationReq(companyRepCreationReq);
        UserFactory userFactory = new UserFactory();
        CompanyRep companyRep = userFactory.addCompanyRep(companyRepCreationReq);
        users.addCompanyRep(companyRep);
    }
}
