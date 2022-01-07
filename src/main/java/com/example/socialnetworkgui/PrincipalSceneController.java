package com.example.socialnetworkgui;

import com.example.business.Controller;
import com.example.domain.Friendship;
import com.example.domain.Message;
import com.example.domain.User;
import com.example.domain.UsersFriendsDTO;
import com.example.exception.EntityException;
import com.example.exception.RepositoryException;
import com.example.exception.ValidatorException;
import com.example.repository.database.DataBaseMessageRepository;
import com.example.repository.database.DataBaseUserRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;


public class PrincipalSceneController implements Initializable, Observer {

    public TableView<UserModel> friendshipTable;
    public TableColumn<UserModel, String> id;
    public TableColumn<UserModel, String> username;
    public ImageView addFriendImage;
    public ImageView deleteImage;
    public Button deleteButton;
    public Button logOutButton;
    public ImageView logOutImage;
    public Label userAccount;
    public AnchorPane anchorPane;
    public Button messageButton;
    public ImageView messageImage;
    public TableColumn<UserModel, String> firstname;
    public TableColumn<UserModel, String> lastname;
    private Controller service;
    private int userId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        username.setCellValueFactory(new PropertyValueFactory<>("username"));
        firstname.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        lastname.setCellValueFactory(new PropertyValueFactory<>("lastname"));

        id.setVisible(false);

        Image image3 = new Image("file:images/addNewFriendImage.jpg");
        addFriendImage.setImage(image3);
        Image image4 = new Image("file:images/deleteButton.png");
        deleteImage.setImage(image4);
        Image image5 = new Image("file:images/logoutButton.png");
        logOutImage.setImage(image5);
        Image image6 = new Image("file:images/messageButton.png");
        messageImage.setImage(image6);

    }

    public void setService(Controller service, int id){
        this.userId = id;
        this.service = service;
        service.addObserver(this);
        try {
            userAccount.setText(service.findUser(userId).getFirstName() + " " + service.findUser(userId).getLastName());
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
        try {
            friendshipTable.setItems(loadTable());
        } catch (ValidatorException | RepositoryException e) {
            e.printStackTrace();
        }
    }


    private ObservableList<UserModel> loadTable() throws ValidatorException, RepositoryException {
        LinkedList<UserModel> friends = new LinkedList<>();
        List<User> users = service.getFriendsForAUser(userId);
        users.stream().
                forEach(x -> {
                    UserModel userModel = new UserModel(x.getId().toString(), x.getUsername(), x.getFirstName(), x.getLastName());
                    friends.add(userModel);

                });
        return FXCollections.observableArrayList(friends);
    }

    public void addFriendClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("addNewFriend.fxml"));
        AnchorPane root = loader.load();
        AddNewFriendController addNewFriendController = loader.getController();
        addNewFriendController.setService(service, userId);
        Scene scene = new Scene(root, 800, 400);
        Stage stage = new Stage();
        stage.setTitle("Add new friend");
        stage.setScene(scene);
        stage.show();
    }

    public void deleteClicked(ActionEvent actionEvent) throws EntityException, RepositoryException, ValidatorException {
        ObservableList<UserModel> users = friendshipTable.getSelectionModel().getSelectedItems();
        int id = Integer.parseInt(users.get(0).getId());
        service.removeFriends(this.userId, id);
        friendshipTable.setItems(loadTable());
    }

    public void logOutClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("login.fxml"));
        AnchorPane root = loader.load();
        LoginController loginController = loader.getController();
        loginController.setService(service);
        Scene scene = new Scene(root, 800, 400);
        Stage stage;
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setTitle("LogIn");
        stage.setScene(scene);
        stage.show();
    }

    public void friendRequestsClicked(ActionEvent actionEvent) throws IOException {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("friendRequests.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load(), 800, 400);
            FriendRequestsController friendRequestsController = fxmlLoader.getController();
            friendRequestsController.setService(service, userId);
            stage.setTitle("Friend Requests");
            stage.setScene(scene);
            stage.show();
            friendshipTable.setItems(loadTable());
        } catch (IOException | ValidatorException | RepositoryException e) {
            e.printStackTrace();
        }
    }

    public void refresh(ActionEvent actionEvent) throws ValidatorException, RepositoryException {
        friendshipTable.setItems(loadTable());
    }

    public void messagesClicked(ActionEvent actionEvent) throws IOException, RepositoryException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("messages.fxml"));
        AnchorPane root = loader.load();
        MessageController messageController = loader.getController();
        messageController.setService(service, userId);
        Scene scene = new Scene(root, 800, 400);
        Stage stage = new Stage();
        stage.setTitle("Messages");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void update(Observable o, Object arg) {
        try {
            friendshipTable.setItems(loadTable());
        } catch (ValidatorException | RepositoryException e) {
            e.printStackTrace();
        }
    }
}
