package game;

import game.model.MockBooksDb;
import game.view.BooksPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.sql.*;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) {

        MockBooksDb booksDb = new MockBooksDb(); // model
        // Don't forget to connect to the db, somewhere...

        BooksPane root = new BooksPane(booksDb);

        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("Books Database Client");
        // add an exit handler to the stage (X) ?
        primaryStage.setOnCloseRequest(event -> {
            try {
                booksDb.disconnect();
            } catch (Exception e) {}
        });
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    public static void main(String[] args) throws SQLException {
        try {
            Class.forName("org.mariadb.jdbc.Driver");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {

            conn = DriverManager.getConnection("jdbc:mariadb://dzeknjak.com:3306/Labb1EmreLo?user=hazard&password=test123&allowPublicKeyRetrieval=true&useSSL=false");

            var rs = conn.createStatement().executeQuery("SELECT * FROM t_book");

            while (rs.next()){
                System.out.println(rs.getString("isbn"));
            }
            conn.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            conn.close();
        }

        launch();
    }

    private static Connection conn;

}