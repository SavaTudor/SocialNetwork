package repository.database;

import com.domain.Friendship;
import com.domain.User;
import exception.RepositoryException;
import repository.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import static build.Build.*;

public class DataBaseFriendshipRepository implements Repository<Integer, Friendship> {
    private Connection connection;
    private Statement statement;

    public DataBaseFriendshipRepository() throws SQLException {
        connection = DriverManager.getConnection(database_url, database_user, database_password);
        statement = connection.createStatement();

    }

    /**
     * @return
     */
    @Override
    public int size() {
        ResultSet rs;
        int size = 0;
        try {
            rs = statement.executeQuery("SELECT * FROM friendships;");
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
    public void add(Integer integer, Friendship friendship) throws RepositoryException {
        String sql = "INSERT INTO friendships(fr_id,\"usera\",\"userb\") VALUES (" + integer.toString() +
                ",'" + friendship.getUserA() + "','" + friendship.getUserB() + "');";
        try {
            statement.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RepositoryException("Entity already exists!\n");
        }
    }

    @Override
    public Friendship remove(Integer integer) throws RepositoryException {
        String sql1 = "SELECT * FROM friendships WHERE fr_id=" + integer.toString();
        String sql2 = "DELETE FROM friendships WHERE fr_id=" + integer.toString();
        Friendship found = null;
        try {
            ResultSet rs = statement.executeQuery(sql1);
            while (rs.next()) {
                int userA = rs.getInt("usera");
                int userB = rs.getInt("userb");
                found = new Friendship(userA, userB);
                found.setId(integer);
            }
            statement.executeUpdate(sql2);
        } catch (Exception e) {
            throw new RepositoryException("Entity does not exist!\n");
        }
        return found;
    }

    @Override
    public ArrayList<Friendship> all() {
        ArrayList<Friendship> all = new ArrayList<>();
        String sql = "SELECT * FROM friendships;";
        try {
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("fr_id");
                int userA = rs.getInt("usera");
                int userB = rs.getInt("userb");
                Friendship fr = new Friendship(userA, userB);
                fr.setId(id);
                all.add(fr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return all;
    }

    @Override
    public Friendship find(Integer integer) throws RepositoryException {
        String sql = "SELECT * FROM friendships WHERE fr_id=" + integer.toString();
        Friendship found = null;
        try {
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("fr_id");
                int userA = rs.getInt("usera");
                int userB = rs.getInt("userb");
                found = new Friendship(userA, userB);
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
    public void update(Integer integer, Friendship friendship) throws RepositoryException {
        String sql = "UPDATE friendships SET \"usera\"=" + friendship.getUserA() + ", \"userb\"=" + friendship.getUserB() +
                " WHERE fr_id=" + integer.toString();
        try {
            statement.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RepositoryException("Entity does not exist!\n");
        }
    }

    @Override
    public HashMap<Integer, Friendship> getElements() {
        HashMap<Integer, Friendship> map = new HashMap<>();
        String sql = "SELECT * FROM friendships";
        try {
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("fr_id");
                int userA = rs.getInt("usera");
                int userB = rs.getInt("userb");
                Friendship fr = new Friendship(userA, userB);
                fr.setId(id);
                map.put(id, fr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
