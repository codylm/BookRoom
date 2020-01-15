package bookroom;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.*;

public class Bookroom extends JFrame
{
    
    JComboBox bookroomBox, percentBox;
    JTextField isbnField, shelvesField, shelvesField2;
    JButton addButton, selectButton, percentButton, infoButton, criteriaButton,
            findFirstShelfButton, findNumCopiesButton, checkRestockButton,
            signalRestockButton, removeBookButton, changeCriteriaButton, changeCriteriaTypeButton,
            contentsOfShelfButton, booksOnShelfButton, getShelfIsFullButton, toggleShelfFullButton,
            addBookButton, getSpecificShelfButton, allWrongBooksButton, copiesOfBookButton,
            sortShelfButton; //not sure about selecting but w/e i'll work on it
    JLabel bookshelvesLabel, selectedLabel, percentLabel, isbnLabel,
           restockLabel, shelvesLabel, shelvesLabel2;
    
    public static void main(String[] args)
    {
        new Bookroom();
    }

    private Bookroom()
    {
        this.setSize(400, 400);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Bookroom");
        JPanel thePanel = new JPanel();
        thePanel.setLayout(new GridBagLayout());
        
        JPanel shelfPanel = new JPanel();
        Border shelfBorder = BorderFactory.createTitledBorder("Bookshelves");
        shelfPanel.setBorder(shelfBorder);
        shelfPanel.setLayout(new GridBagLayout());

        //Debug
        String[] placeholders = {"Bookshelf 1", "Bookshelf 2", "Bookshelf 3"};
        bookroomBox = new JComboBox(placeholders);
        addComp(shelfPanel, bookroomBox, 0, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        addButton = new JButton("Add New Bookshelf");
        addComp(shelfPanel, addButton, 1, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        selectButton = new JButton("Select Bookshelf");
        addComp(shelfPanel, selectButton, 2, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        
        selectedLabel = new JLabel("Selected: Bookshelf 1"); //selected bookshelf is placeholder
        addComp(shelfPanel, selectedLabel, 0, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        infoButton = new JButton("Get Shelf Info");
        addComp(shelfPanel, infoButton, 1, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        criteriaButton = new JButton("Get All Shelf Classifications");
        addComp(shelfPanel, criteriaButton, 2, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);

        isbnLabel = new JLabel("ISBN of Book:");
        addComp(shelfPanel, isbnLabel, 0, 2, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        isbnField = new JTextField(8);
        addComp(shelfPanel, isbnField, 0, 2, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE);
        percentLabel = new JLabel("Current Percent: 100%");
        addComp(shelfPanel, percentLabel, 1, 2, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        String[] percents = {"10%", "20%", "30%", "40%", "50%", "60%", "70%", "80%", "90%", "100%"};
        percentBox = new JComboBox(percents);
        addComp(shelfPanel, percentBox, 1, 2, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE);
        percentButton = new JButton("Set Restock Percent");
        addComp(shelfPanel, percentButton, 2, 2, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        
        removeBookButton = new JButton("Remove First Instance of Book");
        addComp(shelfPanel, removeBookButton, 0, 3, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        findFirstShelfButton = new JButton("Find Shelf of First Book Instance");
        addComp(shelfPanel, findFirstShelfButton, 1, 3, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        findNumCopiesButton = new JButton("Find # of Copies of Book");
        addComp(shelfPanel, findNumCopiesButton, 2, 3, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        
        restockLabel = new JLabel("Bookshelves Needs Restock: NO");
        addComp(shelfPanel, restockLabel, 0, 4, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        checkRestockButton = new JButton("Check if Shelves Needs Restocking");
        addComp(shelfPanel, checkRestockButton, 1, 4, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        signalRestockButton = new JButton("Signal that Shelves are Restocked");
        addComp(shelfPanel, signalRestockButton, 2, 4, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        
        addComp(thePanel, shelfPanel, 0, 0, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.NONE);

        JPanel selectedPanel = new JPanel();
        Border selectedBorder = BorderFactory.createTitledBorder("Selected Bookshelf");
        selectedPanel.setBorder(selectedBorder);
        selectedPanel.setLayout(new GridBagLayout());
        
        changeCriteriaButton = new JButton("Change Main Classification");
        addComp(selectedPanel, changeCriteriaButton, 0, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        changeCriteriaTypeButton = new JButton("Change Sub-Classification");
        addComp(selectedPanel, changeCriteriaTypeButton, 1, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        shelvesLabel = new JLabel("Shelf #: ");
        addComp(selectedPanel, shelvesLabel, 2, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        shelvesField = new JTextField(2);
        addComp(selectedPanel, shelvesField, 2, 0, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE);
        contentsOfShelfButton = new JButton("Get Books on Given Shelf");
        addComp(selectedPanel, contentsOfShelfButton, 3, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        
        booksOnShelfButton = new JButton("# of Books on Shelf");
        addComp(selectedPanel, booksOnShelfButton, 0, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        getShelfIsFullButton = new JButton("Check if Shelf is Full");
        addComp(selectedPanel, getShelfIsFullButton, 1, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        toggleShelfFullButton = new JButton("Mark Shelf as Full/Not Full");
        addComp(selectedPanel, toggleShelfFullButton, 2, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        addBookButton = new JButton("Add Book to Shelf");
        addComp(selectedPanel, addBookButton, 3, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);

        getSpecificShelfButton = new JButton("Get Contents of Single Shelf");
        addComp(selectedPanel, getSpecificShelfButton, 0, 2, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        allWrongBooksButton = new JButton("Find Incorrect Books");
        addComp(selectedPanel, allWrongBooksButton, 1, 2, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        copiesOfBookButton = new JButton("Find All Copies of Book");
        addComp(selectedPanel, copiesOfBookButton, 2, 2, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        sortShelfButton = new JButton("Sort Shelf");
        addComp(selectedPanel, sortShelfButton, 3, 2, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        
        addComp(thePanel, selectedPanel, 0, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.NONE);
        
        this.add(thePanel);
        this.pack();
        this.setVisible(true);
    }
 
    private void addComp(JPanel thePanel, JComponent comp, int xPos, int yPos, int compWidth, int compHeight, int place, int stretch)
    {
        GridBagConstraints gridConstraints = new GridBagConstraints();
        
        gridConstraints.gridx = xPos;
        gridConstraints.gridy = yPos;
        gridConstraints.gridwidth = compWidth;
        gridConstraints.gridheight = compHeight;
        gridConstraints.weightx = 100;
        gridConstraints.weighty = 100;
        gridConstraints.insets = new Insets(5, 5, 5, 5);
        gridConstraints.anchor = place;
        gridConstraints.fill = stretch;
        
        thePanel.add(comp, gridConstraints);
    }
}
