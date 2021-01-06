package game.model;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * This interface declares methods for querying a Books database.
 * Different implementations of this interface handles the connection and
 * queries to a specific DBMS and database, for example a MySQL or a MongoDB
 * database.
 *
 * @author anderslm@kth.se
 */
public interface BooksDbInterface {

    /**
     * Connect to the database.
     * @param database
     * @return true on successful connection.
     */
    public boolean connect(String database) throws IOException, SQLException;

    public void disconnect() throws IOException, SQLException;

    public void addBook(Book book) throws SQLException;

    public List<Book> getAllBooks() throws SQLException,IOException;

    public List<Book> searchBooksByTitle(String title) throws IOException, SQLException;

    List<Book> searchBooksByAuthor(String author) throws IOException, SQLException;

    List<Book> searchBooksByISBN(String isbn) throws IOException, SQLException;

    List<Book> searchBooksByRating(String rating) throws IOException, SQLException;

    List<Book> searchBooksByGenre(String genre) throws IOException, SQLException;

    // TODO: Add abstract methods for all inserts, deletes and queries
    // mentioned in the instructions for the assignement.
}






