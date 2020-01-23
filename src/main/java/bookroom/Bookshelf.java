package bookroom;

import java.text.DecimalFormat;
import java.util.*;
import java.time.LocalDate;

public class Bookshelf
{
    private int numOfShelves;
    private String criteriaType; //This works with an enum, using a switch or something to retrieve the right thing from the book
    private Criteria criteria; //maybe reverse these variable names?
    private boolean restock;
    
    private List<ArrayList<Book>> shelves = new ArrayList<ArrayList<Book>>();
    private boolean[] fullShelves; //Not sure about array, if shelves can be variable shouldn't this be too?
    private int numBooksOnShelves = 0;
    
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
    
    public int getNumOfShelves()
    {
        return numOfShelves;
    }
    
    //!!!consider changing if to < 1 because every bookshelf should have at least 1 shelf
    //!!!needs to take shelf booleans into account when adding or removing shelves (list?)
    //not gonna lie, i don't remember what this second reminder meant by shelf booleans
    //this probably needs to be changed to add new shelves, but... idk
    public void setNumOfShelves(int newNum) throws InvalidNumberException
    {
        if(newNum < 0)
        {
            throw new InvalidNumberException("Number of shelves cannot be a negative number.");
        }
        numOfShelves = newNum;
    }
    
    public Criteria getCriteria()
    {
        return criteria;
    }
    
    public void setCriteria(Criteria criteria)
    {
        this.criteria = criteria;
    }
    
    public String getCriteriaType()
    {
        return criteriaType;
    }
    
    public void setCriteriaType(String criteriaType)
    {
        this.criteriaType = criteriaType;
    }
    
    public void setCriteriaType(LocalDate publishDate)
    {
        this.criteriaType = publishDate.toString();
    }
    
    public int getNumBooksOnShelves()
    {
        return numBooksOnShelves;
    }
    
    public ArrayList<Book> getSingularShelf(int shelf)
    {
        return shelves.get(shelf);
    }
    
    public int getNumBooksOnShelf(int shelf) throws InvalidNumberException
    {
        if(shelf < 1 || shelf > numOfShelves)
        {
            throw new InvalidNumberException("Shelf number cannot be negative or greater than the total number of shelves.");
        }
        return shelves.get(shelf).size();
    }

    public boolean getShelfIsFull(int shelf) throws InvalidNumberException
    {
        if(shelf < 0 || shelf > fullShelves.length - 1)
        {
            throw new InvalidNumberException("Shelf number cannot be negative or greater than the total number of shelves.");
        }
        return fullShelves[shelf];
    }
    
    public boolean getRestock()
    {
        return restock;
    }
    
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
    
    //I just remembered that getShelfIsFull throws that exception, which handles things here.
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
    
    //The reasoning being that the odds of two books with the same name and author are incredibly small;
    //need to look into how these things are tracked in real life and overhaul it to that though.
    //it's hard to say what kind of search should be used because there's every chance that the shelf won't
    //be sorted because it's representing an actual shelf
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
    public void sortSingularShelf(int shelf) throws InvalidNumberException
    {
        Collections.sort(getSingularShelf(shelf));
    }
    
    public void sortAllShelves() throws InvalidNumberException
    {
        for(int i = 0; i < shelves.size(); i++)
        {
            sortSingularShelf(i);
            System.out.println("hi");
        }
    }
    
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