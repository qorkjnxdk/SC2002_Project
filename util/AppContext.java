package util;

import controllers.AuthController;
import entities.Session;

/**
 * Holds shared application-level state for the NTU Internship System.
 *
 * <p>The {@code AppContext} acts as a central container that stores:</p>
 * <ul>
 *     <li>The application's {@link AuthController} instance</li>
 *     <li>The currently active {@link Session}, if any</li>
 * </ul>
 *
 * <p>This class is passed to different views (StudentView, CompanyRepView, CareerStaffView, LoginView)
 * to allow them to access authentication logic and maintain or update the current user session.</p>
 *
 * <p>It acts as a lightweight dependency-injection mechanism for the system.</p>
 */
public class AppContext {
    private AuthController authController;
    private Session session;
     /**
     * Creates a new application context containing the given authentication controller.
     *
     * @param authController the AuthController to associate with this context
     */
    public AppContext(AuthController authController){
        this.authController = authController;
    }
    /**
     * Returns the authentication controller used throughout the application.
     *
     * @return the AuthController instance
     */
    public AuthController getAuthController(){
        return authController;
    }
    /**
     * Returns the currently active session.
     *
     * @return the active {@link Session}, or {@code null} if no user is logged in
     */
    public Session getSession(){
        return session;
    }

    /**
     * Updates the active user session.
     *
     * @param session the session to set as active
     */
    public void setSession(Session session){
        this.session = session;
    }
    /**
     * Clears the current session, effectively logging out any active user.
     */
    public void clearSession(){
        this.session = null;
    }
}
