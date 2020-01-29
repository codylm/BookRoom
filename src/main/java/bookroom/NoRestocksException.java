package bookroom;

//I... don't think this actually gets used anywhere, I'll have to double check.

/**
 * An exception to handle no restocks having occurred in the file.
 * @author Cody
 * @version 1.0
 * @since 1.0
 */
public class NoRestocksException extends Exception
{
    public NoRestocksException(String message)
    {
        super(message);
    }
}
