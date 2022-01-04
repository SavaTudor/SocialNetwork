package com.example.repository.database;

import com.example.domain.Message;
import com.example.domain.MessageDto;
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
     * @param message Message representing the message which we want to add in database
     * @throws RepositoryException if there is another message with the same id in the database
     */
    @Override
    public void add(Integer id, Message message) throws RepositoryException {
        String sql = "INSERT INTO messages(mess,data) VALUES (" + "'" + message.getMessage() + "','" + message.getData() + "');";
        try {
            statement.executeUpdate(sql);
        } catch (Exception e) {
            //throw new RepositoryException("Entity already exists!\n");
            e.printStackTrace();
        }

        String sql1 = "SELECT * FROM messages order by ms_id desc limit 1";
        Integer id1 = null;
        try {
            PreparedStatement ps = connection.prepareStatement(sql1);
            ResultSet rs = ps.executeQuery();
            rs.next();
            id1 = rs.getInt("ms_id");

        } catch (Exception e) {
            //throw new RepositoryException("Entity already exists!\n");
            e.printStackTrace();
        }

        for (User user : message.getTo()) {
            if(message.getReply() == null)
            try {
                String sql2 = "INSERT INTO users_messages(from_user, to_user, mess_id)" +
                        " VALUES (" + message.getFrom().getId().toString() +
                        ", " + user.getId().toString() + ", " + id1 + ")";

                statement.executeUpdate(sql2);
            } catch (SQLException e) {
                throw new RepositoryException("Invalid data!\n");
            }

            if(id != 0)
                try {
                    String sql3 = "INSERT INTO users_messages(from_user, to_user, mess_id, reply_to)" +
                            " VALUES (" + message.getFrom().getId().toString() +
                            ", " + user.getId().toString() + ", " + id1 + ", " + id + " )";

                    statement.executeUpdate(sql3);
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
        Message found;
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            rs.next();

            String mess = rs.getString("mess");
            LocalDateTime date = rs.getTimestamp("data").toLocalDateTime();

            List<User> userList = new ArrayList<>();

            PreparedStatement ps1 = connection.prepareStatement(sql1);
            ResultSet rs1 = ps1.executeQuery();

            User from;
            Integer reply = null;
            Integer fr = null;
            while (rs1.next()) {
                fr = rs1.getInt("from_user");
                Integer to = rs1.getInt("to_user");
                String sql3 = "SELECT * FROM users WHERE id = " + to;
                PreparedStatement ps3 = connection.prepareStatement(sql3);
                ResultSet rs3 = ps3.executeQuery();
                rs3.next();
                int idUser1 = rs3.getInt("id");
                String firstName1 = rs3.getString("firstname");
                String lastName1 = rs3.getString("lastname");
                String username1 = rs3.getString("username");
                String password1 = rs3.getString("password");
                User user = new User(username1, firstName1, lastName1, password1);
                user.setId(idUser1);
                userList.add(user);
                reply = rs1.getInt("reply_to");
            }
            if(fr == null)
                return null;
            String sql2 = "SELECT * FROM users WHERE id=" + fr.toString();
            PreparedStatement ps2 = connection.prepareStatement(sql2);
            ResultSet rs2 = ps2.executeQuery();
            rs2.next();
            int idUser = rs2.getInt("id");
            String firstName = rs2.getString("firstname");
            String lastName = rs2.getString("lastname");
            String username = rs2.getString("username");
            String password = rs2.getString("password");
            from = new User(username,firstName, lastName,password);
            from.setId(idUser);
            found = new Message(from, userList, mess);
            found.setData(date);
            found.setId(integer);
            if(reply != 0) {
                found.setReply(find(reply));
            }
            return found;
        } catch (Exception e) {
            //throw new RepositoryException("Entity does not exist!\n");
            e.printStackTrace();
            return null;
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
