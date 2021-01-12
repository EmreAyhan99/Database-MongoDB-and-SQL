package game.model;

import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Representation of a book.
 *
 * @author anderslm@kth.se, Emre Ayhan
 */
public class Book{

    private ObjectId bookId;
    private String isbn; // should check format
    private String title;
    private Genre genre;
    private int rating;
    private LocalDate puplishedDate;
    private ArrayList<Author> authors;

    private static final Pattern ISBN_PATTERN =
            Pattern.compile("^\\d{9}[\\d|X]$");

    /**
     * Returns if isbn is valid
     * @param isbn
     */
    public static boolean isValidIsbn(String isbn) {
        return ISBN_PATTERN.matcher(isbn).matches();
    }

    /**
     * Constructs a book
     * @param bookId
     * @param isbn
     * @param title
     * @param genre
     * @param rating
     * @param puplishedDate
     */
    public Book(ObjectId bookId, String isbn, String title, Genre genre, int rating, LocalDate puplishedDate)
    {
        this.bookId = bookId;
        this.isbn = isbn;
        this.title = title;
        this.genre = genre;
        this.rating = rating;
        this.puplishedDate = puplishedDate;
    }

    /**
     * Constructs a book
     * @param isbn
     * @param title
     * @param genre
     * @param rating
     * @param puplishedDate
     * @param authors
     */
    public Book(String isbn, String title, Genre genre, int rating, LocalDate puplishedDate, ArrayList<Author> authors)
    {
        this.bookId = bookId;
        this.isbn = isbn;
        this.title = title;
        this.genre = genre;
        this.rating = rating;
        this.puplishedDate = puplishedDate;
        this.authors = new ArrayList<>(authors);
    }

    /**
     * Gets id of book
     * @return
     */
    public ObjectId getBookId() {
        return bookId;
    }

    /**
     * Gets isbn
     */
    public String getIsbn() { return isbn; }

    /**
     * Gets title
     */
    public String getTitle() { return title; }

    /**
     * Gets rating
     */
    public int getRating() {
        return rating;
    }

    /**
     * Gets genre
     */
    public Genre getGenre() {
        return genre;
    }

    /**
     * sets isbn
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * sets title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * sets genre
     */
    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    /**
     * sets puplished date
     * @param puplishedDate
     */
    public void setPuplishedDate(LocalDate puplishedDate) {
        this.puplishedDate = puplishedDate;
    }

    /**
     * gets puplished date
     * @return
     */
    public LocalDate getPuplishedDate() {
        return puplishedDate;
    }

    /**
     * converts genre to string
     */
    public String genreToString(Genre genre)
    {
        if (Genre.Drama == genre)
        {
            return Genre.Drama.name().toLowerCase();
        }
        if (Genre.Horor == genre)
        {
            return Genre.Horor.name().toLowerCase();
        }
        if (Genre.Fantasy == genre)
        {
            return Genre.Fantasy.name().toLowerCase();
        }

        return Genre.Nogenre.name().toLowerCase();

    }

    /**
     * gets authors, does not copy
     */
    public ArrayList<Author> getAuthors() {
        return authors;
    }

    /**
     * sets rating
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * prints as formatted string
     */
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