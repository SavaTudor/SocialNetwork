package com.example.socialnetworkgui;

import com.example.business.Controller;
import com.example.domain.User;
import com.example.exception.RepositoryException;
import com.example.exception.ValidatorException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;


public class AddNewFriendController implements Initializable {
    public Button find;
    public TableView<FindUserModel> userTable;
    public TableColumn<UserModel, String> name;
    public TableColumn<UserModel, String> id;
    private static Controller service;
    public TextField searchField;
    public Button addButton;
    public ImageView searchImage;
    public ImageView addImage;
    public ImageView homeImage;
    public ImageView logoImage;
    public Label invalidUser;
    private int userId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        id.setVisible(false);
        userTable.setVisible(false);
        addButton.setVisible(false);
        Image image = new Image("file:images/searchImage.png");
        searchImage.setImage(image);
        Image image1 = new Image("file:images/addNewFriendImage.jpg");
        addImage.setImage(image1);
        Image image2 = new Image("file:images/logo1.jpg");
        logoImage.setImage(image2);
        Image image3 = new Image("file:images/homeButtonImage.jpg");
        homeImage.setImage(image3);
        invalidUser.setVisible(false);
    }

    public void setService(Controller service, int id){
        this.service = service;
        this.userId = id;
    }

    private ObservableList<FindUserModel> loadTable(String name){
        LinkedList<FindUserModel> friends = new LinkedList<>();
        List<User> users = service.getNoFriend(this.userId);
        users.stream().
                filter(x->(x.getFirstName().equals(name) || x.getLastName().equals(name)) &&
                        x.getId() != this.userId).
                forEach(x->{
                    String id1 = x.getId().toString();
                    String userName = x.getFirstName() + " " + x.getLastName();
                    FindUserModel findUserModel = new FindUserModel(id1, userName);
                    friends.add(findUserModel);
                });
        return FXCollections.observableArrayList(friends);
    }

    public void addClicked(ActionEvent actionEvent){
        ObservableList<FindUserModel> users = userTable.getSelectionModel().getSelectedItems();
        int id1 = Integer.parseInt(users.get(0).getId());
        try {
            service.addFriendRequest(this.userId, id1);
        } catch (ValidatorException | RepositoryException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(e.getMessage());
            alert.setContentText("Try again");
            alert.setTitle("Warning");
            alert.show();
        }
    }

    public void homeClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("principalScene.fxml"));
        AnchorPane root = loader.load();
        PrincipalSceneController principalSceneController = loader.getController();
        principalSceneController.setService(service, userId);
        Scene scene = new Scene(root, 800, 400);
        Stage stage;
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setTitle("Main scene");
        stage.setScene(scene);
        stage.show();
    }

    public void searchCliecked(ActionEvent actionEvent) {
        String userName = searchField.getText();
        ObservableList<FindUserModel> userModels = loadTable(userName);
        if(userModels.size() == 0)
            invalidUser.setVisible(true);
        else {
            userTable.setItems(userModels);
            userTable.setVisible(true);
            addButton.setVisible(true);
            invalidUser.setVisible(false);
        }
    }
}
