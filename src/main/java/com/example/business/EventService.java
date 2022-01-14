package com.example.business;

import com.example.domain.Event;
import com.example.domain.User;
import com.example.domain.ValidatorEvent;
import com.example.exception.RepositoryException;
import com.example.exception.ValidatorException;
import com.example.repository.Repository;
import com.example.repository.database.DataBaseEventRepository;
import com.example.repository.database.DataBaseUserRepository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

public class EventService {
    private Connection connection;
    private Statement statement;
    Repository<Integer, Event> repository;
    Repository<Integer, User> userRepository;
    ValidatorEvent validatorEvent;
    private int id = 1;

    public EventService(Connection connection, Statement statement) {
        this.connection = connection;
        this.statement = statement;
        validatorEvent = new ValidatorEvent();
        repository = new DataBaseEventRepository(connection, statement);
        userRepository = new DataBaseUserRepository(connection, statement);
        generateId();
    }

    public int size() {
        return repository.size();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    private void generateId() {
        String sql = "SELECT * FROM events ORDER BY ev_id DESC;";
        try {
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                this.id = resultSet.getInt("ev_id") + 1;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void addNewEvent(String name, String description, LocalDate date) throws ValidatorException, RepositoryException {
        Event event = new Event(name, description, date);
        event.setId(id);
        validatorEvent.valideaza(event);
        repository.add(id, event);
        id++;
    }

    public List<Event> all() {
        return repository.all();
    }

    public void removeEvent(int id) throws RepositoryException {
        repository.remove(id);
    }

    public Event find(int id) throws RepositoryException {
        return repository.find(id);
    }

    public void updateEvent(int id, String name, String description, LocalDate date) throws ValidatorException, RepositoryException {
        Event event = new Event(name, description, date);
        event.setId(id);
        validatorEvent.valideaza(event);
        repository.update(id, event);
    }

}
