package com.example.socialnetworkgui;

import com.example.business.Controller;
import com.example.domain.User;
import com.example.repository.database.DataBaseMessageRepository;
import com.example.repository.database.DataBaseUserRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import static com.example.build.Build.*;

public class AddNewFriendController implements Initializable {
    public Button find;
    public TextField findUser;
    public TableView<FindUserModel> userTable;
    public TableColumn<UserModel, String> name;
    public TableColumn<UserModel, String> id;
    private static DataBaseMessageRepository repo;
    private static DataBaseUserRepository repoUser;
    private static Controller service;

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
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        id.setVisible(false);
        userTable.setVisible(false);
    }

    private ObservableList<FindUserModel> loadTable(String name){
        LoginController loginController = new LoginController();
        int id = loginController.getId();
        LinkedList<FindUserModel> friends = new LinkedList<>();
        List<User> users = service.getNoFriend(id);
        users.stream().
                filter(x->(x.getFirstName().equals(name) || x.getLastName().equals(name)) &&
                        x.getId() != id).
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
}
