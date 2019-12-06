package bookroom;

import static org.junit.Assert.*;

import org.hamcrest.CoreMatchers;
import org.junit.*;
import java.util.*;
import java.time.LocalDate;

public class BookroomTest
{
    
    private Bookshelf bookshelf;
    private Bookroom bookroom;
    private Book testBook;
    
    @Before
    public void setUp() throws AllShelvesAreFullException, InvalidNumberException
    {
        testBook = new Book("Blah", "Blah", "Fiction", "Paperback", LocalDate.of(1111, 1, 1), 1.99, Criteria.NAME, 1234567890);
        bookshelf = new Bookshelf(4, "Fiction", Criteria.GENRE);
        bookroom = new Bookroom(0.4);
        
        bookshelf.addBook(testBook, 0);
        bookshelf.addBook(testBook, 0);
        bookshelf.addBook(testBook, 0);
        bookshelf.addBook(testBook, 0);
        bookshelf.addBook(testBook, 0);
        
        bookroom.addShelf(0, bookshelf);
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
        Book specificBook = new Book("Specifics", "Stabspeare", "Romance", "Paperback", LocalDate.of(1111, 1, 1), 1.99, Criteria.NAME, 1023456789);
        bookshelf.addBook(specificBook, 0);
        Book book = bookshelf.findFirstInstanceOfBook(1023456789);
        assertEquals(specificBook, book);
    }
    
    @Test
    public void testBookNotFoundOnBookshelf()
    {
        try
        {
            Book book = bookshelf.findFirstInstanceOfBook(1023456789);
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
        Book specificBook = new Book("Specifics", "Stabspeare", "Romance", "Paperback", LocalDate.of(1111, 1, 1), 1.99, Criteria.NAME, 1023456789);
        bookshelf.toggleShelfFull(0);
        bookshelf.addBook(specificBook, 0);
        assertEquals(1, bookshelf.getShelfOfFirstBookInstance(1023456789));
    }
    
    //honestly this test's a bit crap because it's doing nearly the same thing as the previous two tests
    //just to check for a slightly different result
    @Test
    public void testBookNotFoundInShelfSearch()
    {
        try
        {
            int shelf = bookshelf.getShelfOfFirstBookInstance(1023456789);
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
        
        Book specificBook = new Book("Specifics", "Stabspeare", "Romance", "Paperback", LocalDate.of(1111, 1, 1), 1.99, Criteria.NAME, 1023456789);
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
        Book wrongBook = new Book("Blah", "Blah", "Cookbook", "Blah", LocalDate.of(1111, 1, 1), 1.99, Criteria.NAME, 1023456789);
        Book wrongBook2 = new Book("Blah", "Blah", "Textbook", "Blah", LocalDate.of(1111, 1, 1), 1.99, Criteria.NAME, 1203456789);
        Book wrongBook3 = new Book("Blah", "Blah", "Religious", "Blah", LocalDate.of(1111, 1, 1), 1.99, Criteria.NAME, 1230456789);
        Book wrongBook4 = new Book("Blah", "Blah", "Romance", "Blah", LocalDate.of(1111, 1, 1), 1.99, Criteria.NAME, 1234056789);
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
        Book wrongBook = new Book("Blah", "Blah", "Cookbook", "Blah", LocalDate.of(1111, 1, 1), 1.99, Criteria.NAME, 1023456789);
        Book wrongBook2 = new Book("Blah", "Blah", "Textbook", "Blah", LocalDate.of(1111, 1, 1), 2.99, Criteria.NAME, 1203456789);
        Book wrongBook3 = new Book("Blah", "Blah", "Religious", "Blah", LocalDate.of(1111, 1, 1), 3.99, Criteria.NAME, 1230456789);
        Book wrongBook4 = new Book("Blah", "Blah", "Romance", "Blah", LocalDate.of(1111, 1, 1), 4.99, Criteria.NAME, 1234056789);
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
        Book wrongBook = new Book("Blah", "Blah", "Cookbook", "Hardback", LocalDate.of(1111, 1, 1), 1.99, Criteria.NAME, 1023456789);
        Book wrongBook2 = new Book("Blah", "Blah", "Textbook", "Paperback", LocalDate.of(1111, 1, 1), 2.99, Criteria.NAME, 1203456789);
        Book wrongBook3 = new Book("Blah", "Blah", "Religious", "Hardback", LocalDate.of(1111, 1, 1), 3.99, Criteria.NAME, 1230456789);
        Book wrongBook4 = new Book("Blah", "Blah", "Romance", "Hardback", LocalDate.of(1111, 1, 1), 4.99, Criteria.NAME, 1234056789);
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
    public void testFindingWrongAuthorBooksOnShelf() throws AllShelvesAreFullException, InvalidNumberException
    {
        bookshelf.setCriteria(Criteria.AUTHOR);
        bookshelf.setCriteriaType("Blah");
        Book wrongBook = new Book("Blah", "Blah", "Cookbook", "Hardback", LocalDate.of(1111, 1, 1), 1.99, Criteria.AUTHOR, 1023456789);
        Book wrongBook2 = new Book("Blah", "Shakespeare", "Textbook", "Paperback", LocalDate.of(1111, 1, 1), 2.99, Criteria.AUTHOR, 1203456789);
        Book wrongBook3 = new Book("Blah", "Bradbury", "Religious", "Hardback", LocalDate.of(1111, 1, 1), 3.99, Criteria.AUTHOR, 1230456789);
        Book wrongBook4 = new Book("Blah", "Huxley", "Romance", "Hardback", LocalDate.of(1111, 1, 1), 4.99, Criteria.AUTHOR, 1234056789);
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
    public void testFindingWrongDateBooksOnShelf() throws AllShelvesAreFullException, InvalidNumberException
    {
        bookshelf.setCriteria(Criteria.PUBLISHDATE);
        bookshelf.setCriteriaType(LocalDate.of(1111, 1, 1));
        Book wrongBook = new Book("Blah", "Blah", "Cookbook", "Hardback", LocalDate.of(1111, 1, 1), 1.99, Criteria.PUBLISHDATE, 1023456789);
        Book wrongBook2 = new Book("Worm", "Shakespeare", "Textbook", "Paperback", LocalDate.of(1111, 2, 1), 2.99, Criteria.PUBLISHDATE, 1203456789);
        Book wrongBook3 = new Book("Blah", "Bradbury", "Religious", "Hardback", LocalDate.of(1111, 2, 1), 3.99, Criteria.PUBLISHDATE, 1230456789);
        Book wrongBook4 = new Book("Lord of the Flies", "Huxley", "Romance", "Hardback", LocalDate.of(1111, 1, 1), 4.99, Criteria.PUBLISHDATE, 1234056789);
        bookshelf.addBook(wrongBook, 0);
        bookshelf.addBook(wrongBook2, 1);
        bookshelf.addBook(wrongBook3, 2);
        bookshelf.addBook(wrongBook4, 3);
        
        List<Book> wrongBooks = bookshelf.findAllWrongBooks();
        List<Book> expected = new ArrayList<Book>();
        expected.add(wrongBook2);
        expected.add(wrongBook3);
        
        assertThat(wrongBooks, CoreMatchers.is(expected));
    }
    
    @Test
    public void testGetCopiesOfBookOnShelf()
    {
        Book book = new Book("Blah", "Blah", "Fiction", "Paperback", LocalDate.of(1111, 1, 1), 1.99, Criteria.NAME, 1234567890);
        assertEquals(5, bookshelf.getNumCopiesOfBook(book));
    }
    
    @Test
    public void testSortingSingleShelfByName() throws InvalidNumberException, AllShelvesAreFullException
    {
        Book book = new Book("Blah", "Blah", "Fiction", "Paperback", LocalDate.of(1111, 1, 1), 1.99, Criteria.NAME, 1023456789);
        Book book2 = new Book("Antimony", "Blah", "Fiction", "Paperback", LocalDate.of(1111, 1, 1), 1.99, Criteria.NAME, 1203456789);
        Book book3 = new Book("Zenith", "Blah", "Fiction", "Paperback", LocalDate.of(1111, 1, 1), 1.99, Criteria.NAME, 1230456789);
        Book book4 = new Book("Camp", "Blah", "Fiction", "Paperback", LocalDate.of(1111, 1, 1), 1.99, Criteria.NAME, 1234056789);
        Book book5 = new Book("Falla", "Blah", "Fiction", "Paperback", LocalDate.of(1111, 1, 1), 1.99, Criteria.NAME, 1234506789);

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
    
    @Test
    public void testSortingAllShelvesByName() throws InvalidNumberException, AllShelvesAreFullException
    {
        Book book = new Book("Blah", "Blah", "Fiction", "Paperback", LocalDate.of(1111, 1, 1), 1.99, Criteria.NAME, 1023456789);
        Book book2 = new Book("Antimony", "Blah", "Fiction", "Paperback", LocalDate.of(1111, 1, 1), 1.99, Criteria.NAME, 1203456789);
        Book book3 = new Book("Zenith", "Blah", "Fiction", "Paperback", LocalDate.of(1111, 1, 1), 1.99, Criteria.NAME, 1230456789);
        Book book4 = new Book("Camp", "Blah", "Fiction", "Paperback", LocalDate.of(1111, 1, 1), 1.99, Criteria.NAME, 1234056789);
        Book book5 = new Book("Falla", "Blah", "Fiction", "Paperback", LocalDate.of(1111, 1, 1), 1.99, Criteria.NAME, 1234506789);

        bookshelf.addBook(book, 1);
        bookshelf.addBook(book2, 1);
        bookshelf.addBook(book3, 1);
        bookshelf.addBook(book4, 1);
        bookshelf.addBook(book5, 1);
        
        bookshelf.addBook(book5, 2);
        bookshelf.addBook(book3, 2);
        bookshelf.addBook(book2, 2);
        bookshelf.addBook(book4, 2);
        bookshelf.addBook(book, 2);
        
        List<Book> expected = new ArrayList<Book>();
        expected.add(book2);
        expected.add(book);
        expected.add(book4);
        expected.add(book5);
        expected.add(book3);
        
        bookshelf.sortAllShelves();
        
        assertThat(bookshelf.getSingularShelf(1), CoreMatchers.is(expected));
        assertThat(bookshelf.getSingularShelf(2), CoreMatchers.is(expected));
    }
    
    @Test
    public void testSortingSingularShelfByAuthor() throws InvalidNumberException, AllShelvesAreFullException
    {
        Book book = new Book("Blah", "Blah", "Fiction", "Paperback", LocalDate.of(1111, 1, 1), 1.99, Criteria.AUTHOR, 1023456789);
        Book book2 = new Book("Antimony", "Fallon", "Fiction", "Paperback", LocalDate.of(1111, 1, 1), 1.99, Criteria.AUTHOR, 1203456789);
        Book book3 = new Book("Zenith", "Carver", "Fiction", "Paperback", LocalDate.of(1111, 1, 1), 1.99, Criteria.AUTHOR, 1230456789);
        Book book4 = new Book("Camp", "Zimo", "Fiction", "Paperback", LocalDate.of(1111, 1, 1), 1.99, Criteria.AUTHOR, 1234056789);
        Book book5 = new Book("Falla", "Dallon", "Fiction", "Paperback", LocalDate.of(1111, 1, 1), 1.99, Criteria.AUTHOR, 1234506789);

        bookshelf.addBook(book, 1);
        bookshelf.addBook(book2, 1);
        bookshelf.addBook(book3, 1);
        bookshelf.addBook(book4, 1);
        bookshelf.addBook(book5, 1);
        
        List<Book> expected = new ArrayList<Book>();
        expected.add(book);
        expected.add(book3);
        expected.add(book5);
        expected.add(book2);
        expected.add(book4);
        
        bookshelf.sortSingularShelf(1);
        
        assertThat(bookshelf.getSingularShelf(1), CoreMatchers.is(expected));
    }
    
    @Test
    public void testSortingSingularShelfByPrice() throws InvalidNumberException, AllShelvesAreFullException
    {
        Book book = new Book("Blah", "Blah", "Fiction", "Paperback", LocalDate.of(1111, 1, 1), 4.99, Criteria.PRICE, 1023456789);
        Book book2 = new Book("Antimony", "Fallon", "Fiction", "Paperback", LocalDate.of(1111, 1, 1), 1.99, Criteria.PRICE, 1203456789);
        Book book3 = new Book("Zenith", "Carver", "Fiction", "Paperback", LocalDate.of(1111, 1, 1), 6.99, Criteria.PRICE, 1230456789);
        Book book4 = new Book("Camp", "Zimo", "Fiction", "Paperback", LocalDate.of(1111, 1, 1), 5.99, Criteria.PRICE, 1234056789);
        Book book5 = new Book("Falla", "Dallon", "Fiction", "Paperback", LocalDate.of(1111, 1, 1), 2.99, Criteria.PRICE, 1234506789);

        bookshelf.addBook(book, 1);
        bookshelf.addBook(book2, 1);
        bookshelf.addBook(book3, 1);
        bookshelf.addBook(book4, 1);
        bookshelf.addBook(book5, 1);
        
        List<Book> expected = new ArrayList<Book>();
        expected.add(book2);
        expected.add(book5);
        expected.add(book);
        expected.add(book4);
        expected.add(book3);

        bookshelf.sortSingularShelf(1);
        
        assertThat(bookshelf.getSingularShelf(1), CoreMatchers.is(expected));
    }
    
    @Test
    public void testGettingGrossRevenueOfBookroom()
    {
        bookroom.addShelf(1, bookshelf);
        double revenue = bookroom.getGrossRevenue();
        assertEquals(19.90, revenue, 0.0);
    }
    
    @Test
    public void testGetAllShelfCriteria()
    {
        List<Criteria> expected = new ArrayList<Criteria>();
        expected.add(Criteria.GENRE);
        expected.add(Criteria.AUTHOR);
        
        Bookshelf bookshelf2 = new Bookshelf(4, "Fiction", Criteria.AUTHOR);
        bookroom.addShelf(1, bookshelf2);
        
        List<Criteria> actual = bookroom.getAllShelfCriteria();
        
        
        assertThat(actual, CoreMatchers.is(expected));
    }

    @Test
    public void testGetAllShelfCriteriaTypes()
    {
        List<String> expected = new ArrayList<String>();
        expected.add("Fiction");
        expected.add("Shakespeare");
        
        Bookshelf bookshelf2 = new Bookshelf(4, "Shakespeare", Criteria.AUTHOR);
        bookroom.addShelf(1, bookshelf2);
        
        List<String> actual = bookroom.getAllShelfCriteriaTypes();
        
        
        assertThat(actual, CoreMatchers.is(expected));
    }
    
    
    @Test
    public void testFindFirstShelfOfBook() throws AllShelvesAreFullException, InvalidNumberException
    {
        long isbn = 1023456789;
        Book book = new Book("Blah", "Blah", "Fiction", "Paperback", LocalDate.of(1111, 1, 1), 4.99, Criteria.PRICE, 1023456789);
        Bookshelf newShelf = new Bookshelf(4, "Fiction", Criteria.GENRE);
        newShelf.addBook(book, 0);
        bookroom.addShelf(1, newShelf);
        
        
        Bookshelf firstShelf = bookroom.findFirstShelfOfBook(isbn);
        
        assertEquals(newShelf, firstShelf);
    }
    
    @Test
    public void testGetNumCopiesOfBook() throws AllShelvesAreFullException, InvalidNumberException
    {
        Bookshelf bookshelf2 = new Bookshelf(4, "Fiction", Criteria.GENRE);
        bookroom.addShelf(1, bookshelf2);
        bookshelf2.addBook(testBook, 2);
        bookshelf2.addBook(testBook, 3);
        bookshelf2.addBook(testBook, 1);
        int copies = bookroom.getNumCopiesOfBook(testBook);
        assertEquals(8, copies);
    }
    
    @Test
    public void testBookroomNeedsRestocking()
    {
        boolean needsRestocking = false;
        Bookshelf bookshelf2 = new Bookshelf(4, "Fiction", Criteria.GENRE);
        Bookshelf bookshelf3 = new Bookshelf(4, "Fiction", Criteria.GENRE);
        Bookshelf bookshelf4 = new Bookshelf(4, "Fiction", Criteria.GENRE);
        Bookshelf bookshelf5 = new Bookshelf(4, "Fiction", Criteria.GENRE);
        bookroom.addShelf(1, bookshelf2);
        bookroom.addShelf(2, bookshelf3);
        bookroom.addShelf(3, bookshelf4);
        bookroom.addShelf(4, bookshelf5);
        bookroom.getShelf(0).setRestock();
        bookroom.getShelf(1).setRestock();
        
        needsRestocking = bookroom.checkForRestock();
        assertTrue(needsRestocking);
    }
    
    @Test
    public void testSignalRestock()
    {
        boolean needsRestocking = true;
        Bookshelf bookshelf2 = new Bookshelf(4, "Fiction", Criteria.GENRE);
        Bookshelf bookshelf3 = new Bookshelf(4, "Fiction", Criteria.GENRE);
        Bookshelf bookshelf4 = new Bookshelf(4, "Fiction", Criteria.GENRE);
        Bookshelf bookshelf5 = new Bookshelf(4, "Fiction", Criteria.GENRE);
        bookroom.addShelf(1, bookshelf2);
        bookroom.addShelf(2, bookshelf3);
        bookroom.addShelf(3, bookshelf4);
        bookroom.addShelf(4, bookshelf5);
        bookroom.getShelf(0).setRestock();
        bookroom.getShelf(1).setRestock();
        
        bookroom.signalRestock("Carrie");
        needsRestocking = bookroom.checkForRestock();
        
        assertFalse(needsRestocking);
    }
}
