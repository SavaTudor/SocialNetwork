package com.example.socialnetworkgui;

import com.example.business.Controller;
import com.example.domain.Friendship;
import com.example.domain.User;
import com.example.exception.EntityException;
import com.example.exception.RepositoryException;
import com.example.repository.database.DataBaseMessageRepository;
import com.example.repository.database.DataBaseUserRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import static com.example.build.Build.*;

public class PrincipalSceneController implements Initializable {

    public TableView<UserModel> friendshipTable;
    public TableColumn<UserModel, String> firstName;
    public TableColumn<UserModel, String> lastName;
    public TableColumn<UserModel, String> id;
    public TableColumn data;
    private static DataBaseMessageRepository repo;
    private static DataBaseUserRepository repoUser;
    private static Controller service;
    private int userId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            repo = new DataBaseMessageRepository(database_url, database_user, database_password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            repoUser = new DataBaseUserRepository(database_url, database_user, database_password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        service = new Controller(database_url, database_user, database_password);

        LoginController loginController = new LoginController();
        this.userId = loginController.getId();

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstName.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("LastName"));
        data.setCellValueFactory(new PropertyValueFactory<>("data"));
        id.setVisible(false);
        friendshipTable.setItems(loadTable());
    }

    private ObservableList<UserModel> loadTable() {
        LinkedList<UserModel> friends = new LinkedList<>();
        List<Friendship> friendships = service.allFriendships();
        System.out.println(userId);
        friendships.stream().
                filter(x -> x.getUserA() == this.userId || x.getUserB() == this.userId).
                forEach(x -> {
                    if (x.getUserA() == this.userId) {
                        try {
                            User user = service.findUser(x.getUserB());
                            String firstName = user.getFirstName();
                            String lastName = user.getLastName();
                            LocalDateTime data = x.getDate();
                            UserModel userModel = new UserModel(user.getId().toString(), firstName, lastName, data.format(formatter));
                            friends.add(userModel);
                        } catch (RepositoryException e) {
                            e.printStackTrace();
                        }
                    }
                    if (x.getUserB() == this.userId) {
                        try {
                            User user = service.findUser(x.getUserA());
                            String firstName = user.getFirstName();
                            String lastName = user.getLastName();
                            LocalDateTime data = x.getDate();
                            UserModel userModel = new UserModel(user.getId().toString(), firstName, lastName, data.format(formatter));
                            friends.add(userModel);
                        } catch (RepositoryException e) {
                            e.printStackTrace();
                        }
                    }
                });
        return FXCollections.observableArrayList(friends);
    }

    public void deleteButtonClicked(ActionEvent actionEvent) throws EntityException, RepositoryException {
        ObservableList<UserModel> users = friendshipTable.getSelectionModel().getSelectedItems();
        int id = Integer.parseInt(users.get(0).getId());
        service.removeFriends(this.userId, id);
        friendshipTable.setItems(loadTable());
    }

    public void addFriendClicked(ActionEvent actionEvent) throws IOException {
        SceneController controller = new SceneController();
        controller.switchScene("addNewFriend.fxml", "LogIn", actionEvent);
    }

    public void friendRequestsClicked(ActionEvent actionEvent) throws IOException {
//        SceneController controller = new SceneController();
//        controller.switchScene("friendRequests.fxml", "Friend Requests", actionEvent);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("friendRequests.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            stage.setTitle("Friend Requests");
            stage.setScene(scene);
            stage.show();
            friendshipTable.setItems(loadTable());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refresh(ActionEvent actionEvent) {
        friendshipTable.setItems(loadTable());
    }
}
