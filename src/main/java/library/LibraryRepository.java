package library;

import books.Book;
import users.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class LibraryRepository {
    private Connection connection;

    public LibraryRepository(Connection connection){
        this.connection = connection;
    }

    public void insertBorrowedBook(User currentUser, Book book) throws SQLException {
        String sql = "INSERT INTO library (user_id, book_id) VALUES (?,?)";
        PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
        preparedStatement.setInt(1, currentUser.getId());
        preparedStatement.setInt(2, book.getId());
        preparedStatement.executeUpdate();

    }

    public ArrayList<Book> findBooksByUser(User user) throws SQLException {
        String sql = "select * from books where exists" +
                "(select book_id from library where book_id=books.id and user_id = ? and date_returning is null)";
        PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
        preparedStatement.setInt(1, user.getId());
        ResultSet resultSet = preparedStatement.executeQuery();

        ArrayList<Book> books = new ArrayList<>();
        while (resultSet.next()) {
            Book book = new Book(
                    resultSet.getInt("id"),
                    resultSet.getString("title"),
                    resultSet.getString("author"),
                    resultSet.getString("description"),
                    resultSet.getBoolean("is_active"),
                    resultSet.getInt("count"));
            books.add(book);
        }
        return books;
    }

    public ResultSet getLibraryList() throws SQLException{
        String sql = "select * from library";
        PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    public ResultSet getBorrowedBooks() throws SQLException{
        String sql = "select * from library where date_returning is null ";
        PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    public void setBookReturnDate(Book book, User user) throws SQLException{

        String sql = "UPDATE library SET date_returning = ? WHERE book_id = ? and user_id = ?";
        PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
        preparedStatement.setDate(1, Date.valueOf(LocalDate.now()));
        preparedStatement.setInt(2, book.getId());
        preparedStatement.setInt(3, user.getId());
        preparedStatement.executeUpdate();

    }
}
