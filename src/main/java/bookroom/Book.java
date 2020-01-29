package bookroom;

import java.time.LocalDate;

/**
 * Class that represents meaningful data about a physical book.
 * @author Cody
 * @version 1.0
 * @since 1.0
 */

public class Book implements Comparable<Book>
{
    /**
     * The name of the book.
     */
    private String name;
    /**
     * The author of the book.
     */
    private String author;
    /**
     * The genre of the book.
     */
    private String genre;
    /**
     * The cover of the book.
     */
    private String cover;
    /**
     * The publish date of the book.
     */
    private LocalDate publishDate;
    /**
     * The price of the book.
     */
    private double price;
    //Honestly, this is bad code because the book shouldn't care about how it's
    //sorted, but there's no other way I can think of at the moment to allow easy sorting.
    /**
     * The classification of the book.
     */
    private Criteria criteria;
    /**
     * The identifying number of the book.
     */
    private long isb;
    
    /**
     * Constructor for the Book class.
     * @param name The name of the book
     * @param author The author of the book
     * @param genre The genre of the book
     * @param cover The cover, paperback or hardback, of the book
     * @param publishDate The publish date of the book
     * @param price The price of the book, as set by the retailer
     * @param criteria The criteria used to determine how books are placed on shelves
     * @param isb The ISBN of the book
     */
    public Book(String name, String author, String genre, String cover, LocalDate publishDate, double price, Criteria criteria, long isb)
    {
        this.name = name;
        this.author = author;
        this.genre = genre;
        this.cover = cover;
        this.publishDate = publishDate;
        this.price = price;
        this.criteria = criteria;
        this.isb = isb;
    }
    
    /**
     * Get the name of the book.
     * @return The name
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * Get the author of the book.
     * @return The author
     */
    public String getAuthor()
    {
        return author;
    }
    
    /**
     * Get the genre of the book.
     * @return The genre
     */
    public String getGenre()
    {
        return genre;
    }
    
    /**
     * Get the cover of the book.
     * @return The cover
     */
    public String getCover()
    {
        return cover;
    }
    
    /**
     * Get the publish date of the book.
     * @return The publish date
     */
    public LocalDate getPublishDate()
    {
        return publishDate;
    }
    
    /**
     * Get the price of the book.
     * @return The price
     */
    public double getPrice()
    {
        return price;
    }
    
    /**
     * Set the price of the book.
     * @param price The new price to set the book to.
     */
    public void setPrice(float price)
    {
        this.price = price;
    }
    
    /**
     * Get the criteria of the book.
     * @return The criteria
     */
    public Criteria getCriteria()
    {
        return criteria;
    }
    
    /**
     * Set the criteria of the book.
     * @param criteria the new criteria to set the book to.
     */
    public void setCriteria(Criteria criteria)
    {
        this.criteria = criteria;
    }
    
    /**
     * Get the ISBN of the book.
     * @return The ISBN
     */
    public long getIsb()
    {
        return isb;
    }

    //I'm flying completely blind on this, but we'll see how it works
    //We'll work with sorting by name at the moment, and maybe go from there
    
    //javadocs is being weird here, not generating the param and return values
    public int compareTo(Book o)
    {
        switch(criteria)
        {
            case NAME:
                return getName().compareTo(o.getName());
            case AUTHOR:
                return getAuthor().compareTo(o.getAuthor());
            case COVER:
                return getCover().compareTo(o.getCover());
            case GENRE:
                return getGenre().compareTo(o.getGenre());
            case PRICE:
                return (getPrice() > o.getPrice()) ? (1) : (-1);
            case PUBLISHDATE:
                //I think this won't work properly yet, need to look into proper dating next
                return getPublishDate().compareTo(o.getPublishDate());
            default:
                return 0;
                
        }
    }
}