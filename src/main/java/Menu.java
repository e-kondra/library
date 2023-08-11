import books.BookController;
import library.LibraryController;
import users.User;
import users.UserController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.*;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import static javax.swing.JOptionPane.*;

public class Menu extends JFrame{
    protected JOptionPane optPane;
    private final JRadioButton buttonRegister = new JRadioButton("register user");
    private final JRadioButton buttonLogin = new JRadioButton("log in");
    private final JRadioButton buttonExit = new JRadioButton("exit");
    private final JRadioButton buttonAllBooks = new JRadioButton("Show all books");
    private final JRadioButton buttonSearchBooks = new JRadioButton("search book");
    private final JRadioButton buttonCreateBook = new JRadioButton("add book");
    private final JRadioButton buttonDeleteBook = new JRadioButton("remove book");
    private final JRadioButton buttonBorrowedBooks = new JRadioButton("Borrowed books");
    private final JRadioButton buttonBorrowBook = new JRadioButton("borrow book");
    private final JRadioButton buttonReturnBook = new JRadioButton("return book");
    private final JRadioButton buttonMyBorrowedBooks = new JRadioButton("My borrowed books");
    private final JRadioButton buttonLogout = new JRadioButton("logout");

    private BookController bookController;
    private UserController userController;
    private LibraryController libraryController;
    private final String url = "jdbc:mysql://localhost:3306/library";
    private final String username = "root";
    private final String password = "rooter";
    private Connection connection;
    private User currentUser;


    public Menu() {
        this.setDBConnection();
        this.currentUser = null;
        this.userController = new UserController(this.connection);
        this.bookController = new BookController(this.connection);
        this.libraryController = new LibraryController(this.connection);
    }

    public void start(){
        Object[] array;
        int choice;

        ButtonGroup setMode;
        setMode = new ButtonGroup();

        if (this.currentUser == null){
            array = this.displayNoUserMenu(setMode);
        } else {
            if (this.currentUser.getIsLibrarian()) {
                array = this.displayLibrarianMenu(setMode);
            } else {
                array = this.displayReaderMenu(setMode);
            }
        }
        choice = showConfirmDialog(null, array,
                "What would you like to do?", YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (choice == OK_OPTION){
            //Register
            if (this.buttonRegister.isSelected())
                this.currentUser = this.userController.registerUserDialog();
            //Login
            else if (this.buttonLogin.isSelected())
                this.currentUser = this.userController.loginUserDialog();
            //All books
            else if (this.buttonAllBooks.isSelected())
                this.bookController.displayAllBooks();
            //Search books
            else if (this.buttonSearchBooks.isSelected())
                this.bookController.searchBookDialog();
            //BorrowBook by Reader
            else if (this.buttonBorrowBook.isSelected())
                this.libraryController.borrowBook(this.currentUser);
            //Return book by Reader
            else if (this.buttonReturnBook.isSelected())
                this.libraryController.returnBook(this.currentUser);
            //Logout
            else if (this.buttonLogout.isSelected())
                this.currentUser = this.userController.logout();
            //add book
            else if (this.buttonCreateBook.isSelected())
                this.bookController.addBookDialog();
            // remove book
            else if (this.buttonDeleteBook.isSelected())
                this.bookController.deletingBookDialog();
            // show All Borrowed Books (for librarian)
            else if (this.buttonBorrowedBooks.isSelected())
                this.libraryController.borrowedBookDialog();
            // show users Borrowed Books (for User)
            else if (this.buttonMyBorrowedBooks.isSelected())
                this.libraryController.usersBorrowedBookDialog(this.currentUser);
            //Exit
            else if (this.buttonExit.isSelected()) {
                this.userController.exit();
                try {
                    this.connection.close();
                } catch (SQLException e){
                    JOptionPane.showMessageDialog(null,e.getMessage());
                }
            }

            setMode.clearSelection();
        }

        this.start();
    }

    private Object[] displayReaderMenu(ButtonGroup setMode) {

        buttonAllBooks.setSelected(true);
        setMode.add(buttonAllBooks);
        setMode.add(buttonSearchBooks);
        setMode.add(buttonBorrowBook);
        setMode.add(buttonReturnBook);
        setMode.add(buttonMyBorrowedBooks);
        setMode.add(buttonLogout);
        setMode.add(buttonExit);
        Object[] array = {buttonAllBooks, buttonSearchBooks, buttonMyBorrowedBooks, buttonBorrowBook,
                buttonReturnBook, buttonLogout, buttonExit};
        return array;
    }

    private Object[] displayLibrarianMenu(ButtonGroup setMode) {

        buttonAllBooks.setSelected(true);
        setMode.add(buttonAllBooks);
        setMode.add(buttonSearchBooks);
        setMode.add(buttonCreateBook);
        setMode.add(buttonDeleteBook);
        setMode.add(buttonBorrowedBooks);
        setMode.add(buttonExit);

        Object[] array = {buttonAllBooks, buttonSearchBooks,
                buttonCreateBook, buttonDeleteBook, buttonBorrowedBooks, buttonExit};
        return array;
    }

    private Object[] displayNoUserMenu(ButtonGroup setMode) {

        buttonRegister.setSelected(true);
        setMode.add(buttonRegister);
        setMode.add(buttonLogin);
        setMode.add(buttonExit);

        Object[] array = {buttonRegister, buttonLogin, buttonExit};
        return array;
    }
    private void setDBConnection(){
        try{
            PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration();
            propertiesConfiguration.load("application.properties");
            this.connection = DriverManager.getConnection(
                    propertiesConfiguration.getString("database.url") // url
                    , propertiesConfiguration.getString("database.username") //username
                    , propertiesConfiguration.getString("database.password") //password
            );
        } catch (SQLException exception){
            System.out.println("Something was wrong with DB Connection: "
                    + exception.getSQLState() + " "
                    + exception.getMessage()
            );
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

}
