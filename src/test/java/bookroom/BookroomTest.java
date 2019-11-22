package bookroom;

import static org.junit.Assert.*;

import org.hamcrest.CoreMatchers;
import org.junit.*;
import java.util.*;

public class BookroomTest
{
    
    private Bookshelf bookshelf;
    
    @Before
    public void setUp() throws AllShelvesAreFullException, InvalidNumberException
    {
        Book testBook = new Book("Blah", "Blah", "Fiction", "Paperback", "Blah", 1.99);
        bookshelf = new Bookshelf(4, "Fiction", Criteria.GENRE);
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
    
    @Test
    public void testFindBookFromBookshelf() throws BookDoesNotExistException, AllShelvesAreFullException, InvalidNumberException
    {
        Book specificBook = new Book("Specifics", "Stabspeare", "Romance", "Paperback", "1-1-11", 1.99);
        bookshelf.addBook(specificBook, 0);
        Book book = bookshelf.findFirstInstanceOfBook("Specifics", "Stabspeare");
        assertEquals(specificBook, book);
    }
    
    @Test
    public void testBookNotFoundOnBookshelf()
    {
        try
        {
            Book book = bookshelf.findFirstInstanceOfBook("Specifics", "Stabspeare");
            fail("Expected a BookDoesNotExistException");
        }
        catch(BookDoesNotExistException e)
        {
            assertEquals("The desired book is not on this bookshelf.", e.getMessage());
        }
    }
    
    @Test
    public void testBookPlacedOnNextEmptyShelf() throws BookDoesNotExistException, AllShelvesAreFullException, InvalidNumberException
    {
        Book specificBook = new Book("Specifics", "Stabspeare", "Romance", "Paperback", "1-1-11", 1.99);
        bookshelf.toggleShelfFull(0);
        bookshelf.addBook(specificBook, 0);
        assertEquals(1, bookshelf.getShelfOfFirstBookInstance("Specifics", "Stabspeare"));
    }
    
    //honestly this test's a bit crap because it's doing nearly the same thing as the previous two tests
    //just to check for a slightly different result
    @Test
    public void testBookNotFoundInShelfSearch()
    {
        try
        {
            int shelf = bookshelf.getShelfOfFirstBookInstance("Specifics", "Stabspeare");
            fail("Expected a BookDoesNotExistException");
        }
        catch(BookDoesNotExistException e)
        {
            assertEquals("The desired book is not on this bookshelf.", e.getMessage());
        }
    }
    
    @Test
    public void testAllShelvesAreFull() throws InvalidNumberException
    {
        
        Book specificBook = new Book("Specifics", "Stabspeare", "Romance", "Paperback", "1-1-11", 1.99);
        for(int i = 0; i < bookshelf.getNumOfShelves(); i++)
        {
            bookshelf.toggleShelfFull(i);
        }
        
        try
        {
            bookshelf.addBook(specificBook, 0);
            fail("Expected AllShelvesAreFullException.");
        }
        catch(AllShelvesAreFullException e)
        {
            assertEquals("All shelves are full.", e.getMessage());
        }
    }
    
    @Test
    public void testInvalidNumOfShelvesThrowsException()
    {
        try
        {
            bookshelf.setNumOfShelves(-1);
            fail("Expected InvalidNumberException.");
        }
        catch(InvalidNumberException e)
        {
            assertEquals("Number of shelves cannot be a negative number.", e.getMessage());
        }
    }
    
    @Test
    public void testGettingInvalidShelfNumberThrowsException()
    {
        try
        {
            bookshelf.getNumBooksOnShelf(-1);
            fail("Expected InvalidNumberException.");
        }
        catch(InvalidNumberException e)
        {
            assertEquals("Shelf number cannot be negative or greater than the total number of shelves.", e.getMessage());
        }
    }
    
    @Test
    public void testInvalidGetShelfIsFullNumberThrowsException()
    {
        try
        {
            boolean isFull = bookshelf.getShelfIsFull(-1);
            fail("Expected InvalidNumberException.");
        }
        catch(InvalidNumberException e)
        {
            assertEquals("Shelf number cannot be negative or greater than the total number of shelves.", e.getMessage());
        }
    }
    
    @Test
    public void testFlippingInvalidShelfIsFullNumberThrowsException()
    {
        try
        {
            bookshelf.toggleShelfFull(-1);
            fail("Expected InvalidNumberException.");
        }
        catch(InvalidNumberException e)
        {
            assertEquals("Shelf number cannot be negative or greater than the total number of shelves.", e.getMessage());
        }
    }
    
    @Test
    public void testFindingWrongGenreBooksOnShelf() throws AllShelvesAreFullException, InvalidNumberException
    {
        Book wrongBook = new Book("Blah", "Blah", "Cookbook", "Blah", "Blah", 1.99);
        Book wrongBook2 = new Book("Blah", "Blah", "Textbook", "Blah", "Blah", 1.99);
        Book wrongBook3 = new Book("Blah", "Blah", "Religious", "Blah", "Blah", 1.99);
        Book wrongBook4 = new Book("Blah", "Blah", "Romance", "Blah", "Blah", 1.99);
        bookshelf.addBook(wrongBook, 0);
        bookshelf.addBook(wrongBook2, 1);
        bookshelf.addBook(wrongBook3, 2);
        bookshelf.addBook(wrongBook4, 3);
        
        List<Book> wrongBooks = bookshelf.findAllWrongBooks();
        List<Book> expected = new ArrayList<Book>();
        expected.add(wrongBook);
        expected.add(wrongBook2);
        expected.add(wrongBook3);
        expected.add(wrongBook4);
        
        assertThat(wrongBooks, CoreMatchers.is(expected));
    }
    
    @Test
    public void testFindingWrongPriceBooksOnShelf() throws AllShelvesAreFullException, InvalidNumberException
    {
        bookshelf.setCriteria(Criteria.PRICE);
        bookshelf.setCriteriaType("1.99");
        Book wrongBook = new Book("Blah", "Blah", "Cookbook", "Blah", "Blah", 1.99);
        Book wrongBook2 = new Book("Blah", "Blah", "Textbook", "Blah", "Blah", 2.99);
        Book wrongBook3 = new Book("Blah", "Blah", "Religious", "Blah", "Blah", 3.99);
        Book wrongBook4 = new Book("Blah", "Blah", "Romance", "Blah", "Blah", 4.99);
        bookshelf.addBook(wrongBook, 0);
        bookshelf.addBook(wrongBook2, 1);
        bookshelf.addBook(wrongBook3, 2);
        bookshelf.addBook(wrongBook4, 3);
        
        List<Book> wrongBooks = bookshelf.findAllWrongBooks();
        List<Book> expected = new ArrayList<Book>();
        expected.add(wrongBook2);
        expected.add(wrongBook3);
        expected.add(wrongBook4);
        
        assertThat(wrongBooks, CoreMatchers.is(expected));
    }
    
    @Test
    public void testFindingWrongCoverBooksOnShelf() throws AllShelvesAreFullException, InvalidNumberException
    {
        bookshelf.setCriteria(Criteria.COVER);
        bookshelf.setCriteriaType("Paperback");
        Book wrongBook = new Book("Blah", "Blah", "Cookbook", "Hardback", "Blah", 1.99);
        Book wrongBook2 = new Book("Blah", "Blah", "Textbook", "Paperback", "Blah", 2.99);
        Book wrongBook3 = new Book("Blah", "Blah", "Religious", "Hardback", "Blah", 3.99);
        Book wrongBook4 = new Book("Blah", "Blah", "Romance", "Hardback", "Blah", 4.99);
        bookshelf.addBook(wrongBook, 0);
        bookshelf.addBook(wrongBook2, 1);
        bookshelf.addBook(wrongBook3, 2);
        bookshelf.addBook(wrongBook4, 3);
        
        List<Book> wrongBooks = bookshelf.findAllWrongBooks();
        List<Book> expected = new ArrayList<Book>();
        expected.add(wrongBook);
        expected.add(wrongBook3);
        expected.add(wrongBook4);
        
        assertThat(wrongBooks, CoreMatchers.is(expected));
    }
    
    @Test
    public void testGetCopiesOfBookOnShelf()
    {
        Book book = new Book("Blah", "Blah", "Fiction", "Paperback", "Blah", 1.99);
        assertEquals(5, bookshelf.getNumCopiesOfBook(book));
    }
    
    @Test
    public void testSortingSingleShelfByName() throws InvalidNumberException, AllShelvesAreFullException
    {
        Book book = new Book("Blah", "Blah", "Fiction", "Paperback", "Blah", 1.99);
        Book book2 = new Book("Antimony", "Blah", "Fiction", "Paperback", "Blah", 1.99);
        Book book3 = new Book("Zenith", "Blah", "Fiction", "Paperback", "Blah", 1.99);
        Book book4 = new Book("Camp", "Blah", "Fiction", "Paperback", "Blah", 1.99);
        Book book5 = new Book("Falla", "Blah", "Fiction", "Paperback", "Blah", 1.99);

        bookshelf.addBook(book, 1);
        bookshelf.addBook(book2, 1);
        bookshelf.addBook(book3, 1);
        bookshelf.addBook(book4, 1);
        bookshelf.addBook(book5, 1);
        
        List<Book> expected = new ArrayList<Book>();
        expected.add(book2);
        expected.add(book);
        expected.add(book4);
        expected.add(book5);
        expected.add(book3);
        
        bookshelf.sortSingularShelf(1);
        
        assertThat(bookshelf.getSingularShelf(1), CoreMatchers.is(expected));
    }
}
