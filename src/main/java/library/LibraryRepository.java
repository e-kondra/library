package library;

import books.Book;
import users.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                "(select book_id from library where book_id=books.id and user_id = ?)";
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

    public ResultSet getLibrary() throws SQLException{
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
}
