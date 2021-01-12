package game.view;


import game.model.Author;
import game.model.Book;
import game.model.Genre;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

/**
 * A simplified example of a form, using JavaFX Dialog and DialogPane. Type
 * parameterized for FooBook.
 *
 * @author Anders Lindström, anderslm@kth.se
 */
public class AddBookDialog extends Dialog<Book> {

    private final TextField titleField = new TextField();
    private final TextField isbnField = new TextField();
    private final ComboBox<Book> genreChoice = new ComboBox(FXCollections
            .observableArrayList(Genre.values()));
    //private final TextField rating = new TextField();
    private final ComboBox<Book> rating = new ComboBox(FXCollections
            .observableArrayList(1,2,3,4,5));
    private final DatePicker datePicker = new DatePicker();
    private final ArrayList<Author> authours = new ArrayList<>();
    private  ArrayList<Author> clickedAuthors = new ArrayList<>();
    private final TextField authorField = new TextField();
    AddAuthorDialog addAuthorDialog = new AddAuthorDialog();

    private ListView <Author> authorListView = new ListView<>();

    public AddBookDialog(Controller controller) {
       buildAddBookDialog(controller);
    }

    private void buildAddBookDialog(Controller controller)
    {

        this.setTitle("Add a new book");
        this.setResizable(false); // really?

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.add(new Label("Title "), 1, 1);
        grid.add(titleField, 2, 1);
        grid.add(new Label("Isbn "), 1, 2);
        grid.add(isbnField, 2, 2);
        grid.add(new Label("Genre "), 1, 3);
        grid.add(genreChoice, 2, 3);
        grid.add(new Label("Rating"),1,4);
        grid.add(rating,2,4);
        grid.add(new Label("Published"),1,5);
        grid.add(datePicker,2,5);
        grid.add(new Label("Author"),1,6);
        //grid.add(authorField,2,6);
        var addAuthorButton = new Button("Add new Author");
        grid.add(addAuthorButton,2,6);

        var newAuthorButton = new Button("Add author");
        authorListView.setMaxHeight(120);
        grid.add(authorListView,2,7);
        grid.add(newAuthorButton,2,8);
        /////////

        newAuthorButton.setOnAction(e -> {
            var clickedAuthor = authorListView.getSelectionModel().getSelectedItem();
            if (clickedAuthor != null && !clickedAuthors.contains(clickedAuthor))
            {
                clickedAuthors.add(clickedAuthor);
                System.out.println("clickade författare"+ clickedAuthors.toString());
            }
        });


        this.getDialogPane().setContent(grid);

        ButtonType buttonTypeOk
                = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
        this.getDialogPane().getButtonTypes().add(buttonTypeOk);
        ButtonType buttonTypeCancel
                = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        this.getDialogPane().getButtonTypes().add(buttonTypeCancel);

        addAuthorButton.setOnMouseClicked(Event ->{
            Optional<Author> author = addAuthorDialog.showAndWait();


            author.ifPresent(a -> {

                try {
                    controller.addAuthor(a);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
            });
        });

        //Add = (Button) this.getDialogPane().lookupButton(buttonType);
        // this callback returns the result from our dialog, via
        // Optional<FooBook> result = dialog.showAndWait();
        // FooBook book = result.get();
        // see DialogExample, line 31-34

        this.setResultConverter(b -> {
            //Book result;


            if (b == buttonTypeOk) {

                if (isValidData()) {
                    //int  rating = Integer.parseInt(rating.getText());
                    var date = datePicker.getValue();
                    Date date1 = Date.valueOf(date);
                    //System.out.println((genreChoice.getSelectionModel().getSelectedIndex()));

                     Book book = new Book(isbnField.getText(),titleField.getText(), getGenre(genreChoice.getSelectionModel().getSelectedIndex()) ,rating.getSelectionModel().getSelectedIndex()+1, date1.toLocalDate(), clickedAuthors);


                    clearFormData();
                    System.out.println("innan"+clickedAuthors.toString());
                    clickedAuthors.clear(); //////////
                    System.out.println("efter"+clickedAuthors.toString());
                    return book;
                }
            }
            return null;

            //clearFormData();
        });

        // add an event filter to keep the dialog active if validation fails
        // (yes, this is ugly in FX)
        Button okButton
                = (Button) this.getDialogPane().lookupButton(buttonTypeOk);
        okButton.addEventFilter(ActionEvent.ACTION, new EventHandler() {
            @Override
            public void handle(Event event) {
                if (!isValidData()) {
                    event.consume();
                    showErrorAlert("Form error", "Invalid input");
                }
            }
        });
        //return null;
    }

    // TODO for the student: check each input separately, to give better
    // feedback to the user
    private boolean isValidData() {
        if (genreChoice.getValue() == null) {
            return false;
        }
        if (Book.isValidIsbn(isbnField.getText())) {   //Book.isValidIsbn(isbnField.getText())        // ändrade den här
            System.out.println(isbnField.getText());
            return false;
        }
                                                                // if(...) - keep on validating user input...

        return true;
    }

    private void clearFormData() {
        titleField.setText("");
        isbnField.setText("");
        genreChoice.setValue(null);
        rating.setValue(null);
        datePicker.setValue(null);


    }

    private final Alert errorAlert = new Alert(Alert.AlertType.ERROR);

    private void showErrorAlert(String title, String info) {
        errorAlert.setTitle(title);
        errorAlert.setHeaderText(null);
        errorAlert.setContentText(info);
        errorAlert.show();
    }

    public Genre getGenre(int row)
    {
        if(row ==0)
        {
            return Genre.Drama;
        }
        if(row ==1)
        {
            return Genre.Horor;
        }
        if (row ==2)
        {
            return Genre.Fantasy;
        }
        return Genre.Nogenre;
    }

    public ArrayList<Author> getAuthours() {
        return authours;
    }

    public void loadAuthors(ArrayList<Author> authors)
    {
        authorListView.getItems().setAll(authors);

    }
}

