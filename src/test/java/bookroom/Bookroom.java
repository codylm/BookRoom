package bookroom;

import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

public class Bookroom
{
    private Map<Integer, Bookshelf> shelves;
    private double percentRestock;
    private boolean needsRestock;
    
    public Bookroom(double percentRestock)
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
    public int getNumCopiesOfBook(Book book)
    {
        int copies = 0;
        for(Bookshelf value : shelves.values())
        {
            copies += value.getNumCopiesOfBook(book);
        }
        return copies;
    }
    
    public boolean checkForRestock()
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
                String timeLog = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss a").format(Calendar.getInstance().getTime());
                File logFile = new File("Restock Data");

                writer = new BufferedWriter(new FileWriter(logFile, true));
                writer.write("Restock " + timeLog + "\n");
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
            String timeLog = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss a").format(Calendar.getInstance().getTime());
            File logFile = new File("Restock Data");

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
}
