package entities;
import entities.User.Role;

/**
 * Represents an active user session in the NTU Internship System.
 *
 * <p>A session is created immediately after a user successfully logs in,
 * and it stores key information used throughout the system, including:</p>
 *
 * <ul>
 *     <li>The user's ID</li>
 *     <li>The full {@link User} object</li>
 *     <li>The user's role (Student, Company Representative, Career Staff)</li>
 *     <li>An optional {@link Filter} object used to store applied filter criteria</li>
 * </ul>
 *
 * <p>The session persists until the user logs out or the system resets their session.</p>
 */
public class Session {
    private String userID;
    private User user;
    private Role role;
    private Filter filter;
     /**
     * Constructs a new session for a logged-in user.
     *
     * @param userID the ID of the logged-in user
     * @param user   the {@link User} object associated with the session
     * @param role   the role of the user (Student, Career Staff, or Company Rep)
     */
    public Session(String userID, User user, Role role){
        this.userID = userID;
        this.role = role;
        this.user = user;
        this.filter = null;
    }
    /**
     * Returns the {@link User} associated with this session.
     *
     * @return the user object
     */
    public User getUser(){
        return user;
    }
    /**
     * Returns the ID of the logged-in user.
     *
     * @return the user ID
     */
    public String getUserId(){
        return userID;
    }
     /**
     * Returns the role of the logged-in user.
     *
     * @return the user's role
     */
    public Role getRole(){
        return role;
    }
    /**
     * Returns the currently applied filter for this session.
     *
     * @return the filter, or {@code null} if no filter is set
     */
    public Filter getFilter(){
        return filter;
    }
     /**
     * Sets a new filter for the session.
     *
     * @param filter the filter to apply, or {@code null} to clear it
     */
    public void setFilter(Filter filter){
        this.filter = filter;
    }
}
