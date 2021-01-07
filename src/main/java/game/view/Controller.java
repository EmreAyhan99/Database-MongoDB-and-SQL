package game.view;



import game.model.Author;
import game.model.Book;
import game.model.BooksDbInterface;
import game.model.SearchMode;
import javafx.application.Platform;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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
                        result = booksDb.searchBooksByISBN(searchFor);
                        break;
                    case Author:
                        result = booksDb.searchBooksByAuthor(searchFor);
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
        } catch (SQLException | IOException sqlException) {
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

    public void addBook(Book book, ArrayList<Author> author)
    {

        //Book book = null;
        try {
            booksDb.addBookAndAuthor(book, author);  //bara temp måste göra diolog för att få datan
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void addAuthor(Author author) throws IOException,SQLException
    {
        try {
            booksDb.addAuthors(author);
        }catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

    }

    public void showAllbooks()
    {
        new Thread() {
            public void run() {
        try {
             // kallar på data modellen
            ArrayList <Book> result = new ArrayList<>();
            result.addAll(booksDb.getAllBooks());
            Platform.runLater(new Runnable() {
                public void run() {
                    booksView.displayBooks(result);
                    //GÖRA något i programmet upttadera vyn göra någit i vyn
                }
            });
        } catch (IOException e) {
            Platform.runLater(new Runnable() {
                public void run() {
                    booksView.showAlertAndWait(e.getMessage(), ERROR);
                }
            });

        } catch (SQLException e) {
            Platform.runLater(new Runnable() {
                public void run() {
                    booksView.showAlertAndWait(e.getMessage(), ERROR);
                }
            });

        }
    }
    }.start();




        /*
        Thread thread = new Thread(() ->{

                Platform.runLater(new Runnable() {
                    @Override
                    public void run()
                    {
                        try {
                            System.out.println("RIIIIIIIII"+booksDb.getAllBooks());
                            booksView.displayBooks(booksDb.getAllBooks());
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }

                    }
                });

            //this.gameTimeScore = boardModel.getTime();

        });thread.start();
        */
        /*
        try {
            booksView.displayBooks(booksDb.getAllBooks());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } */
    }

    // TODO:
    // Add methods for all types of user interaction (e.g. via  menus).
}
