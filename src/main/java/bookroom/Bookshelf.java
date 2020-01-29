package bookroom;

import java.text.DecimalFormat;
import java.util.*;
import java.time.LocalDate;

/**
 * Class to represent a bookcase/bookshelf in Java, and provide various functions
 * to work with it.
 * @author Cody
 * @version 1.0
 * @since 1.0
 */

public class Bookshelf
{
    /**
     * The number of individual shelves on the bookcase.
     */
    private int numOfShelves;
    /**
     * The specific criteria by which the bookshelf is classified.
     */
    private String criteriaType; //This works with an enum, using a switch or something to retrieve the right thing from the book
    /**
     * The general category by which the bookshelf is classified.
     */
    private Criteria criteria; //maybe reverse these variable names?
    /**
     * Whether the bookshelf needs to be restocked.
     */
    private boolean restock;
    
    /**
     * The shelves of the bookcase, represented as a List of ArrayLists of Books.
     */
    private List<ArrayList<Book>> shelves = new ArrayList<ArrayList<Book>>();
    /**
     * An array holding the information of whether or not a single shelf on a bookcase is full.
     */
    private boolean[] fullShelves; //Not sure about array, if shelves can be variable shouldn't this be too?
    /**
     * The total number of books on the bookcase.
     */
    private int numBooksOnShelves = 0;
    
    /**
     * Constructor for the Bookshelf class.
     * @param numOfShelves The number of individual shelves on the bookcase.
     * @param criteriaType The specific metric to organize the bookshelf.
     * @param criteria The more general metric to organize the bookshelf.
     */
    public Bookshelf(int numOfShelves, String criteriaType, Criteria criteria)
    {
        this.numOfShelves = numOfShelves;
        this.criteriaType = criteriaType;
        this.criteria = criteria;
        this.fullShelves = new boolean[numOfShelves];
        for(int i = 0; i < numOfShelves; i++)
        {
            shelves.add(new ArrayList<Book>());
        }
        
        restock = false;
    }
    
    /**
     * Returns the number of shelves.
     * @return The number of shelves.
     */
    public int getNumOfShelves()
    {
        return numOfShelves;
    }
    
    //!!!consider changing if to < 1 because every bookshelf should have at least 1 shelf
    //!!!needs to take shelf booleans into account when adding or removing shelves (list?)
    //not gonna lie, i don't remember what this second reminder meant by shelf booleans
    //this probably needs to be changed to add new shelves, but... idk
    /**
     * Sets the number of individual shelves.
     * @param newNum The new number of shelves.
     * @throws InvalidNumberException If the number passed in is less than 0.
     */
    public void setNumOfShelves(int newNum) throws InvalidNumberException
    {
        if(newNum < 0)
        {
            throw new InvalidNumberException("Number of shelves cannot be a negative number.");
        }
        numOfShelves = newNum;
    }
    
    /**
     * Returns the criteria for the bookcase.
     * @return The criteria.
     */
    public Criteria getCriteria()
    {
        return criteria;
    }
    
    /**
     * Sets the new criteria for the bookcase.
     * @param criteria The new criteria.
     */
    public void setCriteria(Criteria criteria)
    {
        this.criteria = criteria;
    }
    
    /**
     * Returns the criteria type for the bookcase.
     * @return The criteria type.
     */
    public String getCriteriaType()
    {
        return criteriaType;
    }
    
    /**
     * Sets the new criteria type for the bookcase.
     * @param criteriaType The new criteria type.
     */
    public void setCriteriaType(String criteriaType)
    {
        this.criteriaType = criteriaType;
    }
    
    /**
     * Specifically sets the criteria type in situations where the criteria is PUBLISHDATE.
     * @param publishDate The new criteria type.
     */
    public void setCriteriaType(LocalDate publishDate)
    {
        this.criteriaType = publishDate.toString();
    }
    
    /**
     * Returns the number of books on the bookcase as a whole.
     * @return The number of books.
     */
    public int getNumBooksOnShelves()
    {
        return numBooksOnShelves;
    }
    
    /**
     * Returns the contents of a single shelf on the bookcase.
     * @param shelf The index of the single shelf.
     * @return The single shelf as an ArrayList.
     */
    public ArrayList<Book> getSingularShelf(int shelf)
    {
        return shelves.get(shelf);
    }
    
    /**
     * Returns the number of books on a singular shelf.
     * @param shelf The index of the shelf.
     * @return The number of books.
     * @throws InvalidNumberException If the index provided is out of range of the number of shelves.
     */
    public int getNumBooksOnShelf(int shelf) throws InvalidNumberException
    {
        if(shelf < 1 || shelf > numOfShelves)
        {
            throw new InvalidNumberException("Shelf number cannot be negative or greater than the total number of shelves.");
        }
        return shelves.get(shelf).size();
    }

    /**
     * Returns whether the given shelf is marked as full.
     * @param shelf The index of the shelf.
     * @return Whether the shelf is full.
     * @throws InvalidNumberException If the index provided is out of range of the number of shelves.
     */
    public boolean getShelfIsFull(int shelf) throws InvalidNumberException
    {
        if(shelf < 0 || shelf > fullShelves.length - 1)
        {
            throw new InvalidNumberException("Shelf number cannot be negative or greater than the total number of shelves.");
        }
        return fullShelves[shelf];
    }
    
    /**
     * Returns whether or not the bookcase needs to be restocked.
     * @return Whether or not the bookcase needs to be restocked.
     */
    public boolean getRestock()
    {
        return restock;
    }
    
    /**
     * Sets whether the bookcase needs to be restocked.
     */
    public void setRestock()
    {
        if(restock == true)
        {
            restock = false;
        }
        else
        {
            restock = true;
        }
    }
    
    /**
     * Toggles whether the given single shelf is full.
     * @param shelf The index of the shelf.
     * @throws InvalidNumberException If the index provided is out of range of the number of shelves.
     */
    public void toggleShelfFull(int shelf) throws InvalidNumberException
    {
        if(getShelfIsFull(shelf))
        {
            fullShelves[shelf] = false;
        }
        else
        {
            fullShelves[shelf] = true;
        }
    }
    
    /**
     * Returns the potential gross revenue of the books on the shelf.
     * @return The potential gross revenue of the books on the shelf.
     */
    public double getGrossRevenue()
    {
        double revenue = 0.0;
        for(int i = 0; i < shelves.size(); i++)
        {
            for(int j = 0; j < shelves.get(i).size(); j++)
            {
                revenue += shelves.get(i).get(j).getPrice();
            }
        }
        DecimalFormat f = new DecimalFormat("##.##");
        return Double.valueOf(f.format(revenue));
    }
    
    /**
     * Adds the given book to the shelf specified by the index.
     * @param book The book to be added.
     * @param shelf The index of the shelf to add the book to.
     * @throws AllShelvesAreFullException If there isn't a valid shelf to place the book on.
     * @throws InvalidNumberException If the index provided is out of range of the number of shelves.
     */
    public void addBook(Book book, int shelf) throws AllShelvesAreFullException, InvalidNumberException
    {
        if(!getShelfIsFull(shelf))
        {
            shelves.get(shelf).add(book);
            numBooksOnShelves++;
        }
        else
        {
            for(int i = 0; i < shelves.size(); i++)
            {
                if(!getShelfIsFull(i))
                {
                    shelves.get(i).add(book);
                    numBooksOnShelves++;
                    return;
                }
            }
            throw new AllShelvesAreFullException("All shelves are full.");
        }
    }
    
    //probably need to refactor this to be less crappy at some point,
    //considering it's kinda circular..
    //!!!!
    /**
     * Removes the book specified by the ISBN provided.
     * @param isb The identifying number of the book to remove.
     * @return The removed book.
     * @throws BookDoesNotExistException If the provided ISBN doesn't correspond to any book on the bookcase.
     */
    public Book removeBook(long isb) throws BookDoesNotExistException
    {
        Book foundBook = null;
        for(int i = 0; i < shelves.size(); i++)
        {
            for(int j = 0; j < shelves.get(i).size(); j++)
            {
                if(shelves.get(i).get(j).getIsb() == isb)
                {
                    foundBook = shelves.get(i).get(j);
                    shelves.get(i).remove(foundBook);
                    numBooksOnShelves--;
                    break;
                }
            }
        }
        if(foundBook == null)
        {
            throw new BookDoesNotExistException("The desired book is not on this bookshelf.");
        }
        return foundBook;
    }
    
    //it's hard to say what kind of search should be used because there's every chance that the shelf won't
    //be sorted because it's representing an actual shelf
    /**
     * Returns the first instance of the book identified by the provided ISBN.
     * @param isb The identifying number of the book to be found.
     * @return The found book.
     * @throws BookDoesNotExistException If the provided ISBN doesn't correspond to any book on the bookcase.
     */
    public Book findFirstInstanceOfBook(long isb) throws BookDoesNotExistException
    {
        Book foundBook = null;
        for(int i = 0; i < shelves.size(); i++)
        {
            for(int j = 0; j < shelves.get(i).size(); j++)
            {
                if(shelves.get(i).get(j).getIsb() == isb)
                {
                    foundBook = shelves.get(i).get(j);
                    break;
                }
            }
        }
        if(foundBook == null)
        {
            throw new BookDoesNotExistException("The desired book is not on this bookshelf.");
        }
        return foundBook;
    }
    
    /**
     * Returns the index of the shelf containing the first instance of the book corresponding to the provided ISBN.
     * @param isb The identifying number of the book.
     * @return The index of the shelf containing the book.
     * @throws BookDoesNotExistException If the provided ISBN doesn't correspond to any book on the bookcase.
     */
    public int getShelfOfFirstBookInstance(long isb) throws BookDoesNotExistException
    {
        Book foundBook = null;
        int shelf = 0;
        for(int i = 0; i < shelves.size(); i++)
        {
            for(int j = 0; j < shelves.get(i).size(); j++)
            {
                if(shelves.get(i).get(j).getIsb() == isb)
                {
                    foundBook = shelves.get(i).get(j);
                    shelf = i;
                    break;
                }
            }
        }
        if(foundBook == null)
        {
            throw new BookDoesNotExistException("The desired book is not on this bookshelf.");
        }
        return shelf;
    }
    
    /**
     * Returns a list of all books on the bookcase that do not match the bookcase's criteria type.
     * @return A list of all books on the bookcase that don't match.
     */
    public List<Book> findAllWrongBooks()
    {
        List<Book> wrongBooks = new ArrayList<Book>();
        switch(criteria)
        {
        case AUTHOR:
            for(int i = 0; i < shelves.size(); i++)
            {
                for(int j = 0; j < shelves.get(i).size(); j++)
                {
                    if(!shelves.get(i).get(j).getAuthor().contentEquals(getCriteriaType()))
                    {
                        wrongBooks.add(shelves.get(i).get(j));
                    }
                }
            }
            break;
        case COVER:
            for(int i = 0; i < shelves.size(); i++)
            {
                for(int j = 0; j < shelves.get(i).size(); j++)
                {
                    if(!shelves.get(i).get(j).getCover().contentEquals(getCriteriaType()))
                    {
                        wrongBooks.add(shelves.get(i).get(j));
                    }
                }
            }
            break;
        case GENRE:
            for(int i = 0; i < shelves.size(); i++)
            {
                for(int j = 0; j < shelves.get(i).size(); j++)
                {
                    if(!shelves.get(i).get(j).getGenre().contentEquals(getCriteriaType()))
                    {
                        wrongBooks.add(shelves.get(i).get(j));
                    }
                }
            }
            break;
        case NAME:
            for(int i = 0; i < shelves.size(); i++)
            {
                for(int j = 0; j < shelves.get(i).size(); j++)
                {
                    if(!shelves.get(i).get(j).getName().contentEquals(getCriteriaType()))
                    {
                        wrongBooks.add(shelves.get(i).get(j));
                    }
                }
            }
            break;
        case PRICE:
            for(int i = 0; i < shelves.size(); i++)
            {
                for(int j = 0; j < shelves.get(i).size(); j++)
                {
                    if(shelves.get(i).get(j).getPrice() != Double.parseDouble(getCriteriaType()))
                    {
                        wrongBooks.add(shelves.get(i).get(j));
                    }
                }
            }
            break;
        case PUBLISHDATE:
            for(int i = 0; i < shelves.size(); i++)
            {
                for(int j = 0; j < shelves.get(i).size(); j++)
                {
                    if(!shelves.get(i).get(j).getPublishDate().equals(LocalDate.parse(getCriteriaType())))
                    {
                        wrongBooks.add(shelves.get(i).get(j));
                    }
                }
            }
            break;
        default:
            break;
            
        }
        return wrongBooks;
    }
    
    //feel like I might need to put an isEqual function in book that checks all the
    //primitive/immutable stuff to fully check that they're the same, but this should
    //work for basic functionality
    //!!!!
    /**
     * Returns the number of copies of the book corresponding to the provided ISBN on the bookcase.
     * @param isb The identifying number of the book.
     * @return The number of copies of the book.
     */
    public int getNumCopiesOfBook(long isb)
    {
        int copies = 0;
        for(int i = 0; i < shelves.size(); i++)
        {
            for(int j = 0; j < shelves.get(i).size(); j++)
            {
                if(shelves.get(i).get(j).getIsb() == isb)
                {
                    copies++;
                }
            }
        }
        return copies;
    }
    
    //Sorts by name right now, maybe work on that more later
    /**
     * Sorts the single shelf corresponding to the provided index.
     * @param shelf The index of the shelf to sort.
     * @throws InvalidNumberException If the index provided is out of range of the number of shelves.
     */
    public void sortSingularShelf(int shelf) throws InvalidNumberException
    {
        Collections.sort(getSingularShelf(shelf));
    }
    
    /**
     * Sorts all shelves on the bookcase.
     * @throws InvalidNumberException Neccessary because of how the function works, this function shouldn't actually throw this.
     */
    public void sortAllShelves() throws InvalidNumberException
    {
        for(int i = 0; i < shelves.size(); i++)
        {
            sortSingularShelf(i);
            System.out.println("hi");
        }
    }
    
    /**
     * Returns the position of the given book on the shelf.
     * @param book The book to find the position of.
     * @return The position of the book on the shelf as an index.
     */
    public int findPositionOfBook(Book book)
    {
        for(int i = 0; i < shelves.size(); i++)
        {
            if(shelves.get(i).contains(book))
            {
                return i; //Could possibly use indexOf here, but...
            }
        }
        return -1;
    }
}