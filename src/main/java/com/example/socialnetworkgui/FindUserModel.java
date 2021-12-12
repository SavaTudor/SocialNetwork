package com.example.socialnetworkgui;

import javafx.beans.property.SimpleStringProperty;

public class FindUserModel {

    private SimpleStringProperty id;
    private SimpleStringProperty name;

    public FindUserModel(String id, String name) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }
}