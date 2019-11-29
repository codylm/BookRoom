package bookroom;

import java.util.*;

public class Bookroom
{
    private Map<Integer, Bookshelf> shelves;
    
    public Bookroom()
    {
        shelves = new HashMap<Integer, Bookshelf>();
    }
    
    public void addShelf(int key, Bookshelf shelf)
    {
        shelves.put(key, shelf);
    }
    
    public Bookshelf getShelf(int key)
    {
        return shelves.get(key);
    }
    
    public double getGrossRevenue()
    {
        double revenue = 0.0;
        /*Iterator iterator = shelves.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry pair = (Map.Entry)iterator.next();
            revenue += pair.getValue().getGrossRevenue();
            iterator.remove(); // avoids a ConcurrentModificationException
        }*/
        
        //honestly i maybe should've used getshelf here, but whatever
        for(Bookshelf value : shelves.values())
        {
            revenue += value.getGrossRevenue();
        }
        
        return revenue;
    }
}
