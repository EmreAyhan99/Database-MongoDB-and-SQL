package game.view;



import game.model.Book;
import game.model.BooksDbInterface;
import game.model.SearchMode;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static javafx.scene.control.Alert.AlertType.*;

/**
 * The controller is responsible for handling user requests and update the view
 * (and in some cases the model).
 *
 * @author anderslm@kth.se
 */
public class Controller {

    private final BooksView booksView; // view
    private final BooksDbInterface booksDb; // model


    public Controller(BooksDbInterface booksDb, BooksView booksView)
    {
        this.booksDb = booksDb;
        this.booksView = booksView;

    }

    protected void onSearchSelected(String searchFor, SearchMode mode) {
        try {
            if (searchFor != null && searchFor.length() > 1) {
                List<Book> result = null;
                switch (mode) {
                    case Title:
                        result = booksDb.searchBooksByTitle(searchFor);
                        break;
                    case ISBN:
                        // ...
                        break;
                    case Author:
                        // ...
                        break;
                    default:
                }
                if (result == null || result.isEmpty()) {
                    booksView.showAlertAndWait(
                            "No results found.", INFORMATION);
                } else {
                    booksView.displayBooks(result);
                }
            } else {
                booksView.showAlertAndWait(
                        "Enter a search string!", WARNING);
            }
        } catch (Exception e) {
            booksView.showAlertAndWait("Database error.",ERROR);
        }
    }

    public void connectToServer()
    {
        try {
            booksDb.connect("jdbc:mysql://localhost/mydb?"+ "serverTimezone=UTC");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void disconnectFromServer() throws IOException, SQLException {
        try {
            booksDb.disconnect();
        } finally {}
    }

    public void addBook(Book book)
    {
        //Book book = null;
        try {
            booksDb.addBook(book);  //bara temp måste göra diolog för att få datan
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    // TODO:
    // Add methods for all types of user interaction (e.g. via  menus).
}
