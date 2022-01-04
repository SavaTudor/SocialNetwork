package com.example.socialnetworkgui;

import javafx.beans.property.SimpleStringProperty;

public class UserModel {

    private SimpleStringProperty id;
    private SimpleStringProperty username;
    private SimpleStringProperty firstname;
    private SimpleStringProperty lastname;


    public UserModel(String id, String username, String firstname, String lastname) {
        this.id = new SimpleStringProperty(id);
        this.username = new SimpleStringProperty(username);
        this.firstname = new SimpleStringProperty(firstname);
        this.lastname = new SimpleStringProperty(lastname);
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getFirstname() {
        return firstname.get();
    }

    public SimpleStringProperty firstnameProperty() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname.set(firstname);
    }

    public String getLastname() {
        return lastname.get();
    }

    public SimpleStringProperty lastnameProperty() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname.set(lastname);
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