package com.example.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Event extends Entity<Integer> {
    private String name;
    private String description;
    private LocalDate date;

    public Event(String name, String description, LocalDate date) {
        this.name = name;
        this.description = description;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(this.getId(), event.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
