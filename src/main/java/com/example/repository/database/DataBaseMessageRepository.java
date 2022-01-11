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
    private int page = -1;
    private int size = 5;
    private int offset = 0;

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

    public DataBaseMessageRepository(Connection connection, Statement statement) {
        this.connection = connection;
        this.statement = statement;
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
        String sql2 = "DELETE FROM users_messages WHERE mess_id=" + integer + " or reply_to=" + integer;
        try {
            statement.executeUpdate(sql2);
            statement.executeUpdate(sql1);
        } catch (Exception e) {
            throw new RepositoryException("Entity does not exist!\n");        }

        return null;
    }

    @Override
    public List<MessageDTO> getPage() {
        return new ArrayList<>();
    }

    /**
     * Return all message from database
     * @return ArrayList of Message representing all message from database
     */
    @Override
    public ArrayList<MessageDTO> all() {
        ArrayList<MessageDTO> list = new ArrayList<>();
        List<Integer> toList = new ArrayList<>();
        String sql = "select M.ms_id, M.mess, M.data, U.from_user, U.to_user, U.reply_to FROM messages M INNER JOIN users_messages U ON M.ms_id = U.mess_id";
        int toPrec = 0;
        try {
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()) {
                int id = rs.getInt("ms_id");
                String mess = rs.getString("mess");
                LocalDateTime date = rs.getTimestamp("data").toLocalDateTime();
                int from = rs.getInt("from_user");
                int reply = rs.getInt("reply_to");
                int to_user = rs.getInt("to_user");
                toList.add(to_user);
                if (toPrec != id) {
                    MessageDTO messageDTO = new MessageDTO(from, toList, mess);
                    messageDTO.setId(id);
                    messageDTO.setData(date);
                    messageDTO.setReply(reply);
                    list.add(messageDTO);
                    toPrec = id;
                    toList = new ArrayList<>();
                }
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
        String sql = "select M.ms_id, M.mess, M.data, U.from_user, U.to_user, U.reply_to FROM messages M INNER JOIN users_messages U ON M.ms_id = U.mess_id AND M.ms_id=" + integer;
        MessageDTO messageDTO = null;
        List<Integer> toList = new ArrayList<>();
        try {
            ResultSet rs = statement.executeQuery(sql);
            rs.next();
            int id = rs.getInt("ms_id");
            String mess = rs.getString("mess");
            LocalDateTime date = rs.getTimestamp("data").toLocalDateTime();
            int from = rs.getInt("from_user");
            int reply = rs.getInt("reply_to");
            int to_user = rs.getInt("to_user");
            toList.add(to_user);
            while(rs.next()) {
                to_user = rs.getInt("to_user");
                toList.add(to_user);
            }
            messageDTO = new MessageDTO(from, toList, mess);
            messageDTO.setId(id);
            messageDTO.setData(date);
            messageDTO.setReply(reply);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messageDTO;
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