package com.example.repository.database;

import com.example.build.Build;
import com.example.domain.Event;
import com.example.domain.Friendship;
import com.example.exception.RepositoryException;
import com.example.repository.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class DataBaseEventRepository implements Repository<Integer, Event> {
    private final Connection connection;
    private final Statement statement;
    private int page = -1;
    private int size = 5;
    private int offset = 0;

    public DataBaseEventRepository(Connection connection, Statement statement) {
        this.connection = connection;
        this.statement = statement;
    }

    @Override
    public int size() {
        ResultSet rs;
        int size = 0;
        try {
            rs = statement.executeQuery("SELECT * FROM events;");
            while (rs.next()) {
                size += 1;
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public void add(Integer integer, Event event) throws RepositoryException {
        String sql = "INSERT INTO events(ev_id, name, description, date) VALUES (" + integer.toString() +
                ",'" + event.getName() + "','" + event.getDescription() + "','" + event.getDate().toString() + "');";
        try {
            statement.executeUpdate(sql);
        } catch (SQLException throwables) {
            throw new RepositoryException("Entity already exists!\n");
        }
    }

    @Override
    public Event remove(Integer integer) throws RepositoryException {
        String sql1 = "SELECT * FROM events WHERE ev_id=" + integer.toString();
        String sql2 = "DELETE FROM events WHERE ev_id=" + integer.toString();
        Event found = null;
        try {
            ResultSet resultSet = statement.executeQuery(sql1);
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                LocalDate date = resultSet.getDate("date").toLocalDate();
                found = new Event(name, description, date);
                found.setId(integer);
            }
            statement.executeUpdate(sql2);
        } catch (SQLException ignored) {
        }
        if (found == null) {
            throw new RepositoryException("Entity does not exist!\n");
        }
        return found;
    }

    @Override
    public ArrayList<Event> all() {
        ArrayList<Event> all = new ArrayList<>();
        String sql = "SELECT * FROM events";
        try {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("ev_id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                LocalDate date = resultSet.getDate("date").toLocalDate();
                Event event = new Event(name, description, date);
                event.setId(id);
                all.add(event);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return all;
    }

    @Override
    public Event find(Integer integer) throws RepositoryException {
        String sql = "SELECT * FROM events WHERE ev_id=" + integer.toString();
        Event found = null;
        try {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                LocalDate date = resultSet.getDate("date").toLocalDate();
                found = new Event(name, description, date);
                found.setId(integer);
            }
            if (found == null) {
                throw new RepositoryException("");
            }
        } catch (Exception e) {
            throw new RepositoryException("Entity does not exist!\n");
        }
        return found;
    }

    @Override
    public void update(Integer integer, Event event) throws RepositoryException {
        String sql = "UPDATE events SET name='" + event.getName() + "', description='" + event.getDescription() + "', date='" +
                event.getDate().format(Build.formatter) + "' WHERE ev_id=" + integer.toString();
        try {
            find(integer);
            statement.executeUpdate(sql);
        } catch (SQLException throwables) {
            throw new RepositoryException("Entity does not exist!\n");
        }
    }

    @Override
    public HashMap<Integer, Event> getElements() {
        HashMap<Integer, Event> map = new HashMap<>();
        String sql = "SELECT * FROM events";
        try {
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("ev_id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                LocalDate date = rs.getDate("date").toLocalDate();
                Event event = new Event(name, description, date);
                event.setId(id);
                map.put(id, event);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public List<Event> getPage() {
        ArrayList<Event> pageContent = new ArrayList<>();
        this.page++;
        String sql = "SELECT * FROM events LIMIT " + this.size + " OFFSET " + this.offset + ";";
        this.offset = (size * page + size);
        try {
            ResultSet rs = statement.executeQuery(sql);
            int id = rs.getInt("ev_id");
            String name = rs.getString("name");
            String description = rs.getString("description");
            LocalDate date = rs.getDate("date").toLocalDate();
            Event event = new Event(name, description, date);
            event.setId(id);
            pageContent.add(event);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return pageContent;
    }
}
