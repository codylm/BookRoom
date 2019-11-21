package bookroom;

import java.text.DecimalFormat;
import java.util.*;

public class Bookshelf
{
    private int numOfShelves;
    private String criteriaType; //This works with an enum, using a switch or something to retrieve the right thing from the book
    private Criteria criteria; //maybe reverse these variable names?
    
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
    }
    
    public int getNumOfShelves()
    {
        return numOfShelves;
    }
    
    //!!!consider changing if to < 1 because every bookshelf should have at least 1 shelf
    //!!!needs to take shelf booleans into account when adding or removing shelves (list?)
    //not gonna lie, i don't remember what this second reminder meant by shelf booleans
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
    
    public int getNumBooksOnShelves()
    {
        return numBooksOnShelves;
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
        }
        else
        {
            for(int i = 0; i < shelves.size(); i++)
            {
                if(!getShelfIsFull(i))
                {
                    shelves.get(i).add(book);
                    return;
                }
            }
            throw new AllShelvesAreFullException("All shelves are full.");
        }
    }
    
    //The reasoning being that the odds of two books with the same name and author are incredibly small;
    //need to look into how these things are tracked in real life and overhaul it to that though.
    //it's hard to say what kind of search should be used because there's every chance that the shelf won't
    //be sorted because it's representing an actual shelf
    public Book findFirstInstanceOfBook(String name, String author) throws BookDoesNotExistException
    {
        Book foundBook = null;
        for(int i = 0; i < shelves.size(); i++)
        {
            for(int j = 0; j < shelves.get(i).size(); j++)
            {
                if(shelves.get(i).get(j).getName() == name && shelves.get(i).get(j).getAuthor() == author)
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
    
    public int getShelfOfFirstBookInstance(String name, String author) throws BookDoesNotExistException
    {
        Book foundBook = null;
        int shelf = 0;
        for(int i = 0; i < shelves.size(); i++)
        {
            for(int j = 0; j < shelves.get(i).size(); j++)
            {
                if(shelves.get(i).get(j).getName() == name && shelves.get(i).get(j).getAuthor() == author)
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
    
    //I wanna alter this a bit by putting in an enum that tracks what criteria the shelf
    //is categorized by then returning things that don't fit the criteria (a string for the criteria,
    //maybe, and the enum tells what it should be checking that string against)
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
                    if(shelves.get(i).get(j).getAuthor() != getCriteriaType())
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
                    if(shelves.get(i).get(j).getCover() != getCriteriaType())
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
                    if(shelves.get(i).get(j).getGenre() != getCriteriaType())
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
                    if(shelves.get(i).get(j).getName() != getCriteriaType())
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
                    if(shelves.get(i).get(j).getGenre() != getCriteriaType())
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
    public int getNumCopiesOfBook(Book book)
    {
        int copies = 0;
        for(int i = 0; i < shelves.size(); i++)
        {
            for(int j = 0; j < shelves.get(i).size(); j++)
            {
                if(shelves.get(i).get(j).getName() == book.getName())
                {
                    copies++;
                }
            }
        }
        return copies;
    }
}