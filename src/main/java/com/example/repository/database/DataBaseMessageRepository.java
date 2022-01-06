package com.example.repository.database;

import com.example.domain.MessageDTO;
import com.example.repository.Repository;
import com.example.exception.RepositoryException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBaseMessageRepository implements Repository<Integer, MessageDTO> {
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
    public void add(Integer integer, MessageDTO message) throws RepositoryException {
        String sql = "INSERT INTO messages(ms_id,\"mess\",\"data\") VALUES (" + integer.toString() +
                ",'" + message.getMessage() + "','" + message.getData() + "');";
        try {
            statement.executeUpdate(sql);
        } catch (Exception e) {
            throw new RepositoryException("Entity already exists!\n");
        }
        for (Integer user : message.getTo()) {
            if(message.getReply() == 0)
                try {
                    String sql1 = "INSERT INTO users_messages(from_user, to_user, mess_id)" +
                            " VALUES (" + message.getFrom() +
                            ", " + user + ", " + message.getId().toString() + ")";

                    statement.executeUpdate(sql1);
                } catch (SQLException e) {
                    throw new RepositoryException("Invalid data!\n");
                }

            if(message.getReply() != 0)
                try {
                    String sql1 = "INSERT INTO users_messages(from_user, to_user, mess_id, reply_to)" +
                            " VALUES (" + message.getFrom() +
                            ", " + user+ ", " + message.getId().toString() + ", " + message.getReply() + " )";

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
    public MessageDTO remove(Integer integer) throws RepositoryException {
        String sql1 = "DELETE FROM messages WHERE ms_id=" + integer;
        String sql2 = "DELETE FROM users_messages WHERE mess_id=" + integer;
        try {
            statement.executeUpdate(sql2);
            statement.executeUpdate(sql1);
        } catch (Exception e) {
            throw new RepositoryException("Entity does not exist!\n");        }

        return null;
    }

    /**
     * Return all message from database
     * @return ArrayList of Message representing all message from database
     */
    @Override
    public ArrayList<MessageDTO> all() {
        ArrayList<MessageDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM messages";
        try {
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("ms_id");
                MessageDTO message = find(id);
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
    public MessageDTO find(Integer integer) throws RepositoryException {
        String sql = "SELECT * FROM messages WHERE ms_id=" + integer;
        String sql1 = "SELECT * FROM users_messages WHERE mess_id=" + integer;
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int id = rs.getInt("ms_id");
            String mess = rs.getString("mess");
            LocalDateTime date = rs.getTimestamp("data").toLocalDateTime();
            List<Integer> userList = new ArrayList<>();
            PreparedStatement ps1 = connection.prepareStatement(sql1);
            ResultSet rs1 = ps1.executeQuery();
            Integer reply = 0;
            int from = 0;
            while (rs1.next()) {
                from = rs1.getInt("from_user");
                Integer to = rs1.getInt("to_user");
                userList.add(to);
                Integer replyString = rs1.getInt("reply_to");
            }
            MessageDTO found = new MessageDTO(from, userList, mess);
            found.setId(id);
            found.setReply(reply);
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
    public void update(Integer integer, MessageDTO message) throws RepositoryException {
        remove(integer);
        add(integer, message);
    }

    /**
     * Return all message from database
     * @return HashMap of Integer and Message representing all message from database
     */
    @Override
    public HashMap<Integer, MessageDTO> getElements() {
        HashMap<Integer, MessageDTO> map = new HashMap<>();
        String sql = "SELECT * FROM messages";
        try {
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("ms_id");
                MessageDTO message = find(id);
                if(message != null)
                    map.put(id, message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
