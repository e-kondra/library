package library;

import books.Book;
import books.BookController;
import users.User;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import static javax.swing.JOptionPane.PLAIN_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;

public class LibraryController {
    private LibraryRepository libraryRepository;
    private BookController bookController;
    public LibraryController(Connection connection){
        this.libraryRepository = new LibraryRepository(connection);
        this.bookController = new BookController(connection);
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
            bookController.changeBookCount(book, 1);
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
}
