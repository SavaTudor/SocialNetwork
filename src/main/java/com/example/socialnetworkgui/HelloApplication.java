package com.example.socialnetworkgui;

import com.example.business.Controller;
import com.example.repository.database.DataBaseMessageRepository;
import com.example.repository.database.DataBaseUserRepository;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

import static com.example.build.Build.*;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Controller service;
        service = new Controller(database_url, database_user, database_password);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("login.fxml"));

        AnchorPane root=loader.load();
        LoginController loginController = loader.getController();
        loginController.setService(service);

        stage.setScene(new Scene(root, 800, 400));
        stage.setTitle("Log in");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}