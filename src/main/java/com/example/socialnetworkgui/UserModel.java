package com.example.socialnetworkgui;

import javafx.beans.property.SimpleStringProperty;

public class UserModel {

    private SimpleStringProperty id;
    private SimpleStringProperty username;


    public UserModel(String id, String username) {
        this.id = new SimpleStringProperty(id);
        this.username = new SimpleStringProperty(username);
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getUsername() {
        return username.get();
    }

    public SimpleStringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }
}