package game.model;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This interface declares methods for querying a Books database.
 * Different implementations of this interface handles the connection and
 * queries to a specific DBMS and database, for example a MySQL or a MongoDB
 * database.
 *
 * @author anderslm@kth.se, Emre Ayhan
 */
public interface BooksDbInterface {

    /**
     * Connect to the database.
     * @param database
     * @return true on successful connection.
     */
    public boolean connect(String database) throws IOException;

    /**
     * Disconnects from db
     */
    public void disconnect() throws IOException;

    /**
     * Adds books and authors to db
     */
    public void addBookAndAuthor(Book book) throws IOException;

    /**
     * Adds author to db
     */
    public void addAuthors(Author author) throws IOException;

    /**
     * Gets all books from db
     */
    public List<Book> getAllBooks() throws IOException;

    /**
     * Gets all authors from db
     */
    public List <Author> getAllAuthors() throws IOException;

    /**
     * Searches for books in db by title
     */
    public List<Book> searchBooksByTitle(String title) throws IOException;

    /**
     * Searches for books in db by author
     */
    List<Book> searchBooksByAuthor(String author) throws IOException;

    /**
     * Searches for books in db by isbn
     */
    List<Book> searchBooksByISBN(String isbn) throws IOException;


    /**
     * Deletes selected book from db
     */
    public void deleteClickedBook(Book bookSelected) throws IOException;

    public boolean connected();
}