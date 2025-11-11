package repositories;
import java.util.ArrayList;
import entities.CompanyRepCreationReq;
import entities.InternshipWithdrawalReq;

public class RequestRepository {
    private ArrayList<CompanyRepCreationReq> companyRepCreationReqList = new ArrayList<>();
    private ArrayList<InternshipWithdrawalReq> internshipWithdrawalReqList = new ArrayList<>();
    public RequestRepository(){}
    public ArrayList<CompanyRepCreationReq> getAllCompanyRepCreationReq(){
        return companyRepCreationReqList;
    }
    public ArrayList<InternshipWithdrawalReq> getInternshipWithdrawalReqList(){
        return internshipWithdrawalReqList;
    }
    public void addCompanyReqCreationReq(CompanyRepCreationReq companyRepCreationReq){
        companyRepCreationReqList.add(companyRepCreationReq);
    }
    public void deleteCompanyReqCreationReq(CompanyRepCreationReq companyRepCreationReq){
        companyRepCreationReqList.remove(companyRepCreationReq);
    }
    public void addInternshipWithdrawalReq(InternshipWithdrawalReq internshipWithdrawalReq){
        internshipWithdrawalReqList.add(internshipWithdrawalReq);
    }
    public void deleteInternshipWithdrawalReq(InternshipWithdrawalReq internshipWithdrawalReq){
        internshipWithdrawalReqList.remove(internshipWithdrawalReq);
    }
}
