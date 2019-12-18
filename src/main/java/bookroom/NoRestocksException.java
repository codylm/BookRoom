package bookroom;

public class NoRestocksException extends Exception
{
    public NoRestocksException(String message)
    {
        super(message);
    }
}
