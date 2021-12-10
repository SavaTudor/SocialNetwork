package com.example.socialnetworkgui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchScene(String nameScena, String title, ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(nameScena));
        Scene scene = new Scene(fxmlLoader.load(), 650, 380);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
}
