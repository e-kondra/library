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

        this.uploadLibraryArrayList();
    }


    public void uploadLibraryArrayList() {
        try {
            ResultSet resultSet = this.libraryRepository.getLibrary();
            ArrayList<Library> library = this.setLibraryArrayList(resultSet);
            this.libraryArrayList = library;
        }catch (Exception exception) {
            this.displayMessage(exception.getMessage());
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
        return library;
    }

    public void displayBorrowedBooks(ArrayList<Library> library){

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
        try {
            this.CheckUsersCountOfBooks(currentUser);
            Book book = this.bookController.chooseBookDialog();
            this.CheckUserGetOnlyOneCopy(currentUser, book);
            this.libraryRepository.insertBorrowedBook(currentUser, book);
            this.bookController.changeBookCount(book, -1);
            this.displayMessage("You've successfully borrowed book");
        } catch (CheckException exception){
            this.displayMessage(exception.getMessage());
        } catch (Exception exception) {
            this.displayMessage(exception.getMessage());
        }

    }

    // Check User should NOT be able to borrow more than one copy of the same book
    private void CheckUserGetOnlyOneCopy(User currentUser, Book book) throws Exception {
        ArrayList<Book> userBooks = this.libraryRepository.findBooksByUser(currentUser);
        userBooks.forEach(System.out::println);
        System.out.println(book.getId());
        for(Book bo: userBooks){
            System.out.println(bo.getId());
            if(bo.getId() == book.getId()) {
                System.out.println(bo.getId() == book.getId());
                throw new CheckException("You couldn't borrow more than one copy of the same book");
            }
        }
    }

    // Check User should NOT be able to borrow more than 5 books at a time
    private void CheckUsersCountOfBooks(User currentUser) throws Exception {
        ArrayList<Book> userBooks = this.libraryRepository.findBooksByUser(currentUser);
        if (userBooks.size() >= 5){
            throw new CheckException("You DO NOT able to borrow more than 5 books at a time");
        }
    }

    protected void displayMessage(String s) {
        showMessageDialog(null, s, "Information: ", PLAIN_MESSAGE);
    }

    public void returnBook(User currentUser) {
        try {
            Book book = this.chooseUserBookDialog(currentUser);
            if (book != null) {
                this.setBookReturnDate(book, currentUser);
                this.bookController.changeBookCount(book, 1);
                this.displayMessage("You've successfully returned book");
            } else {
                this.displayMessage("You didn't choose a book");
            }
        } catch (Exception exception){
            this.displayMessage(exception.getMessage());
        }
    }

    private void setBookReturnDate(Book book, User user) throws SQLException{
        this.libraryRepository.setBookReturnDate(book, user);
        this.uploadLibraryArrayList();
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

    public void usersBorrowedBookDialog(User user) {
        try {
            ArrayList<Book> books = this.libraryRepository.findBooksByUser(user);
            this.bookController.displayBooksTable(books);
        }catch (SQLException exception){
            this.displayMessage(exception.getMessage());
        }

    }
}
