package entities;

import entities.InternshipOpportunity.InternshipLevel;
import entities.InternshipOpportunity.Major;
import entities.InternshipOpportunity.Status;

/**
 * Represents a set of filtering criteria applied to internship opportunities.
 *
 * <p>The {@code Filter} class is used by multiple views (Student, CompanyRep,
 * CareerStaff) to narrow down a list of {@link entities.InternshipOpportunity}
 * based on user-selected attributes.</p>
 *
 * <p>A filter may include:</p>
 * <ul>
 *     <li>Internship status (e.g., PENDING, APPROVED, REJECTED)</li>
 *     <li>Preferred major</li>
 *     <li>Internship level (BASIC, INTERMEDIATE, ADVANCED)</li>
 *     <li>Date constraints on application opening date</li>
 *     <li>Date constraints on application closing date</li>
 * </ul>
 *
 * <p>Any of these fields may be {@code null} to indicate that the user chose to skip that filter.</p>
 */

public class Filter {
    private Status status;
    private Major major;
    private InternshipLevel level ;
    private String earliestOpeningDate;
    private String latestClosingDate;

    /**
     * Constructs a filter with the given criteria.
     *
     * @param status               the internship status to filter by, or {@code null} if skipped
     * @param major                the preferred major to filter by, or {@code null} if skipped
     * @param level                the internship level to filter by, or {@code null} if skipped
     * @param earliestOpeningDate  the earliest acceptable opening date (YYYY-MM-DD), or {@code null}
     * @param latestClosingDate    the latest acceptable closing date (YYYY-MM-DD), or {@code null}
     */
    public Filter(Status status, Major major, InternshipLevel level, String earliestOpeningDate, String latestClosingDate){
        this.status = status;
        this.major = major;
        this.level = level;
        this.earliestOpeningDate = earliestOpeningDate;
        this.latestClosingDate = latestClosingDate;
    }
    
    /**
     * Constructs a filter with the given criteria.
     *
     * @param status               the internship status to filter by, or {@code null} if skipped
     * @param major                the preferred major to filter by, or {@code null} if skipped
     * @param level                the internship level to filter by, or {@code null} if skipped
     * @param earliestOpeningDate  the earliest acceptable opening date (YYYY-MM-DD), or {@code null}
     * @param latestClosingDate    the latest acceptable closing date (YYYY-MM-DD), or {@code null}
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Returns the major criteria.
     *
     * @return the required {@link Major}, or {@code null}
     */
    public Major getMajor() {
        return major;
    }

    /**
     * Returns the internship level criteria.
     *
     * @return the required {@link InternshipLevel}, or {@code null}
     */
    public InternshipLevel getLevel() {
        return level;
    }

     /**
     * Returns the earliest acceptable application opening date.
     *
     * @return the earliest opening date (YYYY-MM-DD), or {@code null}
     */
    public String getEarliestOpeningDate() {
        return earliestOpeningDate;
    }

    /**
     * Returns the latest acceptable application closing date.
     *
     * @return the latest closing date (YYYY-MM-DD), or {@code null}
     */
    public String getLatestClosingDate() {
        return latestClosingDate;
    }
}
