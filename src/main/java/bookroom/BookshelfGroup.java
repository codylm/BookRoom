package bookroom;

import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

/**
 * Class to represent a group of bookcases, particularly in retail settings.
 * @author Cody
 * @version 1.0
 * @since 1.0
 */

public class BookshelfGroup
{
    /**
     * The collection of bookshelves as a map.
     */
    private Map<Integer, Bookshelf> shelves;
    /**
     * The threshold as a percentage for restocking the group as a decimal.
     */
    private double percentRestock;
    /**
     * How much revenue has been earned since last restock.
     */
    private double cycleRevenue;
    /**
     * Whether the group needs restocking or not.
     */
    private boolean needsRestock;
    
    /**
     * Constructor for BookshelfGroup.
     * @param percentRestock The default percentage at which the group is marked for restock as a decimal.
     */
    public BookshelfGroup(double percentRestock)
    {
        shelves = new HashMap<Integer, Bookshelf>();
        this.percentRestock = percentRestock;
    }
    
    /**
     * Adds a new bookshelf to the group.
     * @param key The identifier of the bookshelf.
     * @param shelf The bookshelf to be added.
     */
    public void addShelf(int key, Bookshelf shelf)
    {
        shelves.put(key, shelf);
    }
    
    /**
     * Returns the given bookshelf.
     * @param key The identifier to retrieve the bookshelf.
     * @return The given bookshelf.
     */
    public Bookshelf getShelf(int key)
    {
        return shelves.get(key);
    }
    
    /**
     * Sets the threshold needed to restock the group.
     * @param percentRestock The new threshold as a decimal.
     */
    public void setPercentRestock(double percentRestock)
    {
        this.percentRestock = percentRestock;
    }
    
    /**
     * Returns the threshold needed to restock the group.
     * @return The threshold as a decimal.
     */
    public double getPercentRestock()
    {
        return percentRestock;
    }
    
    /**
     * Returns the potential grossRevenue of the entire group of shelves.
     * @return The potential grossRevenue of the entire group of shelves.
     */
    public double getGrossRevenue()
    {
        double revenue = 0.0;
        
        //honestly i maybe should've used getshelf here, but whatever
        for(Bookshelf value : shelves.values())
        {
            revenue += value.getGrossRevenue();
        }
        
        return revenue;
    }
    
    /**
     * Returns all shelf criteria classifications as a list of strings.
     * @return All shelf criteria classifications as a list of strings.
     */
    public List<Criteria> getAllShelfCriteria()
    {
        List<Criteria> criteria = new ArrayList<Criteria>();
        for(Bookshelf value : shelves.values())
        {
            criteria.add(value.getCriteria());
        }
        
        return criteria;
    }
    
    /**
     * Returns all specific criteria types as a list of strings.
     * @return All specific criteria types as a list of strings.
     */
    public List<String> getAllShelfCriteriaTypes()
    {
        List<String> types = new ArrayList<String>();
        for(Bookshelf value : shelves.values())
        {
            types.add(value.getCriteriaType());
        }
        
        return types;
    }
    
    //I think I need an exception here, though maybe not
    /**
     * Returns the first bookshelf containing the given book.
     * @param isb The identifying number of the book to be found.
     * @return The bookshelf containing the given book.
     */
    public Bookshelf findFirstShelfOfBook(long isb)
    {
        int shelfFound = -1;
        for(Bookshelf value : shelves.values())
        {
            try
            {
                shelfFound = value.getShelfOfFirstBookInstance(isb);
                if(shelfFound != -1) return value;
            }
            catch(BookDoesNotExistException e)
            {
                continue;
            }
        }
        
        return null;
    }
    
    /**
     * Returns the number of copies of the given book.
     * @param isbn The identifying number of the book to find the copies of.
     * @return The number of copies of the given book.
     */
    public int getNumCopiesOfBook(long isbn)
    {
        int copies = 0;
        for(Bookshelf value : shelves.values())
        {
            copies += value.getNumCopiesOfBook(isbn);
        }
        return copies;
    }
    
    /**
     * Checks whether the group needs to be restocked.
     * @param name The name of the person checking the group.
     * @return Whether the group needs to be restocked.
     */
    public boolean checkForRestock(String name)
    {
        double percent = 0.0;
        int numOfShelves = shelves.size();
        int shelvesNeedingRestock = 0;
        for(Bookshelf value : shelves.values())
        {
            if(value.getRestock())
            {
                shelvesNeedingRestock++;
            }
        }
        
        percent = (double)shelvesNeedingRestock / (double)numOfShelves;
        if(percent >= percentRestock)
        {
            //I'm honestly not sure if this is the best way to handle this
            needsRestock = true;
            BufferedWriter writer = null;
            try
            {
                String timeLog = new SimpleDateFormat("EEE dd MMM yyyy hh mm a").format(Calendar.getInstance().getTime());
                File logFile = new File("RestockData.txt");

                writer = new BufferedWriter(new FileWriter(logFile, true));
                writer.write("Restock " + name + " " + timeLog + " " + cycleRevenue + "\n");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                try
                {
                    // Close the writer regardless of what happens...
                    writer.close();
                    cycleRevenue = 0;
                }
                catch (Exception e){}
            }
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /**
     * Signals that the group has been restocked, resetting the appropriate variables.
     * @param restockerName The person restocking the group.
     */
    public void signalRestock(String restockerName)
    {
        for(Bookshelf value : shelves.values())
        {
            if(value.getRestock() == true) value.setRestock();
        }
        BufferedWriter writer = null;
        try
        {
            String timeLog = new SimpleDateFormat("EEE dd MMM yyyy hh mm a").format(Calendar.getInstance().getTime());
            File logFile = new File("RestockData.txt");

            writer = new BufferedWriter(new FileWriter(logFile, true));
            writer.write(restockerName + " " + timeLog + "\n");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                // Close the writer regardless of what happens...
                writer.close();
            }
            catch (Exception e){}
        }
    }
    
    //this needed a fair bit of reworking to get rid of some errors introduced when some functions in bookshelf
    //changed, need to double check and make sure everything works here
    /**
     * Removes the first instance of the given book from the group of shelves.
     * @param isbn The identifying number of the book to be removed.
     * @throws BookDoesNotExistException If the book isn't found on any bookshelf in the group.
     */
    public void removeFirstInstanceOfBook(long isbn) throws BookDoesNotExistException
    {
        outerloop:
        for(Bookshelf value : shelves.values())
        {
            for(int i = 0; i < value.getNumOfShelves(); i++)
            {
                for(int j = 0; j < value.getSingularShelf(i).size(); j++)
                {
                    if(value.getSingularShelf(i).get(j).getIsb() != isbn)
                    {
                        Book removedBook = value.removeBook(isbn);
                        cycleRevenue += removedBook.getPrice();
                        break outerloop;
                    }
                }
            }
        }
    }
}
