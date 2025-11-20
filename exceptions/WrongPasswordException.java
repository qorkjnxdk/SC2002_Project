package exceptions;
/**
 * Exception thrown when a user enters an incorrect password during authentication.
 *
 * <p>This exception is typically triggered when:</p>
 * <ul>
 *     <li>A valid user ID is provided, but the password does not match</li>
 *     <li>A password check fails during login</li>
 * </ul>
 *
 * <p>It is primarily thrown inside the {@link controllers.AuthController}
 * during the login validation process.</p>
 */
public class WrongPasswordException extends Exception{
     /**
     * Constructs a new WrongPasswordException with a descriptive message.
     *
     * @param message the detail message explaining why the password was considered incorrect
     */
    public WrongPasswordException(String message){
        super(message);
    }
}