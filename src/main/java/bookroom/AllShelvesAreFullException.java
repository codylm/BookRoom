package bookroom;

/**
 * An exception to handle all shelves being full when an attempt is made to add something to the bookcase.
 * @author Cody
 * @version 1.0
 * @since 1.0
 */

public class AllShelvesAreFullException extends Exception
{
    public AllShelvesAreFullException(String message)
    {
        super(message);
    }
}
