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
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;


public class PrincipalSceneController implements Initializable {

    public TableView<UserModel> friendshipTable;
    public TableColumn<UserModel, String> id;
    public TableColumn<UserModel, String> username;
    public ImageView homeImage;
    public ImageView addFriendImage;
    public ImageView logoButton;
    public ImageView friendImage;
    public ImageView deleteImage;
    public Button deleteButton;
    public Button logOutButton;
    public ImageView logOutImage;
    public Label userAccount;
    public AnchorPane anchorPane;
    public Button messageButton;
    private Controller service;
    private int userId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        username.setCellValueFactory(new PropertyValueFactory<>("username"));
        friendshipTable.setVisible(false);
        id.setVisible(false);
        deleteImage.setVisible(false);


        Image image = new Image("file:images/logo1.jpg");
        logoButton.setImage(image);
        Image image1 = new Image("file:images/homeButtonImage.jpg");
        homeImage.setImage(image1);
        Image image2 = new Image("file:images/friendImage.png");
        friendImage.setImage(image2);
        Image image3 = new Image("file:images/addNewFriendImage.jpg");
        addFriendImage.setImage(image3);
        Image image4 = new Image("file:images/deleteButton.png");
        deleteImage.setImage(image4);
        Image image5 = new Image("file:images/logoutButton.png");
        logOutImage.setImage(image5);

    }

    public void setService(Controller service, int id){
        this.userId = id;
        this.service = service;

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
                            UserModel userModel = new UserModel(user.getId().toString(), firstName + " " + lastName);
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
                            UserModel userModel = new UserModel(user.getId().toString(), firstName + " " + lastName);
                            LocalDateTime data = x.getDate();
                            friends.add(userModel);
                        } catch (RepositoryException e) {
                            e.printStackTrace();
                        }
                    }
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
        Stage stage;
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setTitle("Add new friend");
        stage.setScene(scene);
        stage.show();
    }

    public void homeClicked(ActionEvent actionEvent) {
        friendshipTable.setVisible(false);
        deleteImage.setVisible(false);

    }

    public void friendClicked(ActionEvent mouseEvent) {
        friendshipTable.setVisible(true);
        deleteImage.setVisible(true);
        id.setVisible(false);
        friendshipTable.setItems(loadTable());
    }

    public void deleteClicked(ActionEvent actionEvent) throws EntityException, RepositoryException {
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
        Scene scene = new Scene(root, 750, 400);
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
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            FriendRequestsController friendRequestsController = fxmlLoader.getController();
            friendRequestsController.setService(service, userId);
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

    public void messagesClicked(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("messages.fxml"));
        AnchorPane root = loader.load();
        MessageController messageController = loader.getController();
        messageController.setService(service, userId);
        Scene scene = new Scene(root, 750, 400);
        Stage stage = new Stage();
        stage.setTitle("Messages");
        stage.setScene(scene);
        stage.show();
    }
}
