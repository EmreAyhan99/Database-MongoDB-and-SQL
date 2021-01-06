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

import java.sql.Date;
import java.util.ArrayList;

public class AddAuthorDialog extends Dialog<Author>
{

    private final ArrayList<Author> authours = new ArrayList<>();
    private final TextField authorField = new TextField();
    

    ButtonType buttonType = new ButtonType("Add");
    Button Add = new Button("Add");



    public AddAuthorDialog() {
        buildAddAuthorDialog();
    }

    private void buildAddAuthorDialog()
    {

        this.setTitle("Add a new Author");
        this.setResizable(false); // really?

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER_RIGHT);
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.add(new Label("Author "), 1, 1);
        grid.add(authorField, 2, 1);



        this.getDialogPane().setContent(grid);

        ButtonType buttonTypeOk
                = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
        this.getDialogPane().getButtonTypes().add(buttonTypeOk);
        ButtonType buttonTypeCancel
                = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        this.getDialogPane().getButtonTypes().add(buttonTypeCancel);

        Add = (Button) this.getDialogPane().lookupButton(buttonType);
        // this callback returns the result from our dialog, via
        // Optional<FooBook> result = dialog.showAndWait();
        // FooBook book = result.get();
        // see DialogExample, line 31-34

        this.setResultConverter(b -> {
            //Book result;
            Author author = null;
            if (b == buttonTypeOk) {

                if (isValidData()) {

                    author = new Author(authorField.getText());
                    authorField.clear();
                    return author;
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

        if (authorField.getText().equals("")) {
            return false;
        }

        // if(...) - keep on validating user input...

        return true;
    }

    private void clearFormData() {
        authorField.setText("");
    }

    private final Alert errorAlert = new Alert(Alert.AlertType.ERROR);

    private void showErrorAlert(String title, String info) {
        errorAlert.setTitle(title);
        errorAlert.setHeaderText(null);
        errorAlert.setContentText(info);
        errorAlert.show();
    }


}
