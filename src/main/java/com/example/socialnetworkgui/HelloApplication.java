package com.example.socialnetworkgui;

import com.example.business.Controller;
import com.example.exception.EntityException;
import com.example.exception.RepositoryException;
import com.example.exception.ValidatorException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import static com.example.build.Build.*;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Controller service;
        service = new Controller(database_url, database_user, database_password);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("login.fxml"));


        AnchorPane root = loader.load();
        LoginController loginController = loader.getController();
        loginController.setService(service);
        stage.setScene(new Scene(root, 750, 400));
        stage.getIcons().add(new Image("C:\\Users\\andre\\Desktop\\Facultate\\Facultate-sem III\\MAP\\socialNetworkGUI\\images\\logo.jpg"));
        stage.setTitle("Log in");
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}