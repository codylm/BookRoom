package bookroom;

/**
 * An exception used to handle user-provided indexes potentially causing ArrayIndexOutOfBoundsExceptions
 * @author Cody
 * @version 1.0
 * @since 1.0
 */

public class InvalidNumberException extends Exception
{
    public InvalidNumberException(String message)
    {
        super(message);
    }
}
