package bookroom;

public class BookDoesNotExistException extends Exception
{
    public BookDoesNotExistException(String message)
    {
        super(message);
    }
}