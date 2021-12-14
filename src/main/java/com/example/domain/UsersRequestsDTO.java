package com.example.domain;

import java.time.LocalDateTime;

public class UsersRequestsDTO {
    private User from, to;
    Status status;
    private LocalDateTime date;

    public UsersRequestsDTO(User from, User to, Status status) {
        this.from = from;
        this.to = to;
        this.status = status;
        date = LocalDateTime.now();
    }

    public UsersRequestsDTO(User from, User to, Status status, LocalDateTime date) {
        this.from = from;
        this.to = to;
        this.status = status;
        this.date = date;
    }

    public User getFrom() {
        return from;
    }

    public User getTo() {
        return to;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "from=" + from + ", to=" + to + ", status=" + status;
    }
}
