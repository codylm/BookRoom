package bookroom;

import static org.junit.Assert.*;

import org.junit.*;

public class BookroomTest
{
    
    private Bookshelf bookshelf;
    
    @Before
    public void setUp()
    {
        Book testBook = new Book("Blah", "Blah", "Blah", "Blah", "Blah", 1.99f);
        bookshelf = new Bookshelf(4, "Fiction");
        bookshelf.addBook(testBook, 0);
        bookshelf.addBook(testBook, 0);
        bookshelf.addBook(testBook, 0);
        bookshelf.addBook(testBook, 0);
        bookshelf.addBook(testBook, 0);
    }
    
    @Test
    public void testGetGrossRevenueFromBookshelf()
    {
        double revenue = bookshelf.getGrossRevenue();
        assertEquals(9.95, revenue, 0.0);
    }

}
