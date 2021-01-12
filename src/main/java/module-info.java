module org.example {
    requires javafx.controls;
    requires java.sql;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;
    requires org.mongodb.driver.sync.client;
    exports game.model;
    exports game;
}