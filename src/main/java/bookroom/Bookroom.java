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
            getSpecificShelfButton, allWrongBooksButton, copiesOfBookButton/*, sortShelfButton*/; //not sure about selecting but w/e i'll work on it
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
        removeBookButton.addActionListener(lForButtons);
        findFirstShelfButton = new JButton("Find Shelf of First Book Instance");
        findFirstShelfButton.addActionListener(lForButtons);
        addComp(shelfPanel, findFirstShelfButton, 1, 3, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        findNumCopiesButton = new JButton("Find # of Copies of Book");
        findNumCopiesButton.addActionListener(lForButtons);
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
        changeCriteriaButton.addActionListener(lForButtons);
        addComp(selectedPanel, changeCriteriaButton, 0, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        changeCriteriaTypeButton = new JButton("Change Sub-Classification");
        changeCriteriaTypeButton.addActionListener(lForButtons);
        addComp(selectedPanel, changeCriteriaTypeButton, 1, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        shelvesLabel = new JLabel("Shelf #: ");
        addComp(selectedPanel, shelvesLabel, 2, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        shelvesField = new JTextField(2);
        addComp(selectedPanel, shelvesField, 2, 0, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE);
        getSpecificShelfButton = new JButton("Get Contents of Single Shelf");
        getSpecificShelfButton.addActionListener(lForButtons);
        addComp(selectedPanel, getSpecificShelfButton, 3, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        
        booksOnShelfButton = new JButton("# of Books on Single Shelf");
        booksOnShelfButton.addActionListener(lForButtons);
        addComp(selectedPanel, booksOnShelfButton, 0, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        getShelfIsFullButton = new JButton("Check if Single Shelf is Full");
        getShelfIsFullButton.addActionListener(lForButtons);
        addComp(selectedPanel, getShelfIsFullButton, 1, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        toggleShelfFullButton = new JButton("Mark Shelf as Full/Not Full");
        toggleShelfFullButton.addActionListener(lForButtons);
        addComp(selectedPanel, toggleShelfFullButton, 2, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        addBookButton = new JButton("Add Book to Shelf");
        addBookButton.addActionListener(lForButtons);
        addComp(selectedPanel, addBookButton, 3, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        
        allWrongBooksButton = new JButton("Find Incorrect Books");
        allWrongBooksButton.addActionListener(lForButtons);
        addComp(selectedPanel, allWrongBooksButton, 1, 2, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        copiesOfBookButton = new JButton("Find # Copies of Book on Shelf");
        copiesOfBookButton.addActionListener(lForButtons);
        addComp(selectedPanel, copiesOfBookButton, 2, 2, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        //there's something screwy going on with how collections.sort is sorting stuff, that isn't
        //happening with the test cases, so I don't even know.
        //sortShelfButton = new JButton("Sort Shelf");
        //sortShelfButton.addActionListener(lForButtons);
        //addComp(selectedPanel, sortShelfButton, 3, 2, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        
        setRestockButton = new JButton("Mark Shelf for Restock");
        setRestockButton.addActionListener(lForButtons);
        addComp(selectedPanel, setRestockButton, 0, 2, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        
        addComp(thePanel, selectedPanel, 0, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.NONE);
        
        JPanel analyticsPanel = new JPanel();
        Border analyticsBorder = BorderFactory.createTitledBorder("Analytics");
        analyticsPanel.setBorder(analyticsBorder);
        analyticsPanel.setLayout(new GridBagLayout());
        
        readFileButton = new JButton("Read Restock File");
        readFileButton.addActionListener(lForButtons);
        addComp(analyticsPanel, readFileButton, 0, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        nameLabel = new JLabel("Name: ");
        addComp(analyticsPanel, nameLabel, 1, 0, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE);
        nameField = new JTextField(15);
        addComp(analyticsPanel, nameField, 2, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        

        restockerButton = new JButton("Restocker Numbers");
        restockerButton.addActionListener(lForButtons);
        addComp(analyticsPanel, restockerButton, 0, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        checkerButton = new JButton("Checker Numbers");
        checkerButton.addActionListener(lForButtons);
        addComp(analyticsPanel, checkerButton, 1, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        checkRestockerButton = new JButton("Check For Restocker");
        checkRestockerButton.addActionListener(lForButtons);
        addComp(analyticsPanel, checkRestockerButton, 2, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        checkCheckerButton = new JButton("Check for Checker");
        checkCheckerButton.addActionListener(lForButtons);
        addComp(analyticsPanel, checkCheckerButton, 3, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        
        frequentRestockerButton = new JButton("Most Frequent Restocker");
        frequentRestockerButton.addActionListener(lForButtons);
        addComp(analyticsPanel, frequentRestockerButton, 0, 2, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        frequentCheckerButton = new JButton("Most Frequent Checker");
        frequentCheckerButton.addActionListener(lForButtons);
        addComp(analyticsPanel, frequentCheckerButton, 1, 2, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        averageRestockButton = new JButton("Get Average Restock Time");
        averageRestockButton.addActionListener(lForButtons);
        addComp(analyticsPanel, averageRestockButton, 2, 2, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        averageCheckButton = new JButton("Get Average Check Time");
        averageCheckButton.addActionListener(lForButtons);
        addComp(analyticsPanel, averageCheckButton, 3, 2, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        
        
        frequentRestockDayButton = new JButton("Most Frequent Restock Day");
        frequentRestockDayButton.addActionListener(lForButtons);
        addComp(analyticsPanel, frequentRestockDayButton, 0, 3, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        frequentRestockMonthButton = new JButton("Most Frequent Restock Month");
        frequentRestockMonthButton.addActionListener(lForButtons);
        addComp(analyticsPanel, frequentRestockMonthButton, 1, 3, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        frequentCheckDayButton = new JButton("Most Frequent Check Day");
        frequentCheckDayButton.addActionListener(lForButtons);
        addComp(analyticsPanel, frequentCheckDayButton, 2, 3, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        frequentCheckMonthButton = new JButton("Most Frequent Check Month");
        frequentCheckMonthButton.addActionListener(lForButtons);
        addComp(analyticsPanel, frequentCheckMonthButton, 3, 3, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        
        totalRestocksButton = new JButton("Get Total Restocks");
        totalRestocksButton.addActionListener(lForButtons);
        addComp(analyticsPanel, totalRestocksButton, 0, 4, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        totalChecksButton = new JButton("Get Total Checks");
        totalChecksButton.addActionListener(lForButtons);
        addComp(analyticsPanel, totalChecksButton, 1, 4, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        //not sure about these two things, but
        revenueLabel = new JLabel("Get Specific Restock:");
        addComp(analyticsPanel, revenueLabel, 2, 4, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
        revenueField = new JTextField(2);
        addComp(analyticsPanel, revenueField, 2, 4, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE);
        getRevenueButton = new JButton("Retrieve Revenue");
        getRevenueButton.addActionListener(lForButtons);
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
                if(selectedShelf != null && !isbnField.getText().equals(""))
                {
                    long isbn = Long.parseLong(isbnField.getText());
                    try
                    {
                        selectedShelf.removeBook(isbn);
                    } catch (BookDoesNotExistException e1)
                    {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
            }
            else if(e.getSource() == findFirstShelfButton)
            {
                if(selectedShelf != null && !isbnField.getText().equals(""))
                {
                    long isbn = Long.parseLong(isbnField.getText());
                    bookshelfInfo += bookroom.findFirstShelfOfBook(isbn);
                    JOptionPane.showMessageDialog(Bookroom.this, bookshelfInfo, "First Shelf", JOptionPane.INFORMATION_MESSAGE);
                    bookshelfInfo = "";
                } 
            }
            else if(e.getSource() == findNumCopiesButton)
            {
                if(selectedShelf != null && !isbnField.getText().equals(""))
                {
                    long isbn = Long.parseLong(isbnField.getText());
                    int copies = selectedShelf.getNumCopiesOfBook(isbn);
                    bookshelfInfo += "Number of Copies: " + String.valueOf(copies);
                    JOptionPane.showMessageDialog(Bookroom.this, bookshelfInfo, "Number of Copies", JOptionPane.INFORMATION_MESSAGE);
                    bookshelfInfo = "";
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
                if(!empNameField.getText().equals(""))
                {
                    bookroom.checkForRestock(empNameField.getText());
                }
            }
            else if(e.getSource() == signalRestockButton)
            {
                if(!empNameField.getText().equals(""))
                {
                    bookroom.signalRestock(empNameField.getText());
                }
            }
            else if(e.getSource() == changeCriteriaButton)
            {
                if(selectedShelf != null)
                {
                    new CriteriaFrame();
                }
            }
            else if(e.getSource() == changeCriteriaTypeButton)
            {
                if(selectedShelf != null)
                {
                    new CriteriaTypeFrame();
                }
            }
            else if(e.getSource() == booksOnShelfButton)
            {
                if(selectedShelf != null && !shelvesField.getText().equals(""))
                {
                    try
                    {
                        bookshelfInfo += "Number of Books: " + selectedShelf.getNumBooksOnShelf(Integer.parseInt(shelvesField.getText()));
                    } catch (NumberFormatException e1)
                    {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    } catch (InvalidNumberException e1)
                    {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    JOptionPane.showMessageDialog(Bookroom.this, bookshelfInfo, "Number of Books", JOptionPane.INFORMATION_MESSAGE);
                    bookshelfInfo = "";
                }
            }
            else if(e.getSource() == getShelfIsFullButton)
            {
                if(!shelvesField.getText().equals(""))
                {
                    try
                    {
                        bookshelfInfo += "Shelf is Full: " + selectedShelf.getShelfIsFull(Integer.parseInt(shelvesField.getText()));
                    } catch (InvalidNumberException e1)
                    {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    JOptionPane.showMessageDialog(Bookroom.this, bookshelfInfo, "Is Shelf Full", JOptionPane.INFORMATION_MESSAGE);
                    bookshelfInfo = "";
                }
            }
            else if(e.getSource() == toggleShelfFullButton)
            {
                if(!shelvesField.getText().equals(""))
                {
                    try
                    {
                        selectedShelf.toggleShelfFull(Integer.parseInt(shelvesField.getText()));
                    } catch (NumberFormatException e1)
                    {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    } catch (InvalidNumberException e1)
                    {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
            }
            else if(e.getSource() == getSpecificShelfButton)
            {
                if(!shelvesField.getText().equals(""))
                {
                    List<Book> shelf = selectedShelf.getSingularShelf(Integer.parseInt(shelvesField.getText()));
                    for(Book book : shelf)
                    {
                        bookshelfInfo += book.getName() + ", " + book.getAuthor() + "; Price: " + book.getPrice() + "\n";
                    }
                    
                    JOptionPane.showMessageDialog(Bookroom.this, bookshelfInfo, "Bookshelf Contents", JOptionPane.INFORMATION_MESSAGE);
                    bookshelfInfo = "";
                }
            }
            else if(e.getSource() == allWrongBooksButton)
            {
                if(!shelvesField.getText().equals(""))
                {
                    List<Book> shelf = selectedShelf.findAllWrongBooks();
                    for(Book book : shelf)
                    {
                        bookshelfInfo += book.getName() + ", " + book.getAuthor() + "\n";
                    }
                    
                    JOptionPane.showMessageDialog(Bookroom.this, bookshelfInfo, "Wrongly Classified Books", JOptionPane.INFORMATION_MESSAGE);
                    bookshelfInfo = "";
                }
            }
            else if(e.getSource() == copiesOfBookButton)
            {
                if(!isbnField.getText().equals(""))
                {
                    bookshelfInfo += selectedShelf.getNumCopiesOfBook(Long.parseLong(isbnField.getText()));
                    
                    JOptionPane.showMessageDialog(Bookroom.this, bookshelfInfo, "Bookshelf Contents", JOptionPane.INFORMATION_MESSAGE);
                    bookshelfInfo = "";
                }
            }
            /*
            else if(e.getSource() == sortShelfButton)
            {
                if(selectedShelf != null && !shelvesField.getText().equals(""))
                {
                    try
                    {
                        selectedShelf.sortAllShelves();
                    } catch (InvalidNumberException e1)
                    {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
            }*/
            else if(e.getSource() == readFileButton)
            {
                analyzer.readRestockFile();
            }
            else if(e.getSource() == restockerButton)
            {
                if(analyzer.checkIfFileIsRead())
                {
                    int numbers = analyzer.getRestockNumbers(nameField.getText());
                    bookshelfInfo += "Number of Restocks by this Employee: " + String.valueOf(numbers);
                    JOptionPane.showMessageDialog(Bookroom.this, bookshelfInfo, "Restocker Numbers", JOptionPane.INFORMATION_MESSAGE);
                    bookshelfInfo = "";
                }
            }
            else if(e.getSource() == checkerButton)
            {
                if(analyzer.checkIfFileIsRead())
                {
                    int numbers = analyzer.getCheckerNumbers(nameField.getText());
                    bookshelfInfo += "Number of Checks by this Employee: " + String.valueOf(numbers);
                    JOptionPane.showMessageDialog(Bookroom.this, bookshelfInfo, "Checker Numbers", JOptionPane.INFORMATION_MESSAGE);
                    bookshelfInfo = "";
                }
            }
            else if(e.getSource() == checkRestockerButton)
            {
                if(analyzer.checkIfFileIsRead())
                {
                    boolean hasRestocked = analyzer.checkForRestocker(nameField.getText());
                    bookshelfInfo += "Has Employee Restocked Since File Clear: " + String.valueOf(hasRestocked);
                    JOptionPane.showMessageDialog(Bookroom.this, bookshelfInfo, "Has Restocked", JOptionPane.INFORMATION_MESSAGE);
                    bookshelfInfo = "";
                }
            }
            else if(e.getSource() == checkCheckerButton)
            {
                if(analyzer.checkIfFileIsRead())
                {
                    boolean hasChecked = analyzer.checkForChecker(nameField.getText());
                    bookshelfInfo += "Has Employee Checked Since File Clear: " + String.valueOf(hasChecked);
                    JOptionPane.showMessageDialog(Bookroom.this, bookshelfInfo, "Has Checked", JOptionPane.INFORMATION_MESSAGE);
                    bookshelfInfo = "";
                }
            }
            else if(e.getSource() == frequentRestockerButton)
            {
                if(analyzer.checkIfFileIsRead())
                {
                    bookshelfInfo += "Most Frequent Restocker: " + analyzer.findMostFrequentRestocker();
                    JOptionPane.showMessageDialog(Bookroom.this, bookshelfInfo, "Most FrequentRestocker", JOptionPane.INFORMATION_MESSAGE);
                    bookshelfInfo = "";
                }
            }
            else if(e.getSource() == frequentCheckerButton)
            {
                if(analyzer.checkIfFileIsRead())
                {
                    bookshelfInfo += "Most Frequent Checker: " + analyzer.findMostFrequentChecker();
                    JOptionPane.showMessageDialog(Bookroom.this, bookshelfInfo, "Most Frequent Checker", JOptionPane.INFORMATION_MESSAGE);
                    bookshelfInfo = "";
                }
            }
            else if(e.getSource() == averageRestockButton)
            {
                if(analyzer.checkIfFileIsRead())
                {
                    bookshelfInfo += "Average Restock Time: " + analyzer.findAverageRoomRestockedTime();
                    JOptionPane.showMessageDialog(Bookroom.this, bookshelfInfo, "Average Restock Time", JOptionPane.INFORMATION_MESSAGE);
                    bookshelfInfo = "";
                }
            }
            else if(e.getSource() == averageCheckButton)
            {
                if(analyzer.checkIfFileIsRead())
                {
                    bookshelfInfo += "Average Check Time: " + analyzer.findAverageRestockSignalTime();
                    JOptionPane.showMessageDialog(Bookroom.this, bookshelfInfo, "Average Check Time", JOptionPane.INFORMATION_MESSAGE);
                    bookshelfInfo = "";
                }
            }
            else if(e.getSource() == frequentRestockDayButton)
            {
                if(analyzer.checkIfFileIsRead())
                {
                    bookshelfInfo += "Most Frequent Restock Day: " + analyzer.checkMostFrequentDayRestocked();
                    JOptionPane.showMessageDialog(Bookroom.this, bookshelfInfo, "Most Frequent Restock Day", JOptionPane.INFORMATION_MESSAGE);
                    bookshelfInfo = "";
                }
            }
            else if(e.getSource() == frequentRestockMonthButton)
            {
                if(analyzer.checkIfFileIsRead())
                {
                    bookshelfInfo += "Most Frequent Restock Month: " + analyzer.checkMostFrequentMonthRestocked();
                    JOptionPane.showMessageDialog(Bookroom.this, bookshelfInfo, "Most Frequent Restock Month", JOptionPane.INFORMATION_MESSAGE);
                    bookshelfInfo = "";
                }
            }
            else if(e.getSource() == frequentCheckDayButton)
            {
                if(analyzer.checkIfFileIsRead())
                {
                    bookshelfInfo += "Most Frequent Check Day: " + analyzer.checkMostFrequentDayChecked();
                    JOptionPane.showMessageDialog(Bookroom.this, bookshelfInfo, "Most Frequent Check Day", JOptionPane.INFORMATION_MESSAGE);
                    bookshelfInfo = "";
                }
            }
            else if(e.getSource() == frequentCheckMonthButton)
            {
                if(analyzer.checkIfFileIsRead())
                {
                    bookshelfInfo += "Most Frequent Check Month: " + analyzer.checkMostFrequentMonthChecked();
                    JOptionPane.showMessageDialog(Bookroom.this, bookshelfInfo, "Most Frequent Check Month", JOptionPane.INFORMATION_MESSAGE);
                    bookshelfInfo = "";
                }
            }
            else if(e.getSource() == totalRestocksButton)
            {
                if(analyzer.checkIfFileIsRead())
                {
                    bookshelfInfo += "Total Restocks: " + analyzer.findTotalNumberOfRestocks();
                    JOptionPane.showMessageDialog(Bookroom.this, bookshelfInfo, "Total Restocks", JOptionPane.INFORMATION_MESSAGE);
                    bookshelfInfo = "";
                }
            }
            else if(e.getSource() == totalChecksButton)
            {
                if(analyzer.checkIfFileIsRead())
                {
                    bookshelfInfo += "Total Checks: " + analyzer.findTotalNumberOfChecks();
                    JOptionPane.showMessageDialog(Bookroom.this, bookshelfInfo, "Total Checks", JOptionPane.INFORMATION_MESSAGE);
                    bookshelfInfo = "";
                }
            }
            else if(e.getSource() == getRevenueButton)
            {
                if(analyzer.checkIfFileIsRead())
                {
                    if(!revenueField.getText().equals("") && (Integer.parseInt(revenueField.getText()) == 0 || Integer.parseInt(revenueField.getText()) % 2 == 0))
                    {
                        bookshelfInfo += "Revenue from Selected Restock: " + analyzer.getGrossRevenue(Integer.parseInt(revenueField.getText()));
                        JOptionPane.showMessageDialog(Bookroom.this, bookshelfInfo, "Revenue", JOptionPane.INFORMATION_MESSAGE);
                        bookshelfInfo = "";
                    }
                }
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
                            selectedShelf.addBook(newBook, Integer.parseInt(shelvesField.getText()));
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
    
    private class CriteriaFrame extends JFrame
    {
        JTextField criteriaField;
        JButton changeButton;
        JLabel newLabel;
        
        private CriteriaFrame()
        {
            this.setSize(400, 400);
            this.setLocationRelativeTo(null);
            this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            this.setTitle("Change Classification");
            JPanel criteriaPanel = new JPanel();
            criteriaPanel.setLayout(new GridBagLayout());
            
            newLabel = new JLabel("Set New Main Classification: ");
            addComp(criteriaPanel, newLabel, 0, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
            
            criteriaField = new JTextField(10);
            addComp(criteriaPanel, criteriaField, 0, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
            
            ListenForCriteria l = new ListenForCriteria();
            changeButton = new JButton("Confirm");
            changeButton.addActionListener(l);
            addComp(criteriaPanel, changeButton, 0, 2, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
            
            
            this.add(criteriaPanel);
            this.pack();
            this.setVisible(true);
        }
        
        private class ListenForCriteria implements ActionListener
        {

            public void actionPerformed(ActionEvent e)
            {
                if(e.getSource() == changeButton)
                {
                    selectedShelf.setCriteria(Criteria.valueOf(criteriaField.getText()));
                }
            }
            
        }
    }
    
    private class CriteriaTypeFrame extends JFrame
    {
        JTextField criteriaField;
        JButton changeButton;
        JLabel newLabel;
        
        private CriteriaTypeFrame()
        {
            this.setSize(400, 400);
            this.setLocationRelativeTo(null);
            this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            this.setTitle("Change Classification");
            JPanel criteriaPanel = new JPanel();
            criteriaPanel.setLayout(new GridBagLayout());
            
            newLabel = new JLabel("Set New Sub-Classification: ");
            addComp(criteriaPanel, newLabel, 0, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
            
            criteriaField = new JTextField(10);
            addComp(criteriaPanel, criteriaField, 0, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
            
            ListenForCriteria l = new ListenForCriteria();
            changeButton = new JButton("Confirm");
            changeButton.addActionListener(l);
            addComp(criteriaPanel, changeButton, 0, 2, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
            
            
            this.add(criteriaPanel);
            this.pack();
            this.setVisible(true);
        }
        
        private class ListenForCriteria implements ActionListener
        {

            public void actionPerformed(ActionEvent e)
            {
                if(e.getSource() == changeButton)
                {
                    selectedShelf.setCriteriaType(criteriaField.getText());
                }
            }
            
        }
    }
}
