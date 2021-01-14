package game.model;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import javafx.css.Match;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A mock implementation of the BooksDBInterface interface to demonstrate how to
 * use it together with the user interface.
 * <p>
 * Your implementation should access a real database.
 *
 * @author anderslm@kth.se, Emre Ayhan
 */
public class BooksDb implements BooksDbInterface {



    private boolean connected;
    private MongoClient client;
    private MongoCollection<Document> books;
    private MongoCollection<Document> authors;

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
    public boolean connect(String database) throws IOException {
        client = MongoClients.create(database);
        MongoDatabase mongoDatabase = client.getDatabase("mydb");
        books = mongoDatabase.getCollection("books");
        authors = mongoDatabase.getCollection("authors");
        connected = true;
        return true;
    }

    /**
     * Disconnects from db
     */
    @Override
    public void disconnect() throws IOException
    {
        client.close();
        connected = false;

    }


    /**
     * Adds books and it's authors
     */
    @Override
    public void addBookAndAuthor(Book book) throws IOException
    {
        ArrayList<Document> list = new ArrayList<>();
        for (Author author : book.getAuthors())
        {
            list.add(new Document(Map.of("_id", author.getAuthorID() , "name", author.getName())));
        }
        books.insertOne(new Document(
                Map.of(
                        "isbn", book.getIsbn(),
                        "title",book.getTitle(),
                        "genre",book.getGenre().name(),
                        "rating",book.getRating(),
                        "puplished",book.getPuplishedDate().toString(),
                        "authors", list
                )
        ));

    }


    /**
     * Connects the book with an author
     */


    /**
     * Gets all authors from db
     */
    @Override
    public List<Author> getAllAuthors() throws IOException {
        ArrayList<Author> list = new ArrayList<>();
        for (Document document : authors.find()) {
            list.add(new Author(document.get("_id",new ObjectId()), document.get("name", "")));

        }
        return list;
    }

    /**
     * Adds author to database
     */
    @Override
    public void addAuthors(Author author) throws IOException
    {

        authors.insertOne(new Document(
                Map.of(
                        "name", author.getName()
                )
        ));

    }


    /**
     * Gets all books from db
     */
    @Override
    public List<Book> getAllBooks() throws IOException
    {
        ArrayList <Book> listOfBooks = new ArrayList<>();
        for (Document document : books.find()) {
            listOfBooks.add(new Book(
                    document.get("_id", new ObjectId()),
                    document.get("isbn", ""),
                    document.get("title",""),
                    Genre.valueOf(document.get("genre","")),
                    document.get("rating",0),
                    LocalDate.parse(document.get("puplished",""))
            ));
        }

        return listOfBooks;
    }


    /**
     * Serach books by title
     */
    @Override
    public List<Book> searchBooksByTitle(String searchTitle) throws IOException {
        List<Book> result = new ArrayList<>();
        for (Document document : books.find(Filters.regex("title",Pattern.compile(searchTitle+"(?i)"))))
        {
            result.add(new Book(
                    document.get("_id", new ObjectId()),
                    document.get("isbn", ""),
                    document.get("title",""),
                    Genre.valueOf(document.get("genre","")),
                    document.get("rating",0),
                    LocalDate.parse(document.get("puplished",""))
            ));
        }

        return result;
    }

    /**
     * Search books by author
     */
    @Override
    public List<Book> searchBooksByAuthor(String author) throws IOException {

        List<Book> result = new ArrayList<>();
        for (Document document : books.find(Filters.elemMatch("authors",Filters.regex("name",Pattern.compile(author+"(?i)")))))
        {

            result.add(new Book(
                    document.get("_id", new ObjectId()),
                    document.get("isbn", ""),
                    document.get("title",""),
                    Genre.valueOf(document.get("genre","")),
                    document.get("rating",0),
                    LocalDate.parse(document.get("puplished",""))
            ));
        }

        return result;

    }

    /**
     * Search books by isbn
     */
    @Override
    public List<Book> searchBooksByISBN(String isbn) throws IOException
    {
        List<Book> result = new ArrayList<>();
        for (Document document : books.find(Filters.regex("isbn",Pattern.compile(isbn+"(?i)"))))
        {
            result.add(new Book(
                    document.get("_id", new ObjectId()),
                    document.get("isbn", ""),
                    document.get("title",""),
                    Genre.valueOf(document.get("genre","")),
                    document.get("rating",0),
                    LocalDate.parse(document.get("puplished",""))
            ));
        }

        return result;
    }

    /**
     * Delete selected book
     */
    @Override
    public void deleteClickedBook(Book bookSelected) throws IOException
    {
        if (connected)
        {
            books.deleteOne(new Document("_id",bookSelected.getBookId()));
        }
        else
        {
            return;
        }

    }

    /**
     * Returns if is connected to db
     */
    @Override
    public boolean connected() {
        return connected;
    }
}