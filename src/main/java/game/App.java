package game;

import game.model.BooksDb;
import game.view.BooksView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;


/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException, SQLException {

        BooksDb booksDb = new BooksDb(); // model

        // Don't forget to connect to the db, somewhere...

        BooksView root = new BooksView(booksDb);

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


        launch();
    }

    private static Connection conn;

}




    /*
    public static void main(String[] args) throws SQLException {
        try {
            Class.forName("org.mariadb.jdbc.Driver");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {

            conn = DriverManager.getConnection("jdbc:mariadb://dzeknjak.com:3306/Labb1EmreLo?user=hazard&password=test123&allowPublicKeyRetrieval=true&useSSL=false");

            var rs = conn.createStatement().executeQuery("SELECT * FROM t_book");

            while (rs.next()){   //VARje rad för databasen så skriver man ut
                System.out.println(rs.getString("isbn"));
            }
            conn.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            conn.close();
        }

        launch();
    } */