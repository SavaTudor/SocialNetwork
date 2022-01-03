package com.example.socialnetworkgui;

import com.example.business.Controller;
import com.example.domain.Friendship;
import com.example.domain.Message;
import com.example.domain.User;
import com.example.domain.UsersFriendsDTO;
import com.example.exception.RepositoryException;
import com.example.exception.ValidatorException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Pair;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class MessageController implements Initializable, Observer {
    public ScrollPane scrollPaneMessage;
    public AnchorPane anchorPaneFriends;
    public TextField messageField;
    public ScrollPane scrollPaneFriends;
    public Button deleteButton;
    public Button replyButton;
    private Controller service;
    private int userId;
    private int toId;
    private Map<Integer, Button> buttons;
    private List<Label> messageLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buttons = new HashMap<>();
    }
    public void setService(Controller service, int id){
        this.service = service;
        service.addObserver(this);
        this.userId = id;
        try {
            showFriend();
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
    }

    public void showMessage() throws RepositoryException {
        AnchorPane anchorPane = new AnchorPane();
        scrollPaneMessage.setContent(anchorPane);
        List<Message> messageList = service.getConversation(toId, userId);
        if(messageList.size() == 0)
        {
            Label label = new Label();
            label.setText("no message found ");
            label.setStyle("-fx-background-radius: 5; -fx-background-color:  white");
            label.setLayoutX(197);
            label.setLayoutY(160);
            label.setTextAlignment(TextAlignment.JUSTIFY);
            anchorPane.getChildren().add(label);
        }
        int y = 21;
        messageLabel = new ArrayList<>();
        for(Message message : messageList){
            Label labelMess = new Label();
            labelMess.setText(message.getMessage());
            labelMess.setStyle("-fx-background-radius: 5; -fx-background-color:  #fad907");
            labelMess.setTextAlignment(TextAlignment.JUSTIFY);
            labelMess.setMaxWidth(146);
            labelMess.setFont(new Font("Arial", 18));
            labelMess.setWrapText(true);

            if(message.getReply() != null)
            {
                Label replyLabel = new Label();
                replyLabel.setText(message.getReply().getMessage());
                replyLabel.setStyle("-fx-background-radius: 5; -fx-background-color:  #b9b1b1");
                replyLabel.setTextAlignment(TextAlignment.JUSTIFY);
                if(message.getFrom().getId() == userId)
                    replyLabel.setLayoutX(250);
                else
                    replyLabel.setLayoutX(25);
                replyLabel.setLayoutY(y);
                anchorPane.getChildren().add(replyLabel);
                y += 20;
            }

            if(message.getFrom().getId() == userId)
                labelMess.setLayoutX(250);
            else
                labelMess.setLayoutX(25);
            labelMess.setLayoutY(y);
            y += (40 + (message.getMessage().length() / 4) * 6);
            anchorPane.getChildren().add(labelMess);
            messageLabel.add(labelMess);
            labelMess.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if(mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                        if (mouseEvent.getClickCount() == 1) {
                            try {
                                deleteClicked(message.getId());
                                replyClicked(message.getId());
                            } catch (RepositoryException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
            //deleteButton.setVisible(false);
        }
    }
    public void showFriend() throws RepositoryException {
        int y = 21;
        List<UsersFriendsDTO> friends = service.getFriends(userId);
        for(UsersFriendsDTO friend : friends){
            Button friendButton = new Button();
            if(friend.getUsera().getId() != userId) {
                friendButton.setText(friend.getUsera().getFirstName() + " " + friend.getUsera().getLastName());
                friendButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try {
                            toId = friend.getUsera().getId();
                            showMessage();
                        } catch (RepositoryException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            else
            {
                friendButton.setText(friend.getUserb().getFirstName() + " " + friend.getUserb().getLastName());
                friendButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try {
                            toId = friend.getUserb().getId();
                            System.out.println(toId);
                            showMessage();
                        } catch (RepositoryException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            friendButton.setLayoutX(25);
            friendButton.setLayoutY(y);
            y += 40;
            anchorPaneFriends.getChildren().add(friendButton);
        }
    }

    public void sendClicked(ActionEvent actionEvent) throws RepositoryException, ValidatorException {
        String mess = messageField.getText();
        service.addNewMessage(userId, Arrays.asList(toId), mess);
        messageField.deleteText(0, mess.length());
    }

    public void deleteClicked(int id) throws RepositoryException {
        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    service.removeMessage(id);
                } catch (RepositoryException e) {
                }
            }
        });
    }

    public void replyClicked(int id){
        replyButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    String mess = messageField.getText();
                    service.replyMessage(userId, Arrays.asList(toId), mess, id);
                    messageField.deleteText(0, mess.length());
                } catch (RepositoryException | ValidatorException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void replyAllClicked(ActionEvent actionEvent) throws ValidatorException, RepositoryException {
        String mess = messageField.getText();
        service.replyAll(userId, mess);
        messageField.deleteText(0, mess.length());
    }

    @Override
    public void update(Observable o, Object arg) {
        try {
            showFriend();
            showMessage();
        } catch (RepositoryException e) {
        }
    }
}
