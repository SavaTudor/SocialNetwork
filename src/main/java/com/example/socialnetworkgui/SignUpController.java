package com.example.socialnetworkgui;

import com.example.business.Controller;
import com.example.exception.RepositoryException;
import com.example.exception.ValidatorException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class SignUpController {
    public ImageView signUpImage;
    private Controller service;
    public ImageView beeImage;

    public void initialize() throws SQLException {
        Image image = new Image("C:\\Users\\andre\\Desktop\\Facultate\\Facultate-sem III\\MAP\\socialNetworkGUI\\images/beeLogInImage3.jpg");
        beeImage.setImage(image);
        Image image1 = new Image("C:\\Users\\andre\\Desktop\\Facultate\\Facultate-sem III\\MAP\\socialNetworkGUI\\images\\signInImage.png");
        signUpImage.setImage(image1);
    }

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private PasswordField passwordField;

    public void setService(Controller service){
        this.service = service;
    }

    public void loginButtonClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("login.fxml"));
        AnchorPane root = loader.load();
        LoginController loginController = loader.getController();
        loginController.setService(service);
        Scene scene = new Scene(root, 750, 400);
        Stage stage;
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setTitle("LogIn");
        stage.setScene(scene);
        stage.show();
    }

    public void newAccountClicked(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        try {
            service.add(firstName, lastName);
            SceneController controller = new SceneController();
            controller.switchScene(service, "login.fxml", "Add new friend", actionEvent);
        } catch (RepositoryException | ValidatorException | IOException e) {
            alert.setTitle("Message Here...");
            alert.setHeaderText("Incorrect user");
            alert.setContentText(e.getMessage());
            alert.setTitle("Warning");
            alert.show();
        }

    }
}
