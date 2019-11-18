package bookroom;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Bookshelf
{
    private int numOfShelves;
    private String genre; //This could be an enum, but genre is kind of a fluid thing
    
    private ArrayList<ArrayList<Book>> shelves = new ArrayList<ArrayList<Book>>();
    private boolean[] fullShelves = new boolean[numOfShelves];
    private int totalBooksOnShelves = 0;
    
    public Bookshelf(int numOfShelves, String genre)
    {
        this.numOfShelves = numOfShelves;
        this.genre = genre;
        for(int i = 0; i < numOfShelves; i++)
        {
            shelves.add(new ArrayList<Book>());
        }
    }
    
    public int getNumOfShelves()
    {
        return numOfShelves;
    }
    
    public void setNumOfShelves(int newNum)
    {
        numOfShelves = newNum;
    }
    
    public String getGenre()
    {
        return genre;
    }
    
    public int getNumBooksOnShelves()
    {
        return totalBooksOnShelves;
    }
    
    //needs to check for a shelf number that's negative or otherwise out of range
    public int getBooksOnShelf(int shelf)
    {
        return shelves.get(shelf).size();
    }

    //needs to check for a shelf number that's negative or otherwise out of range
    public boolean getIfShelfIsFull(int shelf)
    {
        return fullShelves[shelf];
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
    
    //gonna need handling for adding books to a full shelf or a full bookshelf
    public void addBook(Book book, int shelf)
    {
        shelves.get(shelf).add(book);
    }
}