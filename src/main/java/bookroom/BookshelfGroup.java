package bookroom;

import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

public class BookshelfGroup
{
    private Map<Integer, Bookshelf> shelves;
    private double percentRestock;
    private double cycleRevenue;
    private boolean needsRestock;
    
    public BookshelfGroup(double percentRestock)
    {
        shelves = new HashMap<Integer, Bookshelf>();
        this.percentRestock = percentRestock;
    }
    
    public void addShelf(int key, Bookshelf shelf)
    {
        shelves.put(key, shelf);
    }
    
    public Bookshelf getShelf(int key)
    {
        return shelves.get(key);
    }
    
    public void setPercentRestock(double percentRestock)
    {
        this.percentRestock = percentRestock;
    }
    
    public double getPercentRestock()
    {
        return percentRestock;
    }
    
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
    
    public List<Criteria> getAllShelfCriteria()
    {
        List<Criteria> criteria = new ArrayList<Criteria>();
        for(Bookshelf value : shelves.values())
        {
            criteria.add(value.getCriteria());
        }
        
        return criteria;
    }
    
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
    
    /*public Book findFirstInstanceOfBook(long isb)
    {
       I'm not sure I actually need this one, i'll leave it in as a comment for now 
    }*/
    
    //not sure if this should take a book or an isbn
    //probably need to rework this to an isbn
    public int getNumCopiesOfBook(long isbn)
    {
        int copies = 0;
        for(Bookshelf value : shelves.values())
        {
            copies += value.getNumCopiesOfBook(isbn);
        }
        return copies;
    }
    
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
