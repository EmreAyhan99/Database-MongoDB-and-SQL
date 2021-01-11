package game.view;


import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import game.model.*;
import game.model.Book;
import game.model.BooksDb;
import game.model.Genre;
import game.model.SearchMode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * The main pane for the view, extending VBox and including the menus. An
 * internal BorderPane holds the TableView for books and a search utility.
 *
 * @author anderslm@kth.se
 */
public class BooksView extends VBox {
    private final BooksDb db;
    //listview
    private TableView<Book> booksTable;
    private ObservableList<Book> booksInTable; // the data backing the table view
    private ObservableList<Author> authorsInTable;

    private ComboBox<SearchMode> searchModeBox;
    private TextField searchField;
    private Button searchButton;
    private Book bookClickedOn;
    private Controller controller;
    AddBookDialog addBookDialog;

    private MenuBar menuBar;

    public BooksView(BooksDb booksDb)
    {
        this.db = booksDb;
        this.controller = new Controller(booksDb, this);
        this.init(controller);
        addBookDialog = new AddBookDialog(controller);


    }

    /**
     * Display a new set of books, e.g. from a database select, in the
     * booksTable table view.
     *
     * @param books the books to display
     */
    public void displayBooks(List<Book> books) {
        booksInTable.clear();
        booksInTable.addAll(books);
    }

    public void displayAuthors(ArrayList<Author> authors)
    {
        addBookDialog.loadAuthors(authors);


    }

    /**
     * Notify user on input error or exceptions.
     *
     * @param msg the message
     * @param type types: INFORMATION, WARNING et c.
     */
    protected void showAlertAndWait(String msg, Alert.AlertType type) {
        // types: INFORMATION, WARNING et c.
        Alert alert = new Alert(type, msg);
        alert.showAndWait();
    }

    private void init(Controller controller) {

        booksInTable = FXCollections.observableArrayList();

        // init views and event handlers
        initBooksTable();
        initSearchView(controller);
        initMenus(controller);

        FlowPane bottomPane = new FlowPane();
        bottomPane.setHgap(10);
        bottomPane.setPadding(new Insets(10, 10, 10, 10));
        bottomPane.getChildren().addAll(searchModeBox, searchField, searchButton);

        BorderPane mainPane = new BorderPane();
        mainPane.setCenter(booksTable);
        mainPane.setBottom(bottomPane);
        mainPane.setPadding(new Insets(10, 10, 10, 10));

        this.getChildren().addAll(menuBar, mainPane);
        VBox.setVgrow(mainPane, Priority.ALWAYS);
    }

    private void initBooksTable() {
        booksTable = new TableView<>();
        booksTable.setEditable(false); // don't allow user updates (yet)

        // define columns
        TableColumn<Book, Integer> idCol = new TableColumn<>("Id");
        TableColumn<Book, String> titleCol = new TableColumn<>("Title");
        TableColumn<Book, String> isbnCol = new TableColumn<>("ISBN");
        TableColumn<Book, Date> publishedCol = new TableColumn<>("Published");
        TableColumn<Book, Genre> genreCol = new TableColumn<>("Genre");
        TableColumn<Book, Integer> ratingCol = new TableColumn<>("Rating");
        booksTable.getColumns().addAll(idCol,titleCol, isbnCol, publishedCol,genreCol,ratingCol);
        // give title column some extra space
        titleCol.prefWidthProperty().bind(booksTable.widthProperty().multiply(0.5));

        // define how to fill data for each cell,
        // get values from Book properties
        idCol.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        isbnCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        publishedCol.setCellValueFactory(new PropertyValueFactory<>("puplishedDate"));
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));
        // associate the table view with the data

        booksTable.setItems(booksInTable);
        booksTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.bookClickedOn = newValue;
        });
    }

    private void initSearchView(Controller controller) {
        searchField = new TextField();
        searchField.setPromptText("Search for...");
        searchModeBox = new ComboBox<>();
        searchModeBox.getItems().addAll(SearchMode.values());
        searchModeBox.setValue(SearchMode.Title);
        searchButton = new Button("Search");

        // event handling (dispatch to controller)
        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String searchFor = searchField.getText();
                SearchMode mode = searchModeBox.getValue();
                System.out.println("nu searchar jag");
                controller.onSearchSelected(searchFor, mode);
            }
        });
    }

    private void initMenus(Controller controller) {    // la till

        Menu fileMenu = new Menu("File");


        MenuItem connectItem = new MenuItem("Connect to Db");
        connectItem.setOnAction(e -> {
            e.consume();
            controller.connectToServer();
            System.out.println("Connectet to DataBase");
        });

        MenuItem disconnectItem = new MenuItem("Disconnect");
        disconnectItem.setOnAction(e -> {
            e.consume();
            try {
                controller.disconnectFromServer();

            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            System.out.println("Disconnected from server");
        });


        fileMenu.getItems().addAll( connectItem, disconnectItem);



        Menu manageMenu = new Menu("Manage");
        MenuItem addItem = new MenuItem("Add");

        addItem.setOnAction(e -> {
            if(!db.connected())
            {
                showAlertAndWait("Not connected", Alert.AlertType.ERROR);
                return;
            }

            //var dialog= new AddBookDialog();

            controller.showAllAuthors();
            //dialog.loadAuthors();

            Optional<Book> result = addBookDialog.showAndWait();
            //System.out.println("ggggggggggggggg"+result.get().toString());
            result.ifPresent(book -> {
                controller.addBook(book);
            });
        });

        MenuItem removeItem = new MenuItem("Remove");
        removeItem.setOnAction(e -> {
            if (bookClickedOn == null)
            {
                showAlertAndWait("you have not selected a book",Alert.AlertType.ERROR);
                return;
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Do you want to delete selected Book?");
            Optional<ButtonType> buttonType = alert.showAndWait();
            controller.onClickedDelete(bookClickedOn, book -> booksInTable.remove(bookClickedOn));


        });

        manageMenu.getItems().addAll(addItem, removeItem);

        menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu,  manageMenu);
    }



}

