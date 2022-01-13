package com.example.repository.database;

import com.example.domain.User;
import com.example.exception.RepositoryException;
import com.example.repository.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DataBaseUserRepository implements Repository<Integer, User> {
    private Connection connection;
    private Statement statement;
    private int page = -1;
    private int size = 5;
    private int offset = 0;


    /**
     * Constructor
     * creates the connection to the database and creates the statement
     *
     * @throws SQLException if it failed to connect to the database
     */
    public DataBaseUserRepository(String url, String user, String pass) throws SQLException {
        connection = DriverManager.getConnection(url, user, pass);
        statement = connection.createStatement();
    }


    public DataBaseUserRepository(Connection connection, Statement statement) {
        this.connection = connection;
        this.statement = statement;
    }

    /**
     * Finds out the number of elements from the database
     *
     * @return an integer representing the number of records
     */
    @Override
    public int size() {
        ResultSet rs;
        int size = 0;
        try {
            rs = statement.executeQuery("SELECT * FROM users;");
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
    public void add(Integer integer, User user) throws RepositoryException {
        String sql = "INSERT INTO users(id,\"firstname\",\"lastname\",\"username\",\"password\") VALUES (" + integer.toString() +
                ",'" + user.getFirstName() + "','" + user.getLastName() + "','" + user.getUsername() + "','" + user.getPassword() + "');";
        try {
            statement.executeUpdate(sql);
        } catch (Exception e) {
            throw new RepositoryException("Username already exists!\n");
        }
    }

    @Override
    public User remove(Integer integer) throws RepositoryException {
        String sql1 = "SELECT * FROM USERS WHERE id=" + integer.toString();
        String sql2 = "DELETE FROM users WHERE id=" + integer.toString();
        User found = null;
        try {
            ResultSet rs = statement.executeQuery(sql1);
            while (rs.next()) {
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                String username = rs.getString("username");
                String password = rs.getString("password");
                found = new User(username, firstName, lastName, password);
                found.setId(integer);
            }
            statement.executeUpdate(sql2);
        } catch (Exception ignored) {
        }
        if (found == null) {
            throw new RepositoryException("Entity does not exist!\n");
        }
        return found;
    }

    public ArrayList<User> getPage() {
        ArrayList<User> pageContent = new ArrayList<>();
        this.page++;
        String sql = "SELECT * FROM users LIMIT " + this.size + " OFFSET " + this.offset + ";";
        this.offset = (size * page + size);
        try{
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                String username = rs.getString("username");
                String password = rs.getString("password");
                User user = new User(username, firstName, lastName, password);
                user.setId(id);
                pageContent.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pageContent;
    }


    @Override
    public ArrayList<User> all() {
        ArrayList<User> all = new ArrayList<>();
        String sql = "SELECT * FROM users;";
        try {
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                String username = rs.getString("username");
                String password = rs.getString("password");
                User user = new User(username, firstName, lastName, password);
                user.setId(id);
                all.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return all;
    }

    @Override
    public User find(Integer integer) throws RepositoryException {
        String sql = "SELECT * FROM users WHERE id=" + integer.toString();
        User found = null;
        try {
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                String username = rs.getString("username");
                String password = rs.getString("password");
                found = new User(username, firstName, lastName, password);
                found.setId(id);
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
    public void update(Integer integer, User user) throws RepositoryException {
        String sql = "UPDATE users SET \"firstname\"='" + user.getFirstName() + "', \"lastname\"='" + user.getLastName() + "', \"username\"='" + user.getUsername() + "', \"password\"='" + user.getPassword() +
                "' WHERE id=" + integer.toString();
        try {
            find(integer);
            statement.executeUpdate(sql);
        } catch (Exception e) {
            throw new RepositoryException("Entity does not exist!\n");
        }

    }

    @Override
    public HashMap<Integer, User> getElements() {
        HashMap<Integer, User> map = new HashMap<>();
        String sql = "SELECT * FROM users";
        try {
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                String firstName = rs.getString("firstname");
                String lastName = rs.getString("lastname");
                String username = rs.getString("username");
                String password = rs.getString("password");
                User user = new User(username, firstName, lastName, password);
                user.setId(id);
                map.put(id, user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
