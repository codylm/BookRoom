package bookroom;

import java.util.*;
import java.time.*;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.*;

public class Bookroom extends JFrame
{
    
    JComboBox bookroomBox, percentBox;
    JTextField isbnField, shelvesField, shelvesField2, nameField, revenueField, empNameField;
    JButton addButton, selectButton, percentButton, infoButton, criteriaButton,
            findFirstShelfButton, findNumCopiesButton, checkRestockButton,
            signalRestockButton, removeBookButton, totalRestocksButton,
            totalChecksButton, getRevenueButton;
    JButton changeCriteriaButton, changeCriteriaTypeButton, contentsOfShelfButton,
            booksOnShelfButton, getShelfIsFullButton, toggleShelfFullButton, addBookButton,
            getSpecificShelfButton, allWrongBooksButton, copiesOfBookButton, sortShelfButton; //not sure about selecting but w/e i'll work on it
    JButton readFileButton, restockerButton, checkerButton, checkRestockerButton, checkCheckerButton,
            frequentRestockerButton, frequentCheckerButton, frequentRestockDayButton,
            frequentRestockMonthButton, frequentCheckDayButton, frequentCheckMonthButton,
            averageRestockButton, averageCheckButton, setRestockButton;
    JLabel bookshelvesLabel, selectedLabel, percentLabel, isbnLabel, empNameLabel,
           shelvesLabel, shelvesLabel2, nameLabel, revenueLabel;
    
    private BookshelfGroup bookroom = new BookshelfGroup(0.4);
    private Bookshelf selectedShelf;
    private Analytics analyzer = new Analytics();
    private int bookshelfNum = 0, singleShelfIndex = 0;
    private String bookshelfInfo = "";
    
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
       
        ListenForButtons lForButtons = new ListenForButtons();

        //Debug
        bookroomBox = new JComboBox();
        addComp(shelfPanel, bookroomBox, 0, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        addButton = new JButton("Add New Bookshelf");
        addButton.addActionListener(lForButtons);
        addComp(shelfPanel, addButton, 1, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        selectButton = new JButton("Select Bookshelf");
        selectButton.addActionListener(lForButtons);
        addComp(shelfPanel, selectButton, 2, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        
        selectedLabel = new JLabel("Selected: "); //selected bookshelf is placeholder
        addComp(shelfPanel, selectedLabel, 0, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        infoButton = new JButton("Get Shelf Info");
        infoButton.addActionListener(lForButtons);
        addComp(shelfPanel, infoButton, 1, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        criteriaButton = new JButton("Get All Shelf Classifications");
        criteriaButton.addActionListener(lForButtons);
        addComp(shelfPanel, criteriaButton, 2, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);

        isbnLabel = new JLabel("ISBN of Book: ");
        addComp(shelfPanel, isbnLabel, 0, 2, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        isbnField = new JTextField(8);
        addComp(shelfPanel, isbnField, 0, 2, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE);
        percentLabel = new JLabel("Current Percent: " + bookroom.getPercentRestock());
        addComp(shelfPanel, percentLabel, 1, 2, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        String[] percents = {"0.1", "0.2", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9", "1"};
        percentBox = new JComboBox(percents);
        addComp(shelfPanel, percentBox, 1, 2, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE);
        percentButton = new JButton("Set Restock Percent");
        percentButton.addActionListener(lForButtons);
        addComp(shelfPanel, percentButton, 2, 2, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        
        removeBookButton = new JButton("Remove First Instance of Book");
        addComp(shelfPanel, removeBookButton, 0, 3, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        findFirstShelfButton = new JButton("Find Shelf of First Book Instance");
        findFirstShelfButton.addActionListener(lForButtons);
        addComp(shelfPanel, findFirstShelfButton, 1, 3, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        findNumCopiesButton = new JButton("Find # of Copies of Book");
        addComp(shelfPanel, findNumCopiesButton, 2, 3, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        
        empNameLabel = new JLabel("Employee Name: ");
        addComp(shelfPanel, empNameLabel, 0, 4, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE);
        empNameField = new JTextField(15);
        addComp(shelfPanel, empNameField, 1, 4, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        
        checkRestockButton = new JButton("Check if Shelves Needs Restocking");
        checkRestockButton.addActionListener(lForButtons);
        addComp(shelfPanel, checkRestockButton, 2, 4, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        
        signalRestockButton = new JButton("Signal that Shelves are Restocked");
        signalRestockButton.addActionListener(lForButtons);
        addComp(shelfPanel, signalRestockButton, 2, 5, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        
        
        
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
        addBookButton.addActionListener(lForButtons);
        addComp(selectedPanel, addBookButton, 3, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);

        getSpecificShelfButton = new JButton("Get Contents of Single Shelf");
        addComp(selectedPanel, getSpecificShelfButton, 0, 2, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        allWrongBooksButton = new JButton("Find Incorrect Books");
        addComp(selectedPanel, allWrongBooksButton, 1, 2, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        copiesOfBookButton = new JButton("Find All Copies of Book");
        addComp(selectedPanel, copiesOfBookButton, 2, 2, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        sortShelfButton = new JButton("Sort Shelf");
        addComp(selectedPanel, sortShelfButton, 3, 2, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        
        setRestockButton = new JButton("Mark Shelf for Restock");
        setRestockButton.addActionListener(lForButtons);
        addComp(selectedPanel, setRestockButton, 0, 3, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        
        addComp(thePanel, selectedPanel, 0, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.NONE);
        
        JPanel analyticsPanel = new JPanel();
        Border analyticsBorder = BorderFactory.createTitledBorder("Analytics");
        analyticsPanel.setBorder(analyticsBorder);
        analyticsPanel.setLayout(new GridBagLayout());
        
        readFileButton = new JButton("Read Restock File");
        addComp(analyticsPanel, readFileButton, 0, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        nameLabel = new JLabel("Name: ");
        addComp(analyticsPanel, nameLabel, 1, 0, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE);
        nameField = new JTextField(15);
        addComp(analyticsPanel, nameField, 2, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        

        restockerButton = new JButton("Restocker Numbers");
        addComp(analyticsPanel, restockerButton, 0, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        checkerButton = new JButton("Checker Numbers");
        addComp(analyticsPanel, checkerButton, 1, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        checkRestockerButton = new JButton("Check For Restocker");
        addComp(analyticsPanel, checkRestockerButton, 2, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        checkCheckerButton = new JButton("Check for Checker");
        addComp(analyticsPanel, checkCheckerButton, 3, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        
        frequentRestockerButton = new JButton("Most Frequent Restocker");
        addComp(analyticsPanel, frequentRestockerButton, 0, 2, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        frequentCheckerButton = new JButton("Most Frequent Checker");
        addComp(analyticsPanel, frequentCheckerButton, 1, 2, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        averageRestockButton = new JButton("Get Average Restock Time");
        addComp(analyticsPanel, averageRestockButton, 2, 2, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        averageCheckButton = new JButton("Get Average Check Time");
        addComp(analyticsPanel, averageCheckButton, 3, 2, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        
        
        frequentRestockDayButton = new JButton("Most Frequent Restock Day");
        addComp(analyticsPanel, frequentRestockDayButton, 0, 3, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        frequentRestockMonthButton = new JButton("Most Frequent Restock Month");
        addComp(analyticsPanel, frequentRestockMonthButton, 1, 3, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        frequentCheckDayButton = new JButton("Most Frequent Check Day");
        addComp(analyticsPanel, frequentCheckDayButton, 2, 3, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        frequentCheckMonthButton = new JButton("Most Frequent Check Month");
        addComp(analyticsPanel, frequentCheckMonthButton, 3, 3, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        
        totalRestocksButton = new JButton("Get Total Restocks");
        addComp(analyticsPanel, totalRestocksButton, 0, 4, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        totalChecksButton = new JButton("Get Total Checks");
        addComp(analyticsPanel, totalChecksButton, 1, 4, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        //not sure about these two things, but
        revenueLabel = new JLabel("Get Specific Restock:");
        addComp(analyticsPanel, revenueLabel, 2, 4, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        revenueField = new JTextField(2);
        addComp(analyticsPanel, revenueField, 2, 4, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE);
        getRevenueButton = new JButton("Retrieve Revenue");
        addComp(analyticsPanel, getRevenueButton, 3, 4, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        
        addComp(thePanel, analyticsPanel, 0, 2, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.NONE);
        
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
    
    private class ListenForButtons implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if(e.getSource() == addButton)
            {
                Bookshelf shelf = new Bookshelf(4, "Fiction", Criteria.GENRE);
                bookroomBox.addItem(shelf);
                bookroom.addShelf(bookshelfNum, shelf);
                bookshelfNum++;
                if(selectedShelf == null)
                {
                    selectedShelf = shelf; //i think this works because reference variables?
                    selectedLabel.setText("Selected: " + selectedShelf.toString());
                }
            }
            //do I even need this with getSelectedItem being a thing?
            else if(e.getSource() == selectButton)
            {
                selectedShelf = (Bookshelf)bookroomBox.getSelectedItem();
                selectedLabel.setText("Selected: " + selectedShelf.toString());
            }
            else if(e.getSource() == infoButton)
            {
                if(selectedShelf != null)
                {
                    bookshelfInfo += "Num of Shelves: " + selectedShelf.getNumOfShelves() + "\n";
                    bookshelfInfo += "Classification: " + selectedShelf.getCriteria() + "\n";
                    bookshelfInfo += "Sub-Classification: " + selectedShelf.getCriteriaType() + "\n";
                    bookshelfInfo += "Number of Books: " + selectedShelf.getNumBooksOnShelves() + "\n";
                    bookshelfInfo += "Needs Restocking: " + selectedShelf.getRestock() + "\n";
                    bookshelfInfo += "Potential Gross Revenue: " + selectedShelf.getGrossRevenue() + "\n";
                    
                    JOptionPane.showMessageDialog(Bookroom.this, bookshelfInfo, "Shelf Info", JOptionPane.INFORMATION_MESSAGE);
                    bookshelfInfo = "";
                }
            }
            else if(e.getSource() == criteriaButton)
            {
                List<Criteria> criteria = bookroom.getAllShelfCriteria();
                List<String> criteriaTypes = bookroom.getAllShelfCriteriaTypes();
                
                for(int i = 0; i < criteria.size(); i++)
                {
                    bookshelfInfo += criteria.get(i).name() + " - " + criteriaTypes.get(i) + "\n";
                }
                
                JOptionPane.showMessageDialog(Bookroom.this, bookshelfInfo, "Shelf Info", JOptionPane.INFORMATION_MESSAGE);
                bookshelfInfo = "";
            }
            else if(e.getSource() == percentButton)
            {
                String stringPercent = percentBox.getSelectedItem().toString();
                double percent = Double.parseDouble(stringPercent);
                bookroom.setPercentRestock(percent);
                percentLabel.setText("Current Percent: " + bookroom.getPercentRestock());
            }
            else if(e.getSource() == addBookButton)
            {
                if(selectedShelf != null)
                {
                    new BookFrame();
                }
            }
            else if(e.getSource() == removeBookButton)
            {
                if(selectedShelf != null && isbnField.getText() != "")
                {
                    //selectedShelf.removeBook(book, singleShelfIndex);
                }
            }
            else if(e.getSource() == findFirstShelfButton)
            {
                if(selectedShelf != null && isbnField.getText() != "")
                {
                    long isbn = Long.parseLong(isbnField.getText());
                    bookshelfInfo += bookroom.findFirstShelfOfBook(isbn);
                    JOptionPane.showMessageDialog(Bookroom.this, bookshelfInfo, "First Shelf", JOptionPane.INFORMATION_MESSAGE);
                    bookshelfInfo = "";
                } 
            }
            else if(e.getSource() == findNumCopiesButton)
            {
                if(selectedShelf != null && isbnField.getText() != "")
                {
                    //long isbn = Long.parseLong(isbnField.getText());
                }
            }
            else if(e.getSource() == setRestockButton)
            {
                if(selectedShelf != null)
                {
                    selectedShelf.setRestock();
                }
            }
            else if(e.getSource() == checkRestockButton)
            {
                bookroom.checkForRestock(empNameField.getText());
            }
            else if(e.getSource() == signalRestockButton)
            {
                bookroom.signalRestock(empNameField.getText());
            }
        }
        
    }
    
    private class BookFrame extends JFrame
    {
        JLabel nameLabel, authorLabel, genreLabel, coverLabel,
               dateLabel, priceLabel, criteriaLabel, isbLabel;
        JTextField nameField, authorField, genreField, coverField,
                   dateField, priceField, criteriaField, isbField;
        JButton addButton;
        
        private BookFrame()
        {
            this.setSize(400, 400);
            this.setLocationRelativeTo(null);
            this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            this.setTitle("Add New Book");
            JPanel bookPanel = new JPanel();
            bookPanel.setLayout(new GridBagLayout());
            
            nameLabel = new JLabel("Name: ");
            addComp(bookPanel, nameLabel, 0, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
            nameField = new JTextField(15);
            addComp(bookPanel, nameField, 1, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
            
            authorLabel = new JLabel("Author: ");
            addComp(bookPanel, authorLabel, 0, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
            authorField = new JTextField(15);
            addComp(bookPanel, authorField, 1, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
            
            genreLabel = new JLabel("Genre: ");
            addComp(bookPanel, genreLabel, 0, 2, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
            genreField = new JTextField(15);
            addComp(bookPanel, genreField, 1, 2, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
            
            coverLabel = new JLabel("Cover: ");
            addComp(bookPanel, coverLabel, 0, 3, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
            coverField = new JTextField(15);
            addComp(bookPanel, coverField, 1, 3, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
            
            dateLabel = new JLabel("Date: ");
            addComp(bookPanel, dateLabel, 0, 4, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
            dateField = new JTextField(15);
            addComp(bookPanel, dateField, 1, 4, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
            
            priceLabel = new JLabel("Price: ");
            addComp(bookPanel, priceLabel, 0, 5, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
            priceField = new JTextField(15);
            addComp(bookPanel, priceField, 1, 5, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
           
            criteriaLabel = new JLabel("Criteria: ");
            addComp(bookPanel, criteriaLabel, 0, 6, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
            criteriaField = new JTextField(15);
            addComp(bookPanel, criteriaField, 1, 6, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
            
            isbLabel = new JLabel("ISBN: ");
            addComp(bookPanel, isbLabel, 0, 7, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
            isbField = new JTextField(15);
            addComp(bookPanel, isbField, 1, 7, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
            

            ListenForBooks lForBooks = new ListenForBooks();
            addButton = new JButton("Add Book");
            addButton.addActionListener(lForBooks);
            addComp(bookPanel, addButton, 0, 8, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
            
            this.add(bookPanel);
            this.pack();
            this.setVisible(true);
        }
        
        private class ListenForBooks implements ActionListener
        {
            public void actionPerformed(ActionEvent e)
            {
                if(e.getSource() == addButton)
                {
                    String name = nameField.getText();
                    String author = authorField.getText();
                    String genre = genreField.getText();
                    String cover = coverField.getText();
                    LocalDate date = LocalDate.parse(dateField.getText());
                    double price = Double.parseDouble(priceField.getText());
                    Criteria criteria = Criteria.valueOf(criteriaField.getText());
                    long isb = Long.parseLong(isbField.getText());
                    Book newBook = new Book(name, author, genre, cover, date, price, criteria, isb);
                    try
                    {
                            selectedShelf.addBook(newBook, 0);
                    } catch (InvalidNumberException e1)
                    {
                        e1.printStackTrace();
                    } catch (AllShelvesAreFullException e1)
                    {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }
}
