package com.example.domain;

import java.time.LocalDateTime;

import static com.example.build.Build.formatter;

public class UsersFriendsDTO {
    private User usera, userb;
    LocalDateTime date;

    public UsersFriendsDTO(User usera, User userb, LocalDateTime date) {
        this.usera = usera;
        this.userb = userb;
        this.date = date;
    }

    public User getUsera() {
        return usera;
    }

    public User getUserb() {
        return userb;
    }

    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public String toString() {
        return userb.getLastName() + " | " + userb.getFirstName() + " | "  + date.format(formatter);
    }
}
