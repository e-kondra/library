package books;

import users.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static javax.swing.JOptionPane.PLAIN_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;

public class BookController {
    protected JOptionPane optPane;
    public BookRepository bookRepository;
    private ArrayList<Book> books;


    public BookController(Connection connection){
        try {
            this.bookRepository = new BookRepository(connection);
            this.books = bookRepository.getArrayBooks();

        } catch (Exception e){
            displayMessage(e.getMessage());
        }
    }

    private void updateBookArray(){
        this.books = bookRepository.getArrayBooks();
    }
    public void deletingBookDialog() {
        Book book = this.chooseBookDialog();
        if (book != null) {
            try {
                this.bookRepository.deleteBook(book);
                this.updateBookArray();
                this.displayMessage(book + " Was successfully deleted");
            } catch (SQLException exception) {
                this.displayMessage("Something was wrong with deleting book:\n " + exception.getMessage());
            }
        } else {
            return;
        }

    }

    public void displayAllBooks(){
        this.updateBookArray();
        this.displayBooksTable(this.books);
    }
    public void displayBooksTable(ArrayList<Book> someBooks) {

        String[] columnNames = { "Title", "Author", "Available", "Description"};
        String[][] data = new String[someBooks.size()][4];
        for (int row = 1; row <= someBooks.size(); row++) {
            Book book = someBooks.get(row-1);
            data[row-1][0] = book.getTitle();
            data[row-1][1] = book.getAuthor();
            data[row-1][2] = Integer.toString(book.getCount());
            data[row-1][3] = book.getDescription();
        }

        JFrame frame = new JFrame();
        JTable table = new JTable(data, columnNames);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(table, BorderLayout.CENTER);
        tablePanel.add(table.getTableHeader(), BorderLayout.NORTH);

        frame.add(tablePanel);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setSize(600,400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void addBookDialog() {
        JTextField fieldAuthor= new JTextField();
        JTextField fieldTitle = new JTextField();
        JTextField fieldDescription = new JTextField();
        JTextField fieldCount = new JTextField();

        Object[] message = {
                "Title:", fieldTitle,
                "Author:", fieldAuthor,
                "Description", fieldDescription,
                "Count", fieldCount
        };

        int option = JOptionPane.showConfirmDialog(null, message,
                "Please, fill out all fields", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION){
            try {
                // create book and put it in DB
                Book book = new Book( fieldTitle.getText(), fieldAuthor.getText(),
                        fieldDescription.getText(), Integer.parseInt(fieldCount.getText()));
                this.bookRepository.createBook(book);
                this.updateBookArray();
                this.displayMessage("A book: " + book + " was added successfully");
            } catch (Exception e) {
                this.displayMessage("Something was wrong with creating book "
                        + fieldAuthor.getText() + ". " + fieldTitle.getText());
            }
        } else {
            this.displayMessage("You canceled creation");
        }
    }

    public void searchBookDialog() {
        String detail = JOptionPane.showInputDialog("Please enter a title or author for searching");
        ArrayList<Book> foundBooks = this.findBooksByDetail(detail);
        if (foundBooks.isEmpty()){
            this.displayMessage("Sorry, we did not find books on your request,\n please, try again ");
            return;
        }
        this.displayBooksTable(foundBooks);
    }

    public ArrayList findBooksByDetail(String detail){
        ArrayList<Book> foundBooks = new ArrayList<>();
        for(Book book: this.books){
            if (book.getAuthor().toLowerCase().trim().contains(detail.toLowerCase().trim())
                || book.getTitle().toLowerCase().trim().contains(detail.toLowerCase().trim())) {
                foundBooks.add(book);
            }
        }
        return foundBooks;
    }
    protected void displayMessage(String s) {
        showMessageDialog(null, s, "Information: ", PLAIN_MESSAGE);
    }

    // This method gets the selected value from the option pane and resets the
    // value to null, so we can use it again.
    protected String getOptionPaneValue() {

        // Get the result . . .
        Object o = optPane.getInputValue();
        String s = "<Unknown>";
        if (o != null)
            s = (String) o;

        Object val = optPane.getValue(); // which button?

        // Check for cancel button or closed option
        if (val != null) {
            if (val instanceof Integer) {
                int intVal = ((Integer) val).intValue();
                if ((intVal == JOptionPane.CANCEL_OPTION)
                        || (intVal == JOptionPane.CLOSED_OPTION))
                    s = "<Cancel>";
            }
        }
        optPane.setValue("");
        optPane.setInitialValue("X");
        optPane.setInitialValue("");

        return s;
    }

    public Book chooseBookDialog() {
        String detail = JOptionPane.showInputDialog("Please enter a title or author for searching");
        ArrayList<Book> foundBooks = this.findBooksByDetail(detail);

        Book selectedBook = (Book) JOptionPane.showInputDialog(null,
                "Select the book to borrow",
                "Select book",
                JOptionPane.QUESTION_MESSAGE,
                null,
                foundBooks.toArray(),
                foundBooks.toArray()[0]);
        return selectedBook;
    }

    public void changeBookCount(Book book, int increment) {
        try {
            this.bookRepository.changeBookCount(book, increment);
            this.updateBookArray();
        }catch (SQLException exception){
            this.displayMessage(exception.getMessage());
        }
    }

    public Book getBookById(int id){
        try{
            ResultSet resultSet = this.bookRepository.findBookById(id);
            Book book = this.bookRepository.getBookFromDB(resultSet);
            return book;
        } catch (SQLException exception){
            this.displayMessage(exception.getMessage());
            return null;
        }
    }

}
