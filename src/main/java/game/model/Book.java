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

    private int bookId;
    private String isbn; // should check format
    private String title;
    private Genre genre;
    private int rating;
    private Date puplishedDate;
    private ArrayList<Author> authors;

    // TODO:
    // Add authors, and corresponding methods, to your implementation
    // as well, i.e. "private ArrayList<Author> authors;"

    private static final Pattern ISBN_PATTERN =
            Pattern.compile("^\\d{9}[\\d|X]$");

    public static boolean isValidIsbn(String isbn) {
        return ISBN_PATTERN.matcher(isbn).matches();
    }



    public Book(int bookId, String isbn, String title, Genre genre, int rating, Date puplishedDate)
    {
        //if(!isValidIsbn(isbn))
            //throw new IllegalArgumentException("not a valid isbn");
        this.bookId = bookId;
        this.isbn = isbn;
        this.title = title;
        this.genre = genre;
        this.rating = rating;
        this.puplishedDate = puplishedDate;

    }

    public Book( String isbn, String title,Genre genre, int rating, Date puplishedDate,ArrayList<Author> authors)
    {
        //if(!isValidIsbn(isbn))
        //throw new IllegalArgumentException("not a valid isbn");
        this.bookId = bookId;
        this.isbn = isbn;
        this.title = title;
        this.genre = genre;
        this.rating = rating;
        this.puplishedDate = puplishedDate;
        this.authors = authors;

    }

    public int getBookId() {
        return bookId;
    }

    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }

    public int getRating() {
        return rating;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    /*
    public void setAuthors(ArrayList<Author> authors) {
        this.authors = authors;
    } */

    public void setPuplishedDate(Date puplishedDate) {
        this.puplishedDate = puplishedDate;
    }

    public Date getPuplishedDate() {
        return puplishedDate;
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
                "bookId=" + bookId +
                ", isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", genre=" + genre +
                ", rating=" + rating +
                ", puplishedDate=" + puplishedDate +
                ", authors=" + authors +
                '}';
    }
}

