package game.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A mock implementation of the BooksDBInterface interface to demonstrate how to
 * use it together with the user interface.
 * <p>
 * Your implementation should access a real database.
 *
 * @author anderslm@kth.se, Emre Ayhan
 */
public class BooksDb implements BooksDbInterface {

    PreparedStatement insertBook;
    PreparedStatement insertAuthor;
    PreparedStatement bookAuthorConnect;
    PreparedStatement searchTitlee;
    PreparedStatement searchIsbn;
    PreparedStatement searchAuthor;
    PreparedStatement searchAuthorBook;
    PreparedStatement getAllAuthors;
    PreparedStatement deleteBook;
    PreparedStatement addAuthor;
    PreparedStatement getAllBooks;
    private Connection conn;
    private boolean connected;

    /**
     * Default constructor for BooksDb
     */
    public BooksDb() {
        connected = false;
    }

    /**
     * Connects to database
     */
    @Override
    public boolean connect(String database) throws SQLException {

        conn = null;

        conn = DriverManager.getConnection(database, "client", "emre123123!");
        var rs = conn.createStatement().executeQuery("SELECT * FROM t_book");


        bookAuthorConnect = conn.prepareStatement("INSERT into t_BookAuthor (authorID,t_book_bookID) VALUES (?,?)");

        searchTitlee = conn.prepareStatement("SELECT * FROM " + "t_book" + " b WHERE b.title LIKE ?");
        searchIsbn = conn.prepareStatement("SELECT * FROM " + "t_book" + " b WHERE b.isbn LIKE ?");
        searchAuthor = conn.prepareStatement("SELECT * FROM " + "t_author" + " b WHERE b.namn LIKE ?");

        insertBook = conn.prepareStatement("INSERT INTO t_book (isbn,title,genre,grade,puplished)" + " values (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        insertAuthor = conn.prepareStatement("insert into t_author (namn)" + " values (?)", Statement.RETURN_GENERATED_KEYS);


        addAuthor = conn.prepareStatement("insert into t_author (namn)" + " values (?)");

        searchAuthorBook = conn.prepareStatement("SELECT * \n" +
                "FROM t_book\n" +
                "WHERE t_book.bookID IN (\n" +
                "            SELECT t_BookAuthor.t_book_bookID\n" +
                "            FROM t_BookAuthor\n" +
                "            WHERE t_BookAuthor.authorId IN (\n" +
                "                            SELECT t_author.authorID\n" +
                "                            FROM t_author\n" +
                "                            WHERE t_author.namn LIKE ?" +
                "                            )\n" +
                "\t\t\t)");

        //deleteBook = conn.prepareStatement("DELETE * FROM " + "t_book" + " b WHERE b.isbn LIKE ?");

        getAllAuthors = conn.prepareStatement("SELECT * FROM t_author");

        getAllBooks = conn.prepareStatement("Select * from t_book");

        while (rs.next()) {   //VARje rad för databasen så skriver man ut
            System.out.println(rs.getString("genre"));
        }


        connected = true;
        return true;
    }

    /**
     * Disconnects from db
     */
    @Override
    public void disconnect() throws SQLException {

        if (conn != null) {
            conn.close();
            conn = null;
        }

        insertBook.close();
        insertAuthor.close();
        bookAuthorConnect.close();
        searchTitlee.close();
        searchIsbn.close();
        searchAuthor.close();
        searchAuthorBook.close();
        getAllAuthors.close();
        deleteBook.close();
        addAuthor.close();


    }


    /**
     * Adds books and it's authors
     */
    @Override
    public void addBookAndAuthor(Book book) throws SQLException {


        try {
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

                for (var author : book.getAuthors()) {
                    int authorId = author.getAuthorID();
                    connectBookAuthor(bookId, authorId);
                }
            }
            conn.commit();
        } catch (SQLException sqlException) {
            conn.rollback();
            throw sqlException;
        } finally {
            conn.setAutoCommit(true);
        }

    }


    /**
     * Connects the book with an author
     */
    public void connectBookAuthor(int bookId, int AuthorId) throws SQLException {

        bookAuthorConnect.setInt(1, AuthorId);
        bookAuthorConnect.setInt(2, bookId);
        bookAuthorConnect.executeUpdate();
    }

    /**
     * Gets all authors from db
     */
    @Override
    public List<Author> getAllAuthors() throws SQLException {
        ArrayList<Author> authors = new ArrayList<>();

        var rs = getAllAuthors.executeQuery();

        while (rs.next()) {
            Author author = new Author(rs.getInt(1), rs.getString(2));
            authors.add(author);
        }

        return authors;
    }

    /**
     * Adds author to database
     */
    @Override
    public void addAuthors(Author author) throws SQLException {
        addAuthor.setString(1, author.getName());
        addAuthor.executeUpdate();
    }


    /**
     * Gets all books from db
     */
    @Override
    public List<Book> getAllBooks() throws SQLException {

        var rs = getAllAuthors.executeQuery();

        List<Book> listOfBooks = new ArrayList<>();

        while (rs.next()) {
            Book book = new Book(rs.getInt(1), rs.getString(2), rs.getString(3), getInum(rs.getString(4)), rs.getInt(5), rs.getDate(6));
            listOfBooks.add(book);
        }

        return listOfBooks;
    }


    /**
     * returns a enum
     */
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

    /**
     * Serach books by title
     */
    @Override
    public List<Book> searchBooksByTitle(String searchTitle) throws SQLException {
        List<Book> result = new ArrayList<>();
        searchTitle = searchTitle.toLowerCase();

        searchTitlee.setString(1, "%" + searchTitle + "%");
        var rs = searchTitlee.executeQuery();
        while (rs.next()) {
            Book book = new Book(rs.getInt(1), rs.getString(2), rs.getString(3), getInum(rs.getString(4)), rs.getInt(5), rs.getDate(6));

            result.add(book);
        }

        return result;
    }

    /**
     * Search books by author
     */
    @Override
    public List<Book> searchBooksByAuthor(String author) throws SQLException {

        List<Book> result = new ArrayList<>();
        author = author.toLowerCase();

        searchAuthorBook.setString(1, "%" + author + "%");
        var rs = searchAuthorBook.executeQuery();
        //var rs = searchAuthor.executeQuery();
        while (rs.next()) {

            Book book = new Book(rs.getInt(1), rs.getString(2), rs.getString(3), getInum(rs.getString(4)), rs.getInt(5), rs.getDate(6));
            result.add(book);
        }

        return result;

    }

    /**
     * Search books by isbn
     */
    @Override
    public List<Book> searchBooksByISBN(String isbn) throws SQLException {
        List<Book> result = new ArrayList<>();
        isbn = isbn.toLowerCase();

        searchIsbn.setString(1, "%" + isbn + "%");
        var rs = searchIsbn.executeQuery();
        while (rs.next()) {
            Book book = new Book(rs.getInt(1), rs.getString(2), rs.getString(3), getInum(rs.getString(4)), rs.getInt(5), rs.getDate(6));

            result.add(book);

        }

        return result;
    }

    /**
     * Delete selected book
     */
    @Override
    public void deleteClickedBook(Book bookSelected) throws SQLException {
        if (bookSelected == null) {
            return;
        }
        deleteBook = conn.prepareStatement("DELETE FROM `t_book` WHERE isbn = '" + bookSelected.getIsbn() + "'");
        deleteBook.execute();
    }

    /**
     * Returns if is connected to db
     */
    public boolean connected() {
        return connected;
    }
}