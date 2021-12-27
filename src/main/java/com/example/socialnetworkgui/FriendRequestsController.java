package com.example.socialnetworkgui;

import com.example.business.Controller;
import com.example.domain.FriendRequest;
import com.example.domain.Status;
import com.example.domain.User;
import com.example.domain.UsersRequestsDTO;
import com.example.exception.EntityException;
import com.example.exception.RepositoryException;
import com.example.exception.ValidatorException;
import com.example.repository.database.DataBaseMessageRepository;
import com.example.repository.database.DataBaseRequestsRepository;
import com.example.repository.database.DataBaseUserRepository;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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

public class FriendRequestsController implements Initializable {

    public TableView<RequestModel> requestsTable;
    public TableColumn<RequestModel, String> firstName;
    public TableColumn<RequestModel, String> lastName;
    public TableColumn<RequestModel, String> id;
    public TableColumn data;
    public TableColumn status;
    private Controller service;
    private int userId;

    @FXML
    public Button closeButton, acceptButton, declineButton, acceptAllButton, myFriendRequests;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        LoginController loginController = new LoginController();
        this.userId = loginController.getId();

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstName.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("LastName"));
        data.setCellValueFactory(new PropertyValueFactory<>("data"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        id.setVisible(true);

    }

    public void setService(Controller service) {
        this.service = service;
        requestsTable.setItems(loadTable());

    }


    private ObservableList<RequestModel> loadTable() {
        LinkedList<RequestModel> requests = new LinkedList<>();
        List<UsersRequestsDTO> friendRequests = service.getFriendRequests(userId);
        friendRequests.stream().
                forEach(x -> {
                    if (x.getTo().getId() == this.userId) {
                        User user = x.getFrom();
                        String firstName = user.getFirstName();
                        String lastName = user.getLastName();
                        LocalDateTime data = x.getDate();
                        Status status = x.getStatus();
                        RequestModel requestModel = new RequestModel(user.getId().toString(), firstName, lastName, data.format(formatter), status
                                .toString());
                        requests.add(requestModel);
                    }
                });
        return FXCollections.observableArrayList(requests);
    }

    public void acceptRequest(ActionEvent actionEvent) throws ValidatorException, EntityException, RepositoryException {
        ObservableList<RequestModel> requestModels = requestsTable.getSelectionModel().getSelectedItems();
        if (requestModels.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Select a request!");
            alert.setTitle("Warning");
            alert.show();
        } else {
            int from = Integer.parseInt(requestModels.get(0).getId());
            service.respondFriendRequest(from, userId, "APPROVE");
            requestsTable.setItems(loadTable());
        }
    }

    public void declineRequest(ActionEvent actionEvent) throws ValidatorException, EntityException, RepositoryException {
        ObservableList<RequestModel> requestModels = requestsTable.getSelectionModel().getSelectedItems();
        if (requestModels.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Select a request!");
            alert.setTitle("Warning");
            alert.show();
        } else {
            int from = Integer.parseInt(requestModels.get(0).getId());
            service.respondFriendRequest(from, userId, "DECLINE");
            requestsTable.setItems(loadTable());
        }
    }

    public void returnB(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    public void acceptAll(ActionEvent actionEvent) {
        service.respondToAllRequests(userId, "APPROVE");
        requestsTable.setItems(loadTable());
    }

    public ObservableList<RequestModel> loadSentRequests() {
        LinkedList<RequestModel> requests = new LinkedList<>();
        List<UsersRequestsDTO> friendRequests = service.sentFriendRequests(userId);
        friendRequests.stream().
                forEach(x -> {
                    if (x.getFrom().getId() == this.userId) {
                        User user = x.getTo();
                        String firstName = user.getFirstName();
                        String lastName = user.getLastName();
                        LocalDateTime data = x.getDate();
                        Status status = x.getStatus();
                        RequestModel requestModel = new RequestModel(user.getId().toString(), firstName, lastName, data.format(formatter), status
                                .toString());
                        requests.add(requestModel);
                    }
                });
        return FXCollections.observableArrayList(requests);
    }


    public void myRequests(ActionEvent actionEvent) {
        requestsTable.setItems(loadSentRequests());
        acceptButton.setDisable(true);
        acceptAllButton.setDisable(true);
        myFriendRequests.setDisable(true);
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                ObservableList<RequestModel> requestModels = requestsTable.getSelectionModel().getSelectedItems();
                if (requestModels.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Select a request!");
                    alert.setTitle("Warning");
                    alert.show();
                } else {
                    int to = Integer.parseInt(requestModels.get(0).getId());
                    try {
                        service.deleteFriendRequest(userId, to);
                    } catch (RepositoryException repositoryException) {
                        repositoryException.printStackTrace();
                    }
                    requestsTable.setItems(loadSentRequests());
                }
            }
        };
        declineButton.setOnAction(event);
        EventHandler<ActionEvent> exit = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                requestsTable.setItems(loadTable());
                acceptButton.setDisable(false);
                acceptAllButton.setDisable(false);
                myFriendRequests.setDisable(false);
            }
        };
        closeButton.setOnAction(exit);
    }
}
