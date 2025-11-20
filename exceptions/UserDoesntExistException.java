package exceptions;
/**
 * Exception thrown when an attempted login or lookup references
 * a user that does not exist in the system.
 *
 * <p>This exception commonly occurs when:</p>
 * <ul>
 *     <li>A user enters a User ID that is not registered</li>
 *     <li>A lookup operation fails to find a matching account</li>
 *     <li>A controller checks a repository and finds no matching record</li>
 * </ul>
 *
 * <p>It is primarily thrown in {@link controllers.AuthController}
 * during authentication.</p>
 */
public class UserDoesntExistException extends Exception{
     /**
     * Constructs a new UserDoesntExistException with a detailed message.
     *
     * @param message the explanation of why the user was not found
     */
    public UserDoesntExistException(String message){
        super(message);
    }
}