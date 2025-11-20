package exceptions;

/**
 * Exception thrown when a user provides an invalid or unacceptable input.
 *
 * <p>This exception is typically used for handling:</p>
 * <ul>
 *     <li>Empty required fields</li>
 *     <li>Incorrect menu selections</li>
 *     <li>Malformed values (e.g., dates, formats)</li>
 *     <li>Inputs outside expected ranges</li>
 * </ul>
 *
 * <p>It is commonly thrown in validation logic within controllers, such as
 * during login or filter creation.</p>
 */
public class InvalidInputException extends Exception{
    /**
     * Constructs a new InvalidInputException with a descriptive message.
     *
     * @param message the detail message explaining the cause of the exception
     */
    public InvalidInputException(String message){
        super(message);
    }
}