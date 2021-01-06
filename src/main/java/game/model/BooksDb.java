package game.model;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.sql.*;

/**
 * A mock implementation of the BooksDBInterface interface to demonstrate how to
 * use it together with the user interface.
 * <p>
 * Your implementation should access a real database.
 *
 * @author anderslm@kth.se
 */
public class BooksDb implements BooksDbInterface {

    private final List<Book> books;
    private Connection conn;
    PreparedStatement insertBook;
    PreparedStatement insertAuthor;
    PreparedStatement bookAuthorConnect;

    public BooksDb() throws SQLException {


        books = Arrays.asList();
    }

    @Override
    public boolean connect(String database) throws SQLException {

        conn = null;
        try {
            conn = DriverManager.getConnection(database, "client", "emre123123!");
            var rs = conn.createStatement().executeQuery("SELECT * FROM t_book");
            insertBook = conn.prepareStatement("INSERT INTO t_book (isbn,title,genre,grade,puplished)" + " values (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            insertAuthor = conn.prepareStatement("insert into t_author (namn)" + " values (?)", Statement.RETURN_GENERATED_KEYS);
            bookAuthorConnect = conn.prepareStatement("INSERT into t_BookAuthor (authorID,t_book_bookID) VALUES (?,?)");

            while (rs.next()) {   //VARje rad för databasen så skriver man ut
                System.out.println(rs.getString("genre"));
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;

    }

    @Override
    public void disconnect() throws IOException, SQLException {
        try {
            if (conn != null) {
                conn.close();
                conn = null;
            }
        } finally {

        }

    }


    @Override
    public void addBookAndAuthor(Book book, ArrayList<Author> authors) throws SQLException {
        //storeb
        /*
        CallableStatement callableStatement = conn.prepareCall("{call authorRelationWithBook(?,?,?,?");

        callableStatement.registerOutParameter(1,Types.INTEGER);
        callableStatement.setString(2,"isbnIn");
        callableStatement.registerOutParameter(3,Types.VARCHAR);
        callableStatement.registerOutParameter(4,Types.DATE);
        callableStatement.executeUpdate();

        var id = callableStatement.getInt(1);
        var isbn = callableStatement.getString(2);
        var name = callableStatement.getString(3);
        var date = callableStatement.getDate(4);

        System.out.println("id"+ id);
        System.out.println("isbn"+ isbn);
        System.out.println("name" + name);
        System.out.println("date"+date);

        */

        conn.setAutoCommit(false);
        insertBook.setString(1, book.getIsbn());
        insertBook.setString(2, book.getTitle());
        insertBook.setString(3, String.valueOf(book.getGenre()));
        insertBook.setInt(4, book.getRating());
        insertBook.setDate(5, book.getPuplishedDate());

        insertBook.executeUpdate();

        var bookResultSet = insertBook.getGeneratedKeys();

        if (bookResultSet.next()) {
            int bookId = bookResultSet.getInt(1);

            for (var author : authors) {
                insertAuthor.setString(1, author.getName());
                insertAuthor.execute();

                var authorResultSet = insertAuthor.getGeneratedKeys();

                if (authorResultSet.next()) {
                    int authorId = authorResultSet.getInt(1);
                    connectBookAuthor(bookId, authorId);

                } else {
                    conn.rollback();
                    break;
                }

            }
        }
        conn.commit();
    }


    public void connectBookAuthor(int bookId, int AuthorId) throws SQLException {

        String query = "INSERT into t_BookAuthor (authorID,t_book_bookID) VALUES (?,?)";
        bookAuthorConnect = conn.prepareStatement(query);

        bookAuthorConnect.setInt(1, AuthorId);
        bookAuthorConnect.setInt(2, bookId);

        bookAuthorConnect.executeUpdate();

    }


    @Override
    public void addAuthors(Author author) throws SQLException {
        String query = "insert into t_book (namn)" + " values (?)";
        PreparedStatement preparedStatement = conn.prepareStatement(query);


    }


    @Override
    public List<Book> getAllBooks() throws SQLException {

        String query = "Select * from t_book";
        var rs = conn.createStatement().executeQuery(query);
        //var rs = conn.prepareStatement(query);
        List<Book> listOfBooks = new ArrayList<>();

        while (rs.next()) {
            Book book = new Book(rs.getInt(1), rs.getString(2), rs.getString(3), getInum(rs.getString(4)), rs.getInt(5), rs.getDate(6));
            listOfBooks.add(book);

        }
        return listOfBooks;
    }


    public Genre getInum(String enumname) {
        if (enumname.equals(String.valueOf(Genre.DRAMA))) {
            return Genre.DRAMA;
        }
        if (enumname.equals(String.valueOf(Genre.HOROR))) {
            return Genre.HOROR;
        }
        if (enumname.equals(String.valueOf(Genre.FANTASY))) {
            return Genre.FANTASY;
        }

        return Genre.NOGENRE;

    }

    @Override
    public List<Book> searchBooksByTitle(String searchTitle) throws IOException, SQLException {
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
        String query = "SELECT * FROM t_book WHERE title='" + searchTitle + "'";

        try {

            Book book;
            var rs = conn.createStatement().executeQuery(query);
            String enumBook = rs.getString("genre");
            while (rs.next()) {
                System.out.println(rs.getString("genre"));
                book = new Book(rs.getInt(1), rs.getString(2), rs.getString(3), getInum(enumBook), rs.getInt("grade"), rs.getDate(6));
                System.out.println(book.toString());
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
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
