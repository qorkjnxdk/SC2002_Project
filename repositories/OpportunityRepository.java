package repositories;
import java.util.ArrayList;

import entities.Filter;
import entities.InternshipOpportunity;
import entities.InternshipOpportunity.Status;

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
    public ArrayList<InternshipOpportunity> getPendingInternshipOpportunities(){
        ArrayList<InternshipOpportunity> pendingOpportunities= internshipOpportunityList.stream()
        .filter(s->s.getStatus().equals(Status.PENDING))
        .collect(Collectors.toCollection(ArrayList::new));
        return pendingOpportunities;
    }
    public void addInternshipOpportunity(InternshipOpportunity internshipOpportunity){
        internshipOpportunityList.add(internshipOpportunity);
    }
    public ArrayList<InternshipOpportunity> filterOpportunities(Filter filter){
        return internshipOpportunityList.stream()
            .filter(opp -> filter.getStatus() == null || opp.getStatus().equals(filter.getStatus()))
            .filter(opp -> filter.getMajor() == null || opp.getPreferredMajor().equals(filter.getMajor()))
            .filter(opp -> filter.getLevel() == null || opp.getInternshipLevel().equals(filter.getLevel()))
            .filter(opp -> filter.getEarliestOpeningDate() == null || opp.getApplicationOpeningDate().compareTo(filter.getEarliestOpeningDate()) >= 0)
            .filter(opp -> filter.getLatestClosingDate() == null || opp.getApplicationClosingDate().compareTo(filter.getLatestClosingDate()) <= 0)
            .collect(Collectors.toCollection(ArrayList::new));
    }
}
