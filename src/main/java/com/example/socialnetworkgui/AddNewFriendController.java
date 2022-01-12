package com.example.socialnetworkgui;

import com.example.business.Controller;
import com.example.domain.User;
import com.example.exception.RepositoryException;
import com.example.exception.ValidatorException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;


public class AddNewFriendController implements Initializable {
    public Button find;
    public TableView<UserModel> userTable;
    public TableColumn<UserModel, String> username;
    public TableColumn<UserModel, String> id;
    private static Controller service;
    public TextField searchField;
    public Button addButton;
    public ImageView searchImage;
    public ImageView addImage;
    public ImageView homeImage;
    public Label invalidUser;
    public TableColumn<UserModel, String> firstname;
    public TableColumn<UserModel, String> lastname;
    public Button homeButton;
    public ImageView background;
    private int userId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PrincipalSceneController.setCell(id, username, firstname, lastname);
        userTable.setVisible(false);
        addButton.setVisible(false);
        Image image = new Image("file:images/searchButton.png");
        searchImage.setImage(image);
        Image image1 = new Image("file:images/addNewFriendImage.jpg");
        addImage.setImage(image1);
        Image image3 = new Image("file:images/homeButtonImage.jpg");
        homeImage.setImage(image3);
        Image image4 = new Image("file:images/back.jpg");
        background.setImage(image4);
        invalidUser.setVisible(false);
        addImage.setVisible(false);
    }

    public void setService(Controller service, int id){
        AddNewFriendController.service = service;
        this.userId = id;
    }

    private ObservableList<UserModel> loadTable(String name){
        List<UserModel> friends = new ArrayList<>();
        List<User> users = service.getNoFriend(this.userId, name);
        users.stream().
                forEach(x->{
                    String id1 = x.getId().toString();
                    String userName = x.getUsername();
                    String firstname = x.getFirstName();
                    String lastname = x.getLastName();
                    UserModel userModel = new UserModel(id1, userName, firstname, lastname);
                    friends.add(userModel);
                });
        return FXCollections.observableArrayList(friends);
    }

    public void addClicked(){
        ObservableList<UserModel> users = userTable.getSelectionModel().getSelectedItems();
        if(users.isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Add new friend error");
            alert.setContentText("Please select a column from table and press the Add new friend button");
            alert.show();
            return;
        }
        int id1 = Integer.parseInt(users.get(0).getId());
        try {
            service.addFriendRequest(this.userId, id1);
        } catch (ValidatorException | RepositoryException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.setHeaderText("Try again");
            alert.show();
        }
    }

    public void homeClicked(){
        Stage stage = (Stage) homeButton.getScene().getWindow();
        stage.close();
    }

    public void searchClicked() {
        if(searchField.getText().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Search error");
            alert.setContentText("Please give a name");
            alert.show();
        }
        String userName = searchField.getText();
        ObservableList<UserModel> userModels = loadTable(userName);
        if(userModels.size() == 0)
            invalidUser.setVisible(true);
        else {
            userTable.setItems(userModels);
            userTable.setVisible(true);
            addButton.setVisible(true);
            addImage.setVisible(true);
            invalidUser.setVisible(false);
        }
    }
}
