package game.view;


import game.model.Book;
import game.model.Genre;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

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
    private final TextField rating = new TextField();


    public AddBookDialog() {
        buildAddBookDialog();
    }

    private void buildAddBookDialog() {

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

        this.getDialogPane().setContent(grid);

        ButtonType buttonTypeOk
                = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
        this.getDialogPane().getButtonTypes().add(buttonTypeOk);
        ButtonType buttonTypeCancel
                = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        this.getDialogPane().getButtonTypes().add(buttonTypeCancel);

        // this callback returns the result from our dialog, via
        // Optional<FooBook> result = dialog.showAndWait();
        // FooBook book = result.get();
        // see DialogExample, line 31-34
        this.setResultConverter(new Callback<ButtonType, Book>() {
            @Override
            public Book call(ButtonType b) {
                Book result = null;
                if (b == buttonTypeOk) {
                    if (isValidData()) {
                        //int  rating = Integer.parseInt(rating.getText());
                        result = new Book( isbnField.getText(),titleField.getText(), genreChoice.getSelectionModel().getSelectedItem().getGenre(), rating.getPrefColumnCount());
                    }
                }

                clearFormData();
                return result;
            }
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
    }

    // TODO for the student: check each input separately, to give better
    // feedback to the user
    private boolean isValidData() {
        if (genreChoice.getValue() == null) {
            return false;
        }
        if (!Book.isValidIsbn(isbnField.getText())) {
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
    }

    private final Alert errorAlert = new Alert(Alert.AlertType.ERROR);

    private void showErrorAlert(String title, String info) {
        errorAlert.setTitle(title);
        errorAlert.setHeaderText(null);
        errorAlert.setContentText(info);
        errorAlert.show();
    }
}

