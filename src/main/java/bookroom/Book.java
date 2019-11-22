package bookroom;

public class Book implements Comparable<Book>
{
    private String name, author;
    private String genre; //enum for genre?
    private String cover; //enum for cover?
    private String publishDate; //should see about converting this to some kind of date object if java has that
    private double price;
    
    public Book(String name, String author, String genre, String cover, String publishDate, double price)
    {
        this.name = name;
        this.author = author;
        this.genre = genre;
        this.cover = cover;
        this.publishDate = publishDate;
        this.price = price;
    }
    
    //Do I even need this base case?
    public Book()
    {
        this.name = "";
        this.author = "";
        this.genre = "";
        this.cover = "";
        this.publishDate = "";
        this.price = 0.0;
    }
    public String getName()
    {
        return name;
    }
    
    public String getAuthor()
    {
        return author;
    }
    
    public String getGenre()
    {
        return genre;
    }
    
    public String getCover()
    {
        return cover;
    }
    
    public String getPublishDate()
    {
        return publishDate;
    }
    
    public double getPrice()
    {
        return price;
    }
    
    public void setPrice(float price)
    {
        this.price = price;
    }

    //I'm flying completely blind on this, but we'll see how it works
    //We'll work with sorting by name at the moment, and maybe go from there
    public int compareTo(Book o)
    {
        // TODO Auto-generated method stub
        return getName().compareTo(o.getName());
    }
}
