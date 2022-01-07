package com.example.socialnetworkgui;

import com.example.business.Controller;
import com.example.domain.Friendship;
import com.example.domain.Message;
import com.example.domain.MessageDTO;
import com.example.domain.User;
import com.example.exception.RepositoryException;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.net.URL;
import java.util.ResourceBundle;

import java.util.*;

public class Raport1Controller implements Initializable {
    public ScrollPane scrollPane;
    private List<MessageDTO> messageList;
    private List<Friendship> friendships;
    private Controller service;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setLists(Controller service, List<MessageDTO> messageList, List<Friendship> friendships, int user) throws RepositoryException {
        this.service = service;
        this.messageList = messageList;
        this.friendships = friendships;
        AnchorPane anchorPaneRaport = new AnchorPane();
        scrollPane.setContent(anchorPaneRaport);
        int x = 14;
        int y = 23;
        Label label = new Label();
        label.setText("New friends:");
        label.setStyle("-fx-background-radius: 5");
        label.setTextAlignment(TextAlignment.JUSTIFY);
        label.setMaxWidth(300);
        label.setFont(new Font("Arial", 18));
        label.setWrapText(true);
        label.setLayoutX(x);
        label.setLayoutY(y);
        y += 20;
        anchorPaneRaport.getChildren().add(label);

        for(Friendship friendship : friendships){
            Label labelFr = new Label();
            if(friendship.getUserA() != user){
                User user1 = service.findUser(friendship.getUserA());
                labelFr.setText(user1.getFirstName() + " " + user1.getLastName());
            }
            else{
                User user1 = service.findUser(friendship.getUserB());
                labelFr.setText(user1.getFirstName() + " " + user1.getLastName());
            }
            labelFr.setStyle("-fx-background-radius: 5");
            labelFr.setTextAlignment(TextAlignment.JUSTIFY);
            labelFr.setMaxWidth(300);
            labelFr.setFont(new Font("Arial", 18));
            labelFr.setWrapText(true);
            labelFr.setLayoutX(x);
            labelFr.setLayoutY(y);
            y += 20;
            anchorPaneRaport.getChildren().add(labelFr);
        }

        Label label1 = new Label();
        label1.setText("All messages received:");
        label1.setStyle("-fx-background-radius: 5");
        label1.setTextAlignment(TextAlignment.JUSTIFY);
        label1.setMaxWidth(300);
        label1.setFont(new Font("Arial", 18));
        label1.setWrapText(true);
        label1.setLayoutX(x);
        label1.setLayoutY(y);
        y += 20;
        anchorPaneRaport.getChildren().add(label1);
        for(MessageDTO message : messageList){
            Label labelMess = new Label();
            User user1 = service.findUser(message.getFrom());
            String lineMess = user1.getFirstName() + " " + user1.getLastName()  + ":" + message.getMessage();
            labelMess.setText(lineMess);
            labelMess.setStyle("-fx-background-radius: 5");
            labelMess.setTextAlignment(TextAlignment.JUSTIFY);
            labelMess.setMaxWidth(146);
            labelMess.setFont(new Font("Arial", 18));
            labelMess.setWrapText(true);
            labelMess.setLayoutX(x);
            labelMess.setLayoutY(y);
            y += 20;
            anchorPaneRaport.getChildren().add(labelMess);
        }
    }

}

