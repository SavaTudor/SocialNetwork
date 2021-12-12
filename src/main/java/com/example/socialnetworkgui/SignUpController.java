package com.example.socialnetworkgui;

import com.example.business.Controller;
import com.example.exception.RepositoryException;
import com.example.exception.ValidatorException;
import com.example.repository.database.DataBaseMessageRepository;
import com.example.repository.database.DataBaseUserRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.sql.SQLException;

import static com.example.build.Build.*;

public class SignUpController {

    private static DataBaseMessageRepository repo;
    private static DataBaseUserRepository repoUser;
    private static Controller service;
    public ImageView beeImage;

    public void initialize() throws SQLException {
        repo = new DataBaseMessageRepository(database_url, database_user, database_password);
        repoUser = new DataBaseUserRepository(database_url, database_user, database_password);
        service = new Controller(database_url, database_user, database_password);
        Image image = new Image("C:\\Users\\andre\\Desktop\\Facultate\\Facultate-sem III\\MAP\\socialNetworkGUI\\images/loginImage.png");
        beeImage.setImage(image);
    }

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private PasswordField passwordField;

    public void loginButtonClicked(ActionEvent actionEvent) throws IOException {
        SceneController controller = new SceneController();
        controller.switchScene("login.fxml", "LogIn", actionEvent);
    }

    public void newAccountClicked(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        try {
            service.add(firstName, lastName);
            SceneController controller = new SceneController();
            controller.switchScene("login.fxml", "Add new friend", actionEvent);
        } catch (RepositoryException | ValidatorException | IOException e) {
            alert.setTitle("Message Here...");
            alert.setHeaderText("Incorrect user");
            alert.setContentText(e.getMessage());
            alert.setTitle("Warning");
            alert.show();
        }

    }
}
