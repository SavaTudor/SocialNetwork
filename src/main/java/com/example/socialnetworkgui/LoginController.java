package com.example.socialnetworkgui;

import com.example.business.Controller;
import com.example.domain.User;
import com.example.exception.RepositoryException;
import com.example.utils.Encryption;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
public class LoginController {
    public ImageView lockImage;
    private Controller service;
    private int id;
    public ImageView beeImage;

    @FXML
    private TextField usernameField;

    public void initialize() throws SQLException {
        Image image = new Image("file:images/beeLogInImage3.jpg");
        beeImage.setImage(image);
        Image image1 = new Image("file:images/lockImage1.png");
        lockImage.setImage(image1);
    }

    public void setService(Controller service){
        this.service = service;
    }

    @FXML
    private PasswordField passwordField;

    @FXML
    public void signInClicked(ActionEvent event) throws SQLException, IOException {
        Encryption encryption = new Encryption();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        try {
            String username = usernameField.getText();
            String password = passwordField.getText();
            this.id = service.getUserByUsernameAndPassword(username, password);


            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("principalScene.fxml"));        AnchorPane root = loader.load();
            PrincipalSceneController principalSceneController = loader.getController();
            principalSceneController.setService(service, id);
            Scene scene = new Scene(root, 800, 400);
            Stage stage;
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setTitle("Main scene");
            stage.setScene(scene);
            stage.show();

        } catch (RepositoryException e) {
            alert.setHeaderText("Incorrect username or password");
            alert.setTitle("Warning");
            alert.show();
        }
    }

    @FXML
    public void signUpClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("signUp.fxml"));        AnchorPane root = loader.load();
        SignUpController signUpController = loader.getController();
        signUpController.setService(service);
        Scene scene = new Scene(root, 800, 400);
        Stage stage;
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setTitle("Sign up");
        stage.setScene(scene);
        stage.show();

    }
}
