package repositories;
import java.util.ArrayList;
import entities.InternshipOpportunity;

public class OpportunityRepository {
    private ArrayList<InternshipOpportunity> internshipOpportunityList = new ArrayList<>();
    public OpportunityRepository(){}
    public ArrayList<InternshipOpportunity> getInternshipOpportunityReqList(){
        return internshipOpportunityList;
    }
    public void addCompanyReqCreationReq(InternshipOpportunity internshipOpportunity){
        internshipOpportunityList.add(internshipOpportunity);
    }
}
