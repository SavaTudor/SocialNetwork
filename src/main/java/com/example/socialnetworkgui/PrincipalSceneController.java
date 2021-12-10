package com.example.socialnetworkgui;

import com.example.business.Controller;
import com.example.domain.Friendship;
import com.example.domain.User;
import com.example.exception.RepositoryException;
import com.example.repository.database.DataBaseMessageRepository;
import com.example.repository.database.DataBaseUserRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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
    public TableColumn data;
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
        firstName.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("LastName"));
        data.setCellValueFactory(new PropertyValueFactory<>("data"));
        friendshipTable.setItems(insertInTable());
    }

    private ObservableList<UserModel> userModels = FXCollections.observableArrayList(
            new UserModel("Amos", "Chepchieng", "11"),
            new UserModel("Keep", "Too", "22")
            );
    private ObservableList<UserModel> insertInTable(){
        LoginController loginController = new LoginController();
        int id = loginController.getId();
        LinkedList<UserModel> friends = new LinkedList<>();
        List<Friendship> friendships = service.allFriendships();
        friendships.stream().
                filter(x->x.getUserA() == id || x.getUserB() == id ).
                forEach(x->{
                    if(x.getUserA() == id)
                    {
                        try {
                            User user = service.findUser(x.getUserB());
                            String firstName = user.getFirstName();
                            String lastName = user.getLastName();
                            LocalDateTime data = x.getDate();
                            UserModel userModel = new UserModel(firstName, lastName, data.toString());
                            friends.add(userModel);
                        } catch (RepositoryException e) {
                            e.printStackTrace();
                        }
                    }
                    if(x.getUserB() == id)
                    {
                        try {
                            User user = service.findUser(x.getUserA());
                            String firstName = user.getFirstName();
                            String lastName = user.getLastName();
                            LocalDateTime data = x.getDate();
                            UserModel userModel = new UserModel(firstName, lastName, data.toString());
                            friends.add(userModel);
                        } catch (RepositoryException e) {
                            e.printStackTrace();
                        }
                    }
                });
        return FXCollections.observableArrayList(friends);
    }

}
