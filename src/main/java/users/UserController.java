package users;


import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;

import static javax.swing.JOptionPane.showMessageDialog;

public class UserController {
    protected JOptionPane optPane;
    private UserRepository userRepository;


    public UserController(Connection connection){
        this.userRepository = new UserRepository(connection);
    }
    public User registerUserDialog() {

        JTextField fieldUsername= new JTextField();
        JTextField fieldPassword = new JTextField();
        JTextField fieldFirstName = new JTextField();
        JTextField fieldLastName = new JTextField();

        Object[] message = {
                "Username:", fieldUsername,
                "Password:", fieldPassword,
                "FirstName", fieldFirstName,
                "LastName", fieldLastName,
        };

        int option = JOptionPane.showConfirmDialog(null, message,
                "Please, fill out all fields", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                User user = this.registerUser(fieldUsername.getText(), fieldPassword.getText(), fieldFirstName.getText(), fieldLastName.getText(), false);
                return user;
            }catch (UserCreatingException e){
                this.displayMessage( e.getMessage());
                return null;
            }catch (Exception e) {
                this.displayMessage("Something was wrong with creating user " + e.getMessage());
                return null;
            }
        } else {
            this.displayMessage("You canceled creation");
            return null;
        }
    }

    private User registerUser(String username, String password, String firstName, String lastName, boolean isLibrarian) throws Exception {
        User user = null;
        this.checkUserRegister(username, password, firstName, lastName);
        return this.userRepository.createNewUser(username,password, firstName, lastName, isLibrarian);
    }

    private void checkUserRegister(String username, String password, String firstName, String lastName) throws Exception{
        if (username.isBlank() || username == null
                || password.isBlank() || password == null
                || firstName.isBlank() || firstName == null
                || lastName.isBlank() || lastName == null){
            throw new UserCreatingException("Fields Username, Password, First name, Last name must be filled in");
        }
        this.userRepository.checkUserByUsername(username);
    }

    private void displayMessage(String message){
        JOptionPane.showMessageDialog(null, message);
    }


    public User loginUserDialog() {
        try {
            JTextField field1 = new JTextField();
            JTextField field2 = new JTextField();

            Object[] message = {
                    "Username:", field1,
                    "Password:", field2,
            };
            int option = JOptionPane.showConfirmDialog(null, message,
                    "Please, fill out all fields", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                String username = field1.getText();
                String password = field2.getText();
                User user = this.userRepository.getUserByUsernameAndPassword(username, password);
                this.displayMessage("You've successfully logged in");
                return user;
            } else{
                this.displayMessage("You've canceled logging in");
                return null;
            }
        }catch (Exception exception){
            this.displayMessage(exception.getMessage());
            return null;
        }
    }

    public void exit() {
        this.displayMessage("See you!");
        System.exit(0);
    }

    public User logout() {
        this.displayMessage("You successfully logged out!");
        return null;
    }

    public User getUserById(int id){
        try{
            User user = this.userRepository.getUserByID(id);
            return user;
        } catch (SQLException exception){
            this.displayMessage(exception.getMessage());
            return null;
        }
    }
}
