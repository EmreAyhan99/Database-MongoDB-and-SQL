package game.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Representation of a book.
 *
 * @author anderslm@kth.se
 */
public class Book{

    //private int bookId;
    private String isbn; // should check format
    private String title;
    private Genre genre;
    private ArrayList<Author> authors;
    private int rating;

    // TODO:
    // Add authors, and corresponding methods, to your implementation
    // as well, i.e. "private ArrayList<Author> authors;"

    private static final Pattern ISBN_PATTERN =
            Pattern.compile("^\\d{9}[\\d|X]$");

    public static boolean isValidIsbn(String isbn) {
        return ISBN_PATTERN.matcher(isbn).matches();
    }

    public Book( String isbn, String title,Genre genre, int rating)
    {
        if(!isValidIsbn(isbn))
            throw new IllegalArgumentException("not a valid isbn");
        this.isbn = isbn;
        this.title = title;
        this.genre = genre;
        this.rating = rating;

    }


    //public int getBookId() { return bookId; }
    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }

    public int getRating() {
        return rating;
    }

    public Genre getGenre() {
        return genre;
    }
    public String genreToString(Genre genre)
    {
        if (Genre.DRAMA == genre)
        {
            return Genre.DRAMA.name().toLowerCase();
        }
        if (Genre.HOROR == genre)
        {
            return Genre.HOROR.name().toLowerCase();
        }
        if (Genre.FANTASY == genre)
        {
            return Genre.FANTASY.name().toLowerCase();
        }

        return Genre.NOGENRE.name().toLowerCase();

    }

    public ArrayList<Author> getAuthors() {
        return authors;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Book{" +
                "isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", genre=" + genre +
                ", authors=" + authors +
                ", rating=" + rating +
                '}';
    }
}

