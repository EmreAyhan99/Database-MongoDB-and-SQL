package game.model;

import javafx.scene.input.GestureEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
        books = Arrays.asList();
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



        } finally {
            if (conn== null)
            {
                conn.close();
            }
        }
            return true;

    }

    @Override
    public void disconnect() throws IOException, SQLException
    {
        try {
            if (conn != null) {
                conn.close();
                conn = null;
            }
        } finally {

        }

    }



    private void getBooks(ResultSet resultSet) throws SQLException {
        HashMap<String,Book> bookHashMap = new HashMap<>();
        while (resultSet.next())
        {
            String isbn = resultSet.getString("isbn");



        }

    }
    public Genre getInum(String enumname)
    {
        if (enumname == (String.valueOf(Genre.DRAMA)))
        {
            return Genre.DRAMA;
        }
        if (enumname == (String.valueOf(Genre.HOROR)))
        {
            return Genre.HOROR;
        }
        return Genre.NOGENRE;

    }

    @Override
    public List<Book> searchBooksByTitle(String searchTitle) throws IOException, SQLException
    {
        // mock implementation
        // NB! Your implementation should select the books matching
        // the search string via a query with to a database.
        List<Book> result = new ArrayList<>();
        searchTitle = searchTitle.toLowerCase();

        /*
        try {
            var preparedStatement = conn.prepareStatement("SELECT * FROM t_book WHERE title='" + searchTitle + "'");
            preparedStatement.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }  */

        //ObservableList<Book> books = FXCollections.observableArrayList();
        String query = "SELECT * FROM t_book WHERE title='"+searchTitle+"'";

            var rs = conn.createStatement().executeQuery(query);
            Book book;
            String enumBook = rs.getString("genre");


            while (rs.next())
            {
                book = new Book(rs.getString(1),rs.getString(2),getInum(enumBook),rs.getInt("grade"));
                System.out.println(book.toString());
            }



        /*
        var rs = conn.createStatement().executeQuery("SELECT * FROM t_book WHERE title='"+searchTitle+"'");

        while (rs.next())
        {
            Book book = null;
            result.add(new Book(rs.getString("isbn"),rs.getString("title"),rs.getString("genre"),rs.getString("grade")));
            //book.setIsbn(rs.getString("isbn"));
            //book.setTitle(rs.g);



            System.out.println(rs.getString("title"));
        } */

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

    /*
    private static final Book[] DATA = {
            new Book(1, "123456789", "Databases Illuminated", new Date(1990, 1, 1)),
            new Book(2, "456789012", "The buried giant", new Date(2000, 1, 1)),
            new Book(2, "567890123", "Never let me go", new Date(2000, 1, 1)),
            new Book(2, "678901234", "The remains of the day", new Date(2000, 1, 1)),
            new Book(2, "234567890", "Alias Grace", new Date(2000, 1, 1)),
            new Book(3, "345678901", "The handmaids tale", new Date(2010, 1, 1))
    }; */
}
