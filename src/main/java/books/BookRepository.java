package books;

import users.User;

import java.sql.*;
import java.util.ArrayList;

public class BookRepository {

    private Connection connection;

    public BookRepository(Connection connection) {
        this.connection = connection;
    }

    public ArrayList getArrayBooks(){
        ArrayList<Book> allBooks = new ArrayList<>();
        try {
            ResultSet resultSet = this.getAllBooksFromDB();
            while (resultSet.next()){
                Book book = new Book(
                        Integer.valueOf(resultSet.getString("id")),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("description"),
                        Boolean.valueOf(resultSet.getString("is_active")),
                        resultSet.getInt("count")
                );
                allBooks.add(book);
            }
            return allBooks;
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    private ResultSet getAllBooksFromDB()  throws SQLException {
        String sql = "SELECT * FROM books where is_active = 1 order by title";
        Statement statement = this.connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        return resultSet;
    }

    public void createBook(Book book) throws SQLException {
        String sql = "INSERT INTO books (title, author, description, is_active, count) VALUES (?,?,?,?,?)";
        PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
        preparedStatement.setString(1, book.getTitle());
        preparedStatement.setString(2, book.getAuthor());
        preparedStatement.setString(3, book.getDescription());
        preparedStatement.setInt(4, 1);
        preparedStatement.setInt(5, book.getCount());

        preparedStatement.executeUpdate();
    }

    public void deleteBook(Book book) throws SQLException {
        String sql = "UPDATE books SET is_active = 0 WHERE id = ?";
        PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
        preparedStatement.setInt(1, book.getId());

        preparedStatement.executeUpdate();
    }

    private ResultSet findBookById(int id) throws SQLException{

        String sql = "SELECT * from books where id = ?";
        PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    public void changeBookCount(Book book, int increment) throws SQLException{
        int count = 0;
        ResultSet resultSet = this.findBookById(book.getId());
        while (resultSet.next()) {
            count = resultSet.getInt("count");
            System.out.println(count);
        }
        count += increment;
        System.out.println(count);
        String sql = "UPDATE books SET count = ? WHERE id = ?";
        PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
        preparedStatement.setInt(1, count);
        preparedStatement.setInt(2, book.getId());
        preparedStatement.executeUpdate();

        book.setCount(count);
    }

    public void findBooksByUser(User user) {

    }
}
