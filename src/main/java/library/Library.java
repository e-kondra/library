package library;

import books.Book;
import lombok.Data;
import users.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
@Data
public class Library {
    private int id;
    private User reader;
    private Book book;
    private Date dateBorrowing;
    private Date dateReturning;

    @Override
    public String toString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        return "Library{" +
                "id=" + id +
                ", reader=" + reader +
                ", book=" + book +
                ", dateBorrowing=" + dateFormat.format(dateBorrowing )+
                ", dateReturning=" + dateReturning +
                '}';
    }
}
