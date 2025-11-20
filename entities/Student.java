package entities;

import entities.InternshipOpportunity.Major;

/**
 * Represents a student user in the NTU Internship System.
 *
 * <p>A student may:</p>
 * <ul>
 *     <li>Apply for internship opportunities</li>
 *     <li>Withdraw applications</li>
 *     <li>Accept internship offers</li>
 *     <li>Filter internship listings</li>
 *     <li>View and edit their own profile</li>
 * </ul>
 *
 * <p>This class extends {@link User} and adds student-specific fields such as
 * year of study, major, and email.</p>
 */
public class Student extends User{
    private int yearOfStudy;
    private Major major;
    private String email;

    /**
     * Constructs a new Student user with the given information.
     *
     * @param userID       the student's unique user ID
     * @param name         the student's full name
     * @param major        the student's major as a string (converted to enum)
     * @param yearOfStudy  the student's current academic year
     * @param email        the student's email address
     * @param password     the password used for login authentication
     */
    public Student(String userID, String name, String major,int yearOfStudy, String email, String password){
        super(userID, name, password);
        this.role = Role.STUDENT;
        this.yearOfStudy = yearOfStudy;
        this.email = email;
        this.major = switch(major){
            case "CCDS" -> Major.CCDS;
            case "IEEE" -> Major.IEEE;
            case "DSAI" -> Major.DSAI;
            default -> Major.CCDS;
        };
    }
     /**
     * Returns the academic year of study of the student.
     *
     * @return the year of study
     */
    public int getYearOfStudy(){
        return yearOfStudy;
    }
     /**
     * Returns the student's major.
     *
     * @return the major as an enum {@link Major}
     */
    public Major getMajor(){
        return major;
    }
    /**
     * Returns the student's email address.
     *
     * @return the student’s email
     */
    public String getEmail(){
        return email;
    }
}
