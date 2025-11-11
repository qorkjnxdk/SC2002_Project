package repositories;
import java.util.ArrayList;

import entities.InternshipOpportunity;
import java.util.stream.Collectors;

public class OpportunityRepository {
    private ArrayList<InternshipOpportunity> internshipOpportunityList = new ArrayList<>();
    public OpportunityRepository(){}
    public ArrayList<InternshipOpportunity> getInternshipOpportunityList(){
        return internshipOpportunityList;
    }
    public ArrayList<InternshipOpportunity> getRelevantInternshipOpportunities(String name){
        ArrayList<InternshipOpportunity> filteredOpportunities= internshipOpportunityList.stream()
        .filter(s->s.getCompanyRepInCharge().equals(name))
        .collect(Collectors.toCollection(ArrayList::new));
        return filteredOpportunities;
    }
    public void addInternshipOpportunity(InternshipOpportunity internshipOpportunity){
        internshipOpportunityList.add(internshipOpportunity);
    }
}
