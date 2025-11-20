package repositories;
import java.util.ArrayList;

import entities.Filter;
import entities.InternshipOpportunity;
import entities.InternshipOpportunity.Status;

import java.util.stream.Collectors;

/**
 * Repository class that stores and manages all {@link InternshipOpportunity} records
 * within the system.
 *
 * <p>This repository provides methods to:</p>
 * <ul>
 *     <li>Retrieve all internship opportunities</li>
 *     <li>Retrieve opportunities owned by a specific company representative</li>
 *     <li>Retrieve pending opportunities awaiting approval</li>
 *     <li>Add new internship opportunities</li>
 *     <li>Filter opportunities based on a {@link Filter}</li>
 * </ul>
 *
 * <p>The repository internally uses an {@code ArrayList} as its storage structure.</p>
 */
public class OpportunityRepository {
    private ArrayList<InternshipOpportunity> internshipOpportunityList = new ArrayList<>();
    public OpportunityRepository(){}

    /**
     * Returns the complete list of internship opportunities.
     *
     * @return a list of all internship opportunities
     */
    public ArrayList<InternshipOpportunity> getInternshipOpportunityList(){
        return internshipOpportunityList;
    }
    /**
     * Retrieves internship opportunities created by or assigned to a specific
     * company representative.
     *
     * @param name the user ID or name of the company representative in charge
     * @return the list of relevant internship opportunities
     */
    public ArrayList<InternshipOpportunity> getRelevantInternshipOpportunities(String name){
        ArrayList<InternshipOpportunity> filteredOpportunities= internshipOpportunityList.stream()
        .filter(s->s.getCompanyRepInCharge().equals(name))
        .collect(Collectors.toCollection(ArrayList::new));
        return filteredOpportunities;
    }
      /**
     * Retrieves all internship opportunities that are still pending approval.
     *
     * @return a list of opportunities with status {@link Status#PENDING}
     */
    public ArrayList<InternshipOpportunity> getPendingInternshipOpportunities(){
        ArrayList<InternshipOpportunity> pendingOpportunities= internshipOpportunityList.stream()
        .filter(s->s.getStatus().equals(Status.PENDING))
        .collect(Collectors.toCollection(ArrayList::new));
        return pendingOpportunities;
    }

     /**
     * Adds a new internship opportunity to the repository.
     *
     * @param internshipOpportunity the opportunity to be added
     */
    public void addInternshipOpportunity(InternshipOpportunity internshipOpportunity){
        internshipOpportunityList.add(internshipOpportunity);
    }

    /**
     * Filters internship opportunities based on the criteria provided in the
     * specified {@link Filter}.
     *
     * <p>The filtering criteria may include:</p>
     * <ul>
     *     <li>Status</li>
     *     <li>Preferred student major</li>
     *     <li>Internship level</li>
     *     <li>Earliest application opening date</li>
     *     <li>Latest application closing date</li>
     * </ul>
     *
     * <p>Any {@code null} filter values are ignored.</p>
     *
     * @param filter the filtering criteria to apply
     * @return a filtered list of internship opportunities
     */
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
