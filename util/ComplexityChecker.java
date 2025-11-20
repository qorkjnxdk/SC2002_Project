package util;
/**
 * Utility class that checks whether a given password meets the system's
 * complexity requirements.
 *
 * <p>The complexity rules enforced are:</p>
 * <ul>
 *     <li>Password must be longer than 4 characters</li>
 *     <li>Password must contain at least one uppercase letter</li>
 *     <li>Password must contain at least one lowercase letter</li>
 *     <li>Password must contain at least one numeric digit</li>
 * </ul>
 *
 * <p>This checker is useful when validating new passwords during account creation
 * or password changes.</p>
 */
public class ComplexityChecker {
    /**
     * Evaluates the provided password and returns the result of the complexity check.
     *
     * <p>The returned string is one of the following messages:</p>
     * <ul>
     *     <li>{@code "Password is too short!"}</li>
     *     <li>{@code "Password must contain at least one uppercase letter!"}</li>
     *     <li>{@code "Password must contain at least one lowercase letter!"}</li>
     *     <li>{@code "Password must contain at least one number!"}</li>
     *     <li>{@code "Accepted"}</li>
     * </ul>
     *
     * @param password the password to evaluate
     * @return a string describing whether the password meets complexity requirements
     */
    public String checkComplexity(String password){
        if(password.length() <= 4){
            return("Password is too short!");
        }
        else if (!password.matches(".*[A-Z].*")) {
            return("Password must contain at least one uppercase letter!");
        }
        else if (!password.matches(".*[a-z].*")) {
            return("Password must contain at least one lowercase letter!");
        }
        else if (!password.matches(".*\\d.*")) {
            return("Password must contain at least one number!");
        }
        else {
            return("Accepted");
        }
    }
}
