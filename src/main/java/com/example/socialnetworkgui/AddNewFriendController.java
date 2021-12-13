package com.example.socialnetworkgui;

import com.example.business.Controller;
import com.example.domain.User;
import com.example.exception.RepositoryException;
import com.example.exception.ValidatorException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;


public class AddNewFriendController implements Initializable {
    public Button find;
    public TextField findUser;
    public TableView<FindUserModel> userTable;
    public TableColumn<UserModel, String> name;
    public TableColumn<UserModel, String> id;
    private static Controller service;
    private int userId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        id.setVisible(false);
        userTable.setVisible(false);

        LoginController loginController = new LoginController();
        this.userId = loginController.getId();
    }

    public void setService(Controller service){
        this.service = service;
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

    public void findButton(ActionEvent actionEvent) {
        String userName = findUser.getText();
        userTable.setItems(loadTable(userName));
        userTable.setVisible(true);
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
}
