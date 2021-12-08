package com.example.repository.database;

import com.example.domain.FriendRequest;
import com.example.domain.Status;
import com.example.exception.RepositoryException;
import com.example.repository.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DataBaseRequestsRepository implements Repository<Integer, FriendRequest> {
    private Connection connection;
    private Statement statement;


    public DataBaseRequestsRepository(String url, String user, String pass) throws SQLException {
        connection = DriverManager.getConnection(url, user, pass);
        statement = connection.createStatement();
    }

    @Override
    public int size() {
        ResultSet rs;
        int size = 0;
        try {
            rs = statement.executeQuery("SELECT * FROM friendship_invites");
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
    public void add(Integer integer, FriendRequest friendRequest) throws RepositoryException {
        String sql = "INSERT INTO friendship_invites(id,usera,userb,status) VALUES (" +
                integer.toString() + ",'" + friendRequest.getFrom() + "','" + friendRequest.getTo() +
                "','" + friendRequest.getStatus().toString() + "');";
        try {
            statement.executeUpdate(sql);
        } catch (Exception e) {
//            e.printStackTrace();
            throw new RepositoryException("Entity already exists!\n");
        }
    }

    @Override
    public FriendRequest remove(Integer integer) throws RepositoryException {
        String sql1 = "SELECT * FROM friendship_invites WHERE id=" + integer.toString();
        String sql2 = "DELETE FROM friendship_invites WHERE id=" + integer.toString();
        FriendRequest found = null;
        try {
            ResultSet rs = statement.executeQuery(sql1);
            while (rs.next()) {
                int userA = rs.getInt("usera");
                int userB = rs.getInt("userb");
                Status status = rs.getString("status").equals("PENDING") ? Status.PENDING :
                        rs.getString("status").equals("APPROVED") ? Status.APPROVED :
                                Status.REJECTED;
                found = new FriendRequest(userA, userB, status);
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

    @Override
    public ArrayList<FriendRequest> all() {
        ArrayList<FriendRequest> all = new ArrayList<>();
        String sql = "SELECT * FROM friendship_invites;";
        try {
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                int userA = rs.getInt("usera");
                int userB = rs.getInt("userb");
                Status status = rs.getString("status").equals("PENDING") ? Status.PENDING :
                        rs.getString("status").equals("APPROVED") ? Status.APPROVED :
                                Status.REJECTED;
                FriendRequest fr = new FriendRequest(userA, userB, status);
                fr.setId(id);
                all.add(fr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return all;
    }

    @Override
    public FriendRequest find(Integer integer) throws RepositoryException {
        String sql = "SELECT * FROM friendship_invites WHERE id=" + integer.toString();
        FriendRequest found = null;
        try {
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                int userA = rs.getInt("usera");
                int userB = rs.getInt("userb");
                Status status = rs.getString("status").equals("PENDING") ? Status.PENDING :
                        rs.getString("status").equals("APPROVED") ? Status.APPROVED :
                                Status.REJECTED;
                found = new FriendRequest(userA, userB, status);
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
    public void update(Integer integer, FriendRequest friendRequest) throws RepositoryException {
        String sql = "UPDATE friendship_invites SET \"usera\"=" + friendRequest.getFrom() + ", \"userb\"=" + friendRequest.getTo() +
               ",status='"+ friendRequest.getStatus().toString()+ "' WHERE id=" + integer.toString();
        try {
            find(integer);
            statement.executeUpdate(sql);
        } catch (Exception e) {
            throw new RepositoryException("Entity does not exist!\n");
        }
    }

    @Override
    public HashMap<Integer, FriendRequest> getElements() {
        HashMap<Integer, FriendRequest> map = new HashMap<>();
        String sql = "SELECT * FROM friendship_invites";
        try {
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                int userA = rs.getInt("usera");
                int userB = rs.getInt("userb");
                Status status = rs.getString("status").equals("PENDING") ? Status.PENDING :
                        rs.getString("status").equals("APPROVED") ? Status.APPROVED :
                                Status.REJECTED;
                FriendRequest fr = new FriendRequest(userA, userB, status);
                fr.setId(id);
                map.put(id, fr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
