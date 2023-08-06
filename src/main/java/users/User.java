package users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    private int id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private Boolean isLibrarian;


    @Override
    public String toString() {
        return username + ": " + firstName + " " + lastName;
    }

}
