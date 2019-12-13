package bookroom;

import java.util.*;

public class Analytics
{
    //Yknow, i'm not actually sure how I wanna pass the file around the class,
    //and it feels like bad practice to continually open and close the file in
    //each method. Maybe store each line into a string... array? list? it needs
    //to have a flexible size
    
    //this'll have a bit more to do with prices down the line, but i'm not
    //quite on that step yet
    
    //I could do some deeper, longer term, trends in here, maybe, but that would
    //probably require saving the various bits of data extracted from the restock
    //file to their own file(s) and using those files to set up the analysis,
    //so maybe something to shoot for down the line
    
    private Map<String, Integer> restockers;
    private List<String> restockData;
    
    public Analytics()
    {
        restockers = new HashMap<String, Integer>();
        restockData = new ArrayList<String>();
    }
    
    public int getRestockNumbers(String name)
    {
        return 0;
    }
    
    public String getRestockData(int index)
    {
        return "";
    }
    public void readRestockFile()
    {
        
    }
    
    //I'm not sure if int is what I wanna return here, but it should be fine
    public int compareRestockDates()
    {
        return 0;
    }
    
    public boolean checkOneDayPassed()
    {
        return false;
    }
    
    //I'm not sure if this is better than just letting a person check restock
    //numbers directly via the getter, but hey, people are lazy and this
    //saves on mental math
    public boolean checkForRestocker(String name)
    {
        return false;
    }
    
    public String findMostFrequentRestocker()
    {
        return "";
    }
    
    //Bit of a weird name, this checks for who's signalling the need for restock most often
    public String findMostFrequentChecker()
    {
        return "";
    }
    
    public String checkMostFrequentDayRestocked()
    {
        return "";
    }
    
    public String checkMostFrequentMonthRestocked()
    {
        return "";
    }
    
    public String checkMostFrequentDayNeedingRestock()
    {
        return "";
    }
    
    public String checkMostFrequentMonthNeedingRestock()
    {
        return "";
    }
    
    //not sure about the return type here either yet, need to look into how this
    //kinda thing is done
    public int findAverageRestockSignalTime()
    {
        return 0;
    }
    
    public int findAverageRoomRestockedTime()
    {
        return 0;
    }
    
    public int findTotalNumberOfRestocks()
    {
        return 0;
    }
}