package bookroom;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * A class to analyze data about the BookshelfGroup's restocking.
 * @author Cody
 * @version 1.0
 * @since 1.0
 */

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
    
    /**
     * Flag to check whether the file has been read into the data structures yet.
     */
    private boolean hasReadFile = false;
    /**
     * Data structure that holds the names of restockers and how often their names appear.
     */
    private Map<String, Integer> restockers;
    /**
     * Data structure that holds the names of checkers and how often their names appear.
     */
    private Map<String, Integer> checkers;
    /**
     * Data structure that holds the days that restocks happen and how often they happen.
     */
    private Map<String, Integer> restockDays;
    /**
     * Data structure that holds the months that restocks happen and how often they happen.
     */
    private Map<String, Integer> restockMonths;
    /**
     * Data structure that holds the days that checks happen and how often they happen.
     */
    private Map<String, Integer> checkDays;
    /**
     * Data structure that holds the months that checks happen and how often they happen.
     */
    private Map<String, Integer> checkMonths;
    /**
     * Data structure that holds the lines of the restock data text file.
     */
    private List<String> restockData;
    
    /**
     * Constructor for Analytics.
     */
    public Analytics()
    {
        restockers = new HashMap<String, Integer>();
        checkers = new HashMap<String, Integer>();
        restockData = new ArrayList<String>();
        restockDays = new HashMap<String, Integer>();
        restockMonths = new HashMap<String, Integer>();
        checkDays = new HashMap<String, Integer>();
        checkMonths = new HashMap<String, Integer>();
    }
    
    //There's something screwy going on with the read loop in here,
    //but I can't figure out *what*. Sticking println(restockdataSize)
    //in there shows that the loop is running way more than it should
    //It doesn't affect the end numbers at all though, which is weird
    /**
     * Method to read in the restock data file into the appropriate data structure.
     */
    public void readRestockFile()
    {
        BufferedReader reader = null;
        String line = null;
        try
        {
            reader = new BufferedReader(new FileReader("RestockData.txt"));
            String[] words = new String[10];
            while((line = reader.readLine()) != null)
            {
                restockData.add(line);
                if(!line.matches("Restock.+"))
                {
                    words = line.split(" ");
                    if(restockers.containsKey(words[0]))
                    {
                        restockers.put(words[0], restockers.get(words[0]) + 1);
                    }
                    else
                    {
                        restockers.put(words[0], 1);
                    }
                    
                    if(restockDays.containsKey(words[1]))
                    {
                        restockDays.put(words[1], restockDays.get(words[1]) + 1);
                    }
                    else
                    {
                        restockDays.put(words[1], 1);
                    }
                    
                    if(restockMonths.containsKey(words[3]))
                    {
                        restockMonths.put(words[3], restockMonths.get(words[3]) + 1);
                    }
                    else
                    {
                        restockMonths.put(words[3], 1);
                    }
                }
                else if(line.matches("Restock.+"))
                {
                    words = line.split(" ");
                    if(checkers.containsKey(words[1]))
                    {
                        checkers.put(words[1], checkers.get(words[1]) + 1);
                    }
                    else
                    {
                        checkers.put(words[1], 1);
                    }
                    
                    if(checkDays.containsKey(words[2]))
                    {
                        checkDays.put(words[2], checkDays.get(words[2]) + 1);
                    }
                    else
                    {
                        checkDays.put(words[2], 1);
                    }
                    
                    if(checkMonths.containsKey(words[4]))
                    {
                        checkMonths.put(words[4], checkMonths.get(words[4]) + 1);
                    }
                    else
                    {
                        checkMonths.put(words[4], 1);
                    }
                }
            }
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
                reader.close();
                hasReadFile = true;
            }
            catch (Exception e){}
        }
    }
    
    /**
     * Returns the number of times the given person has restocked the group.
     * @param name The person to check the numbers of.
     * @return The number of times the given person has restocked the group.
     */
    public int getRestockNumbers(String name)
    {
        try
        {
            return restockers.get(name);
        }
        catch(NullPointerException e)
        {
            return 0;
        }
    }

    /**
     * Returns the number of times the given person has checked the group.
     * @param name The person to check the numbers of.
     * @return The number of times the given person has checked the group.
     */
    public int getCheckerNumbers(String name)
    {
        try
        {
            return checkers.get(name);
        }
        catch(NullPointerException e)
        {
            return 0;
        }
    }
    
    /**
     * Returns the line of restock data at the given index.
     * @param index The line of data to return.
     * @return The line of restock data.
     */
    public String getRestockData(int index)
    {
        return restockData.get(index);
    }
    
    //This could probably be made to specifically check for a 24
    //hour period, but this works for basic functionality
    /**
     * Checks whether a 24 hour period has passed between the given check and restock.
     * @param indexOne The index of the desired check data.
     * @param indexTwo The index of the desired restock data.
     * @return True if 24 hours have passed, false otherwise.
     */
    public boolean checkOneDayPassed(int indexOne, int indexTwo)
    {
        String[] checkLine = restockData.get(indexOne).split(" ");
        String[] restockLine = restockData.get(indexTwo).split(" ");
        return(!checkLine[2].equals(restockLine[1]));
    }
    
    //I'm not sure if this is better than just letting a person check restock
    //numbers directly via the getter, but hey, people are lazy and this
    //saves on mental math
    /**
     * Checks to see if the given person is doing the most restocking.
     * @param name The person to check the numbers of.
     * @return True if the person is doing the most restocking, false otherwise.
     */
    public boolean checkForRestocker(String name)
    {
        int restockerRestocks = 0;
        try
        {
            restockerRestocks = restockers.get(name);
        }
        catch(NullPointerException e)
        {
            return false;
        }
        for(int value : restockers.values())
        {
            //an argument to be made that it should be equal to as well, but that's more
            //personal preference than anything.
            if(value > restockerRestocks)
            {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Returns the person who restocks the group most frequently.
     * @return The name of the person who restocks most frequently.
     */
    public String findMostFrequentRestocker()
    {
        String restocker = new String();
        int mostFrequentRestocks = 0;
        Set<String> keySet = restockers.keySet();
        for(String key : keySet)
        {
            if(restockers.get(key) > mostFrequentRestocks)
            {
                mostFrequentRestocks = restockers.get(key);
                restocker = new String(key);
            }
        }
        
        return restocker;
    }
    
    /**
     * Checks to see if the given person is doing the most checking.
     * @param name The person to check the numbers of.
     * @return True if the person is doing the most checking, false otherwise.
     */
    public boolean checkForChecker(String name)
    {
        int checkerChecks = 0;
        try
        {
            checkerChecks = checkers.get(name);
        }
        catch(NullPointerException e)
        {
            return false;
        }
        for(int value : checkers.values())
        {
            //an argument to be made that it should be equal to as well, but that's more
            //personal preference than anything.
            if(value > checkerChecks)
            {
                return false;
            }
        }
        return true;
    }
    
    //Bit of a weird name, this checks for who's signalling the need for restock most often
    //I'll have to rework the file writing a bit to make this work
    /**
     * Returns the person who checks the group most frequently.
     * @return The name of the person who checks most frequently.
     */
    public String findMostFrequentChecker()
    {
        String checker = new String();
        int mostFrequentChecks = 0;
        Set<String> keySet = checkers.keySet();
        for(String key : keySet)
        {
            if(checkers.get(key) > mostFrequentChecks)
            {
                mostFrequentChecks = checkers.get(key);
                checker = new String(key);
            }
        }
        
        return checker;
    }
    
    /**
     * Returns the most frequent day restocks happen.
     * @return The most frequent day restocks happen.
     */
    public String checkMostFrequentDayRestocked()
    {
        String day = new String();
        int mostFrequentDay = 0;
        Set<String> keySet = restockDays.keySet();
        for(String key : keySet)
        {
            if(restockDays.get(key) > mostFrequentDay)
            {
                mostFrequentDay = restockDays.get(key);
                day = new String(key);
            }
        }
        
        return day;
    }
    
    /**
     * Returns the most frequent month restocks happen.
     * @return The most frequent month restocks happen.
     */
    public String checkMostFrequentMonthRestocked()
    {
        String month = new String();
        int mostFrequentMonth = 0;
        Set<String> keySet = restockMonths.keySet();
        for(String key : keySet)
        {
            if(restockMonths.get(key) > mostFrequentMonth)
            {
                mostFrequentMonth = restockMonths.get(key);
                month = new String(key);
            }
        }
        
        return month;
    }
    
    /**
     * Returns the most frequent day checks happen.
     * @return The most frequent day checks happen.
     */
    public String checkMostFrequentDayChecked()
    {
        String day = new String();
        int mostFrequentDay = 0;
        Set<String> keySet = checkDays.keySet();
        for(String key : keySet)
        {
            if(checkDays.get(key) > mostFrequentDay)
            {
                mostFrequentDay = checkDays.get(key);
                day = new String(key);
            }
        }
        
        return day;
    }
    
    /**
     * Returns the most frequent month checks happen.
     * @return The most frequent month checks happen.
     */
    public String checkMostFrequentMonthChecked()
    {
        String month = new String();
        int mostFrequentMonth = 0;
        Set<String> keySet = checkMonths.keySet();
        for(String key : keySet)
        {
            if(checkMonths.get(key) > mostFrequentMonth)
            {
                mostFrequentMonth = checkMonths.get(key);
                month = new String(key);
            }
        }
        
        return month;
    }
    
    //not sure about the return type here either yet, need to look into how this
    //kinda thing is done

    //both this and the other have issues with single times that are thirds
    //of 60, probably because it's an repeating decimal and rounding weirdness?
    /**
     * Returns the average time checking happens, as a string.
     * @return A string representing the average time checks happen.
     */
    public String findAverageRestockSignalTime()
    {
        int index = 0;
        double average = 0;
        double count = 0;
        String[] data = new String[10];
        while(index < restockData.size())
        {
            data = restockData.get(index).split(" ");
            Double hour = Double.parseDouble(data[6]);
            Double min = Double.parseDouble(data[7]);
            min = min / 60; //this'll be where everything screws up, i bet
            average += (hour + min);
            count++;
            index += 2;
        }
        
        average = average / count;
        
        int wholeNumber = (int) average; //i need the whole number portion of the average, here
        double minute = average - (double) wholeNumber;
        
        minute *= 60; //dunno if I need to move the decimal here or not

        String averageTime = new String(Integer.toString(wholeNumber) + ":" + Integer.toString((int)minute));
        
        return averageTime;
    }
    
    //both this and the other have issues with single times that are thirds
    //of 60, probably because it's an repeating decimal and rounding weirdness?
    /**
     * Returns the average time restocks happen as a String.
     * @return A string representing the average times restocks happen.
     */
    public String findAverageRoomRestockedTime()
    {
        int index = 1;
        double average = 0;
        double count = 0;
        String[] data = new String[10];
        while(index < restockData.size())
        {
            data = restockData.get(index).split(" ");
            Double hour = Double.parseDouble(data[5]);
            Double min = Double.parseDouble(data[6]);
            min = min / 60; //this'll be where everything screws up, i bet
            average += (hour + min);
            count++;
            index += 2;
        }
        
        average = average / count;
        
        int wholeNumber = (int) average; //i need the whole number portion of the average, here
        double minute = average - (double) wholeNumber;
        
        minute *= 60; //dunno if I need to move the decimal here or not

        String averageTime = new String(Integer.toString(wholeNumber) + ":" + Integer.toString((int)minute));
        
        return averageTime;
    }
    
    /**
     * Returns the total number of restocks in the data file.
     * @return The total number of restocks.
     */
    public int findTotalNumberOfRestocks()
    {
        int restocks = 0;
        for(int value : restockers.values())
        {
            restocks += value;
        }
        return restocks;
    }
    
    /**
     * Returns the total number of checks in the data file.
     * @return The total number of checks.
     */
    public int findTotalNumberOfChecks()
    {
        int checks = 0;
        for(int value : checkers.values())
        {
            checks += value;
        }
        return checks;
    }
    
    /**
     * Returns the gross revenue of a given restock.
     * @param restockNum The index of the restock to check.
     * @return The gross revnue of the given restock.
     */
    public double getGrossRevenue(int restockNum)
    {
        String[] data = restockData.get(restockNum).split(" ");
        double revenue = Double.valueOf(data[9]);
        return revenue;
    }
    
    /**
     * Checks if the file has been read.
     * @return True if the file has been read, false otherwise.
     */
    public boolean checkIfFileIsRead()
    {
        return hasReadFile;
    }
}