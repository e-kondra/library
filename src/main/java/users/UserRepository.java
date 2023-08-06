package users;

import java.sql.*;

public class UserRepository {
    private Connection connection;
    public UserRepository(Connection connection){
        this.connection = connection;
    }

    public ResultSet getUserByUsernameFromDB(String username)  throws SQLException {
        String sql = "SELECT * FROM users where username = ?";
        PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    public User getUserByUsernameAndPassword(String username, String password) throws SQLException {

        String sql = "select * from users where username = ? and password = ?";
        PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        return getUserFromDB(preparedStatement);
    }

    public User createNewUser(String username, String password, String firstName, String lastName, boolean isLibrarian) throws SQLException {
        String sql = "INSERT INTO users (username, password, firstname, lastname, is_librarian) VALUES (?,?,?,?,?)";
        PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        preparedStatement.setString(3, firstName);
        preparedStatement.setString(4, lastName);
        preparedStatement.setInt(5, isLibrarian ? 1 : 0);
        preparedStatement.executeUpdate();

        return getUserFromDB(preparedStatement);

    }

    public User getUserFromDB(PreparedStatement preparedStatement) throws SQLException {
        ResultSet resultSet = preparedStatement.executeQuery();

        User user = null;
        while (resultSet.next()) {
            user = new User(
                    resultSet.getInt("id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getString("firstname"),
                    resultSet.getString("lastname"),
                    resultSet.getBoolean("is_librarian")
            );
        }
        return user;
    }

    public User getUserByID(int id)  throws SQLException {
        String sql = "SELECT * FROM users where id = ?";
        PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.executeQuery();
        return getUserFromDB(preparedStatement);
    }
}

