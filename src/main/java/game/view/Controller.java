package game.view;



import game.model.Author;
import game.model.Book;
import game.model.BooksDbInterface;
import game.model.SearchMode;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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

       new Thread(() -> {

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
                        Platform.runLater(() -> {
                        booksView.showAlertAndWait("No results found.", INFORMATION);
                        });
                    } else {
                        List<Book> finalResult = result;
                        Platform.runLater(() -> {
                        booksView.displayBooks(finalResult);
                        });
                    }
                } else {
                    Platform.runLater(() -> {
                    booksView.showAlertAndWait("Enter a search string!", WARNING);
                    });
                }
            } catch (SQLException | IOException sqlException) {
                Platform.runLater(() -> booksView.showAlertAndWait("Database error.",ERROR));
            }
       }).start();
    }

    public void connectToServer()
    {
        new Thread(() -> {
            try {
                booksDb.connect("jdbc:mysql://localhost/mydb?"+ "serverTimezone=UTC");
                showAllbooks();
            } catch (SQLException | IOException throwables) {
                Platform.runLater(() -> {
                    booksView.showAlertAndWait("Error connecting to db: "+ throwables.getMessage(), ERROR);
                });
            }
        }).start();
    }

    public void disconnectFromServer() throws SQLException {

        try {
            booksDb.disconnect();
        } catch (SQLException sqlException) {
            booksView.showAlertAndWait("could not disconnect from server", ERROR);

        }
    }

    public void addBook(Book book)
    {
        new Thread(() -> {
            try {
                booksDb.addBookAndAuthor(book);  //bara temp måste göra diolog för att få datan
            } catch (SQLException | IOException throwables) {
                Platform.runLater(() -> {
                    booksView.showAlertAndWait("Error while adding bok: " + throwables.getMessage(), ERROR);

                });
            }
        }).start();
    }


    public void addAuthor(Author author) throws IOException,SQLException
    {
        new Thread(() -> {
            try {
                ArrayList<Author> a = new ArrayList<>();
                a.add(author);
                booksDb.addAuthors(author);
            }catch (SQLException | IOException throwables)
            {
                Platform.runLater(() -> {
                    booksView.showAlertAndWait("Error while adding author: "+ throwables.getMessage(), ERROR);
                });
            }
        }).start();
    }

    public void showAllbooks()
    {
        new Thread(() -> {
            try {
                // kallar på data modellen
                ArrayList <Book> result = new ArrayList<>();
                result.addAll(booksDb.getAllBooks());
                Platform.runLater(() -> {
                    booksView.displayBooks(result);
                    //GÖRA något i programmet upttadera vyn göra någit i vyn
                });
            } catch (IOException e) {
                Platform.runLater(() -> booksView.showAlertAndWait(e.getMessage(), ERROR));

            } catch (SQLException e) {
                Platform.runLater(() -> booksView.showAlertAndWait(e.getMessage(), ERROR));
            }
        }).start();
    }

    public void showAllAuthors()
    {
        new Thread(() -> {
            try {
                // kallar på data modellen
                ArrayList <Author> result = new ArrayList<>();
                result.addAll(booksDb.getAllAuthors());
                Platform.runLater(() -> {
                    booksView.displayAuthors(result);
                    //GÖRA något i programmet upttadera vyn göra någit i vyn
                });
            } catch (SQLException e) {
                Platform.runLater(() -> booksView.showAlertAndWait(e.getMessage(), ERROR));
            }
        }).start();

    }


    public void onClickedDelete(Book clickedOn,Consumer <Book> info)
    {
        Thread thread = new Thread(() ->{

                try {
                  booksDb.deleteClickedBook(clickedOn);  // TODO clicked on är null forstätter imorgon notis

                } catch (SQLException throwables) {
                Platform.runLater(() -> {
                    booksView.showAlertAndWait(throwables.getMessage(), ERROR);
                    });
                }


            //this.gameTimeScore = boardModel.getTime();

        });thread.start();



    }

    // TODO:
    // Add methods for all types of user interaction (e.g. via  menus).
}
