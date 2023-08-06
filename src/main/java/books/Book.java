package books;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@NoArgsConstructor
@Data
public class Book {
    private int id;
    private String author;
    private String title;
    private String description;
    private Boolean isActive;
    private int count;

    public Book(String title, String author, String description, int count) {
        this.id = 1;
        this.author = author;
        this.title = title;
        this.description = description;
        this.count = count;
        this.isActive = true;
    }
    public Book(int id, String title, String author, String description, Boolean isActive, int count) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.description = description;
        this.isActive = isActive;
        this.count = count;
    }

    @Override
    public String toString() {
        return author + ". " + title + ".";
    }



}
