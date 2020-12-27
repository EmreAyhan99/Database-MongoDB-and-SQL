package game.model;

import java.io.IOException;
import java.lang.module.FindException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.sql.*;

/**
 * A mock implementation of the BooksDBInterface interface to demonstrate how to
 * use it together with the user interface.
 *
 * Your implementation should access a real database.
 *
 * @author anderslm@kth.se
 */
public class BooksDb implements BooksDbInterface {

    private final List<Book> books;
    private Connection conn;

    public BooksDb() {
        books = Arrays.asList(DATA);
    }

    @Override
    public boolean connect(String database) throws SQLException
    {

        conn = null;
        try {
            conn = DriverManager.getConnection(database,"client","emre123123!");
            var rs = conn.createStatement().executeQuery("SELECT * FROM t_book");

            while (rs.next()) {   //VARje rad för databasen så skriver man ut
                System.out.println(rs.getString("isbn"));
            }

            conn.close();

        } finally {
            if (conn!= null)
            {
                conn.close();
            }
        }
            return true;

    }

    @Override
    public void disconnect() throws IOException, SQLException {
        // mock implementation
    }

    @Override
    public List<Book> searchBooksByTitle(String searchTitle) throws IOException, SQLException
    {
        // mock implementation
        // NB! Your implementation should select the books matching
        // the search string via a query with to a database.
        List<Book> result = new ArrayList<>();
        searchTitle = searchTitle.toLowerCase();
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(searchTitle)) {
                result.add(book);
            }
        }
        return result;
    }

    @Override
    public List<Book> searchBooksByAuthor(String author) throws IOException, SQLException {
        return null;
    }

    @Override
    public List<Book> searchBooksByISBN(String isbn) throws IOException, SQLException {
        return null;
    }

    @Override
    public List<Book> searchBooksByRating(String rating) throws IOException, SQLException {
        return null;
    }

    @Override
    public List<Book> searchBooksByGenre(String genre) throws IOException, SQLException {
        return null;
    }

    private static final Book[] DATA = {
            new Book(1, "123456789", "Databases Illuminated", new Date(1990, 1, 1)),
            new Book(2, "456789012", "The buried giant", new Date(2000, 1, 1)),
            new Book(2, "567890123", "Never let me go", new Date(2000, 1, 1)),
            new Book(2, "678901234", "The remains of the day", new Date(2000, 1, 1)),
            new Book(2, "234567890", "Alias Grace", new Date(2000, 1, 1)),
            new Book(3, "345678901", "The handmaids tale", new Date(2010, 1, 1))
    };
}
