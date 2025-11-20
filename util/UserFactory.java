package util;
import entities.CareerStaff;
import entities.CompanyRep;
import entities.Student;
import entities.User.Role;
import entities.User;

/**
 * A factory class responsible for constructing {@link User} objects
 * based on CSV line input and a specified user role.
 *
 * <p>This class centralizes the creation logic for all user types
 * in the system, ensuring consistent parsing and object instantiation
 * across Student, CareerStaff, and CompanyRep records.</p>
 *
 * <p>CSV Format Requirements:</p>
 * <ul>
 *     <li><b>STUDENT</b>: {@code userId, name, major, year, email, password}</li>
 *     <li><b>CAREER_STAFF</b>: {@code userId, name, department, email, password}</li>
 *     <li><b>COMPANY_REP</b>: {@code userId, name, companyName, department, position, password}</li>
 * </ul>
 *
 * <p>If an unsupported role is provided, an {@link IllegalArgumentException} is thrown.</p>
 */
public class UserFactory {
    /**
     * Parses a CSV line and creates the appropriate {@link User} subclass instance
     * based on the given {@link Role}.
     *
     * @param line the CSV record containing all required user fields
     * @param role the type of user to construct (Student, CareerStaff, or CompanyRep)
     * @return a fully constructed {@link User} instance
     *
     * @throws IllegalArgumentException if the role is unknown
     * @throws ArrayIndexOutOfBoundsException if the CSV line does not contain enough fields
     */
    public User addUser(String line, Role role){
        switch (role) {
            case STUDENT:
                String[] data = line.split(",");
                int yearOfStudy = Integer.valueOf(data[3]);
                return new Student(data[0],data[1],data[2],yearOfStudy, data[4], data[5]);
            case CAREER_STAFF:
                String[] data2 = line.split(",");
                return new CareerStaff(data2[0],data2[1],data2[2],data2[3], data2[4]);
            case COMPANY_REP:
                String[] data3 = line.split(",");
                return new CompanyRep(data3[0],data3[1],data3[2],data3[3],data3[4], data3[5]);
            default:
                throw new IllegalArgumentException("Unknown role: " + role);        
        } 
    }
}
