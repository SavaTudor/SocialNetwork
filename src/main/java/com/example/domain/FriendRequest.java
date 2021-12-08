package com.example.domain;

import java.util.Objects;

public class FriendRequest extends Entity<Integer> {
    int from, to;
    Status status;

    public FriendRequest(int usera, int userb, Status status) {
        this.from = usera;
        this.to = userb;
        this.status = status;
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
