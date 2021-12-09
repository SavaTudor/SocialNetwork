package com.example.socialnetworkgui;

import com.example.business.Controller;
import com.example.domain.User;
import com.example.repository.database.DataBaseMessageRepository;
import com.example.repository.database.DataBaseUserRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.example.build.Build.*;

public class LoginController {
    private static DataBaseMessageRepository repo;
    private static DataBaseUserRepository repoUser;
    private static Controller service;

    public void initialize() throws SQLException {
        repo = new DataBaseMessageRepository(database_url, database_user, database_password);
        repoUser = new DataBaseUserRepository(database_url, database_user, database_password);
        service = new Controller(database_url, database_user, database_password);
    }

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label usernameLabel;

    @FXML
    public void usernameInserted() {
    }

    @FXML
    public void passwordInserted(ActionEvent actionEvent) {
    }

    @FXML
    private Button signInButton;

    @FXML
    public void signInClicked() throws SQLException {
        ArrayList<User> users = service.allUsers();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        boolean find = false;
        for(User user : users)
            if(usernameField.getText().equals(user.getFirstName()))
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
        else
        {

        }
    }

    @FXML
    public void signUpClicked(ActionEvent actionEvent) {
    }

}
