package repositories;
import java.util.ArrayList;
import entities.CompanyRepCreationReq;

public class RequestRepository {
    private ArrayList<CompanyRepCreationReq> companyRepCreationReqList = new ArrayList<>();
    public RequestRepository(){}
    public ArrayList<CompanyRepCreationReq> getAllCompanyRepCreationReq(){
        return companyRepCreationReqList;
    }
    public void addCompanyReqCreationReq(CompanyRepCreationReq companyRepCreationReq){
        companyRepCreationReqList.add(companyRepCreationReq);
    }
}
