package repository.database;

import com.domain.User;
import exception.RepositoryException;
import repository.Repository;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static build.Build.*;

public class DataBaseUserRepository implements Repository<Integer, User> {
    private Connection connection;
    private Statement statement;

    /**
     * Constructor
     * creates the connection to the database and creates the statement
     * @throws SQLException if it failed to connect to the database
     */
    public DataBaseUserRepository() throws SQLException {
        connection = DriverManager.getConnection(dataBase, user, password);
        statement = connection.createStatement();
    }

    /**
     * Finds out the number of elements from the database
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
        String sql = "INSERT INTO users(id,\"firstName\",\"lastName\") VALUES (" + integer.toString() +
                ",'" + user.getFirstName() + "','" + user.getLastName()+"');";
        try {
            statement.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RepositoryException("Entity already exists!\n");
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
                found = new User(firstName, lastName);
                found.setId(integer);
            }
            statement.executeUpdate(sql2);
        } catch (Exception e) {
            throw new RepositoryException("Entity does not exist!\n");
        }
        return found;
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
                User user = new User(firstName, lastName);
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
                found = new User(firstName, lastName);
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
        String sql = "UPDATE users SET \"firstName\"='" + user.getFirstName() + "', \"lastName\"='" + user.getLastName() +
                "' WHERE id=" + integer.toString();
        System.out.println(sql);
        try {
            statement.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
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
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                User user = new User(firstName, lastName);
                user.setId(id);
                map.put(id, user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
