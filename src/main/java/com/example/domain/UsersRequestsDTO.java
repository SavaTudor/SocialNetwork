package com.example.domain;

public class UsersRequestsDTO {
    private User from, to;
    Status status;

    public UsersRequestsDTO(User from, User to, Status status) {
        this.from = from;
        this.to = to;
        this.status = status;
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

    @Override
    public String toString() {
        return "from=" + from + ", to=" + to + ", status=" + status;
    }
}
