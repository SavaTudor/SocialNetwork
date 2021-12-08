package com.example.repository.database;

import com.example.domain.Message;
import com.example.domain.User;
import com.example.repository.Repository;
import com.example.exception.RepositoryException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBaseMessageRepository implements Repository<Integer, Message> {
    private final Connection connection;
    private final Statement statement;

    /**
     * The constructor
     * @param url String representing the URL of database
     * @param user String representing the user of the database
     * @param pass String representing the password of the user's account
     * @throws SQLException if the arguments are not valid
     */
    public DataBaseMessageRepository(String url, String user, String pass) throws SQLException {
        connection = DriverManager.getConnection(url, user, pass);
        statement = connection.createStatement();
    }

    /**
     * Return the number of messages in the database
     * @return Integer representing the number of messages in the database
     */
    @Override
    public int size() {
        return all().size();
    }

    /**
     * Check if there are no messages in the database
     * @return Boolean representing if there are no messages in the database
     */
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Add a message in database
     * @param integer Integer representing the id of the message
     * @param message Message representing the message which we want to add in database
     * @throws RepositoryException if there is another message with the same id in the database
     */
    @Override
    public void add(Integer integer, Message message) throws RepositoryException {
        String sql = "INSERT INTO messages(ms_id,\"mess\",\"data\") VALUES (" + integer.toString() +
                ",'" + message.getMessage() + "','" + message.getData() + "');";
        try {
            statement.executeUpdate(sql);
        } catch (Exception e) {
            throw new RepositoryException("Entity already exists!\n");
        }
        for (User user : message.getTo()) {
            if(message.getReply() == null)
            try {
                String sql1 = "INSERT INTO users_messages(from_user, to_user, mess_id)" +
                        " VALUES (" + message.getFrom().getId().toString() +
                        ", " + user.getId().toString() + ", " + message.getId().toString() + ")";

                statement.executeUpdate(sql1);
            } catch (SQLException e) {
                throw new RepositoryException("Invalid data!\n");
            }

            if(message.getReply() != null)
                try {
                    String sql1 = "INSERT INTO users_messages(from_user, to_user, mess_id, reply_to)" +
                            " VALUES (" + message.getFrom().getId().toString() +
                            ", " + user.getId().toString() + ", " + message.getId().toString() + ", " + message.getReply().getId().toString() + " )";

                    statement.executeUpdate(sql1);
                } catch (SQLException e) {
                    throw new RepositoryException("Invalid data!\n");
                }
        }

    }

    /**
     * Remove a message from database
     * @param integer Integer representing the id of message which we want to remove
     * @return Message representing the removed message
     * @throws RepositoryException if there is no message with the id given in the database
     */
    @Override
    public Message remove(Integer integer) throws RepositoryException {
        Message found = find(integer);
        String sql1 = "DELETE FROM messages WHERE ms_id=" + integer;
        String sql2 = "DELETE FROM users_messages WHERE mess_id=" + integer;
        try {
            statement.executeUpdate(sql2);
            statement.executeUpdate(sql1);
        } catch (Exception e) {
            throw new RepositoryException("Entity does not exist!\n");        }

        return found;
    }

    /**
     * Return all message from database
     * @return ArrayList of Message representing all message from database
     */
    @Override
    public ArrayList<Message> all() {
        ArrayList<Message> list = new ArrayList<>();
        String sql = "SELECT * FROM messages";
        try {
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("ms_id");
                Message message = find(id);
                if(message != null)
                    list.add(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Find a message in database
     * @param integer Integer representing the id of the message which we are looking for
     * @return Message representing the message which we are looking for
     * @throws RepositoryException if there is no message with the id given in the database
     */
    @Override
    public Message find(Integer integer) throws RepositoryException {
        String sql = "SELECT * FROM messages WHERE ms_id=" + integer;
        String sql1 = "SELECT * FROM users_messages WHERE mess_id=" + integer;
        Message found = null;
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int id = rs.getInt("ms_id");
            String mess = rs.getString("mess");
            LocalDateTime date = rs.getTimestamp("data").toLocalDateTime();
            List<User> userList = new ArrayList<>();
            User from;
            PreparedStatement ps1 = connection.prepareStatement(sql1);
            ResultSet rs1 = ps1.executeQuery();
            Integer reply = 0;
            while (rs1.next()) {
                Integer fr = rs1.getInt("from_user");
                String sql2 = "SELECT * FROM users WHERE id=" + fr;
                PreparedStatement ps2 = connection.prepareStatement(sql2);
                ResultSet rs2 = ps2.executeQuery();
                rs2.next();
                int idUser = rs2.getInt("id");
                String firstname = rs2.getString("firstname");
                String lastname = rs2.getString("lastname");
                from = new User(firstname, lastname);
                from.setId(idUser);

                Integer to = rs1.getInt("to_user");
                String sql3 = "SELECT * FROM users WHERE id = " + to;
                PreparedStatement ps3 = connection.prepareStatement(sql3);
                ResultSet rs3 = ps3.executeQuery();
                rs3.next();
                int idUser1 = rs3.getInt("id");
                String firstname1 = rs3.getString("firstname");
                String lastname1 = rs3.getString("lastname");
                User user = new User(firstname1, lastname1);
                user.setId(idUser1);
                userList.add(user);
                found = new Message(from, userList, mess);
                found.setData(date);
                found.setId(id);
                String replyString = rs1.getString("reply_to");
                if(replyString == null) {
                    reply = 0;
                }
                else{
                    reply = Integer.parseInt(replyString);}
            }
            if(reply != 0)
                found.setReply(find(reply));
            return found;
        } catch (Exception e) {
            throw new RepositoryException("Entity does not exist!\n");
        }
    }

    /**
     * Update a message
     * @param integer Integer representing the id of message which we want to update
     * @param message Message representing the new message
     * @throws RepositoryException if there is no message with the id given in the database
     */
    @Override
    public void update(Integer integer, Message message) throws RepositoryException {
        remove(integer);
        add(integer, message);
    }

    /**
     * Return all message from database
     * @return HashMap of Integer and Message representing all message from database
     */
    @Override
    public HashMap<Integer, Message> getElements() {
        HashMap<Integer, Message> map = new HashMap<>();
        String sql = "SELECT * FROM messages";
        try {
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("ms_id");
                Message message = find(id);
                if(message != null)
                    map.put(id, message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
