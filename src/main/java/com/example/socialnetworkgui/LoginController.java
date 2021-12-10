package com.example.socialnetworkgui;

import com.example.business.Controller;
import com.example.domain.User;
import com.example.repository.database.DataBaseMessageRepository;
import com.example.repository.database.DataBaseUserRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.example.build.Build.*;

public class LoginController {
    private static DataBaseMessageRepository repo;
    private static DataBaseUserRepository repoUser;
    private static Controller service;
    private static int id;
    @FXML
    private TextField usernameField;

    public void initialize() throws SQLException {
        repo = new DataBaseMessageRepository(database_url, database_user, database_password);
        repoUser = new DataBaseUserRepository(database_url, database_user, database_password);
        service = new Controller(database_url, database_user, database_password);
    }

    @FXML
    private PasswordField passwordField;


    public int getId(){
        return this.id;
    }

    @FXML
    public void signInClicked(ActionEvent event) throws SQLException, IOException {
        this.id = Integer.parseInt(usernameField.getText());
        ArrayList<User> users = service.allUsers();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        boolean find = false;
        for(User user : users)
            if(usernameField.getText().equals(user.getId().toString()))
            {
                find = true;
                break;

            }
        if(!find)
        {
            alert.setTitle("Message Here...");
            alert.setHeaderText("Incorrect user");
            alert.setContentText("Try again");
            alert.setTitle("Warning");
            alert.show();
        }
        else {
            SceneController controller = new SceneController();
            controller.switchScene("principalScene.fxml", "Principal page", event);
        }
    }

    @FXML
    public void signUpClicked(ActionEvent event) throws IOException {
        SceneController controller = new SceneController();
        controller.switchScene("signUp.fxml", "SingUp", event);
    }
}
