package library;

import books.Book;
import books.BookController;
import users.User;
import users.UserController;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static javax.swing.JOptionPane.PLAIN_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;

public class LibraryController {
    private LibraryRepository libraryRepository;
    private BookController bookController;
    private UserController userController;
    private ArrayList<Library> libraryArrayList;
    public LibraryController(Connection connection){
        this.libraryRepository = new LibraryRepository(connection);
        this.bookController = new BookController(connection);
        this.userController = new UserController(connection);

        this.libraryArrayList = this.getAllLibraryHistory();
    }

    public ArrayList<Library> getAllLibraryHistory() {
        try {
            ResultSet resultSet = this.libraryRepository.getLibrary();
            ArrayList<Library> library = this.setLibraryArrayList(resultSet);
            return library;
        }catch (Exception exception) {
            this.displayMessage(exception.getMessage());
            return null;
        }
    }
    public ArrayList<Library> getBorrowedBookList() {
        try {
            ResultSet resultSet = this.libraryRepository.getBorrowedBooks();
            ArrayList<Library> library = this.setLibraryArrayList(resultSet);
            return library;
        }catch (Exception exception) {
            this.displayMessage(exception.getMessage());
            return null;
        }
    }
    private ArrayList<Library> setLibraryArrayList(ResultSet resultSet) throws SQLException{
        ArrayList<Library> library = new ArrayList<>();
        while (resultSet.next()) {
            Library borrowedBook = new Library();
            borrowedBook.setId(resultSet.getInt("id"));
            borrowedBook.setReader(this.userController
                            .getUserById(resultSet.getInt("user_id")));
            borrowedBook.setBook(this.bookController
                    .getBookById(resultSet.getInt("book_id")));
            borrowedBook.setDateBorrowing(resultSet.getDate("date_borrowing"));
            borrowedBook.setDateReturning(resultSet.getDate("date_returning"));
            library.add(borrowedBook);
        }
        library.forEach(System.out::println);
        System.out.println("---------");
        return library;
    }

    public void displayBorrowedBooks(ArrayList<Library> library){
        library.forEach(System.out::println);
        String[] columnNames = { "Reader", "Book", "DateBorrowing"};
        String[][] data = new String[library.size()][4];
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (int row = 1; row <= library.size(); row++) {
            Library borrowedBook = library.get(row-1);
            data[row-1][0] = borrowedBook.getReader().toString();
            data[row-1][1] = borrowedBook.getBook().toString();
            data[row-1][2] = dateFormat.format(borrowedBook.getDateBorrowing());
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

    public void borrowBook(User currentUser) {
        Book book = this.bookController.chooseBookDialog();
        try {
            this.libraryRepository.insertBorrowedBook(currentUser, book);
            this.bookController.changeBookCount(book, -1);
            this.displayMessage("You've successfully borrowed book");
        } catch (SQLException exception){
            this.displayMessage(exception.getMessage());
        }
    }
    protected void displayMessage(String s) {
        showMessageDialog(null, s, "Information: ", PLAIN_MESSAGE);
    }

    public void returnBook(User currentUser) {
        try {
            Book book = this.chooseUserBookDialog(currentUser);
            this.bookController.changeBookCount(book, 1);
        } catch (Exception exception){
            this.displayMessage(exception.getMessage());
        }
    }

    public Book chooseUserBookDialog(User user) throws SQLException{

        ArrayList<Book> foundBooks = this.findBooksByUser(user);

        Book selectedBook = (Book) JOptionPane.showInputDialog(null,
                "Select the book to borrow",
                "Select book",
                JOptionPane.QUESTION_MESSAGE,
                null,
                foundBooks.toArray(),
                foundBooks.toArray()[0]);
        return selectedBook;
    }

    private ArrayList<Book> findBooksByUser(User user) throws SQLException{
        return this.libraryRepository.findBooksByUser(user);
    }

    public void borrowedBookDialog() {
        ArrayList<Library> library =  this.getBorrowedBookList();
        this.displayBorrowedBooks(library);
    }
}
