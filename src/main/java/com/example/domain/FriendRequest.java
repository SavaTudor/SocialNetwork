package com.example.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class FriendRequest extends Entity<Integer> {
    int from, to;
    Status status;
    LocalDateTime date;

    public FriendRequest(int usera, int userb, Status status, LocalDateTime date) {
        this.from = usera;
        this.to = userb;
        this.status = status;
        this.date = date;
    }

    public FriendRequest(int from, int to, Status status) {
        this.from = from;
        this.to = to;
        this.status = status;
        this.date = LocalDateTime.now();
    }

    public LocalDateTime getDate() {
        return date;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendRequest that = (FriendRequest) o;
        return from == that.from && to == that.to;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "from=" + from + ", to=" + to + ", status=" + status;
    }

    public boolean isFrom(int id) {
        return from == id;
    }

    public boolean isTo(int id) {
        return to == id;
    }
}
