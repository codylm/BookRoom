package bookroom;

/**
 * An exception to handle books being searched for not existing on the bookcase.
 * @author Cody
 * @version 1.0
 * @since 1.0
 */

public class BookDoesNotExistException extends Exception
{
    public BookDoesNotExistException(String message)
    {
        super(message);
    }
}