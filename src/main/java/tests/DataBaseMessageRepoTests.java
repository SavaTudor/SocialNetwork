package tests;

import com.example.domain.Message;
import com.example.domain.User;
import com.example.exception.RepositoryException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.example.repository.database.DataBaseMessageRepository;
import com.example.repository.database.DataBaseUserRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


import static com.example.build.Build.*;
import static org.junit.jupiter.api.Assertions.*;
//package tests;
//
//import com.example.domain.Message;
//import com.example.domain.User;
//import com.example.exception.RepositoryException;
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import com.example.repository.database.DataBaseMessageRepository;
//import com.example.repository.database.DataBaseUserRepository;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//
//
//import static com.example.build.Build.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//
//public class DataBaseMessageRepoTests {
//    private static DataBaseMessageRepository repo;
//    private static DataBaseUserRepository repoUser;
//    private static Connection connection;
//    private static Statement statement;
//
//    @BeforeAll
//    static void setUpConnection() throws SQLException {
//        repo = new DataBaseMessageRepository(test_database_url, database_user, database_password);
//        repoUser = new DataBaseUserRepository(test_database_url, database_user, database_password);
//        connection = DriverManager.getConnection(test_database_url, database_user, database_password);
//        statement = connection.createStatement();
//    }
//
//    @AfterAll
//    static void closeConnection() throws SQLException{
//        statement.executeUpdate("DELETE FROM users_messages;");
//        statement.executeUpdate("DELETE FROM messages;");
//        statement.executeUpdate("DELETE FROM users;");
//        statement.close();
//        connection.close();
//    }
//    @BeforeEach
//    void setUp() {
//        try {
//            statement.executeUpdate("DELETE FROM users_messages;");
//            statement.executeUpdate("DELETE FROM messages;");
//            statement.executeUpdate("DELETE FROM users;");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    void  addMessageTest(){
//        assertEquals(repo.size(), 0);
//        assertTrue(repo.isEmpty());
//        User user1 = new User("AS","Andrei", "Suciu","Dsa");
//        user1.setId(1);
//        try {
//            repoUser.add(1, user1);
//        } catch (RepositoryException e) {
//            assertTrue(Assertions.fail());
//        }
//        User user2 = new User("Dsa","Tudor", "Sava","FDs");
//        user2.setId(2);
//        try {
//            repoUser.add(2,user2);
//        } catch (RepositoryException e) {
//            assertTrue(Assertions.fail());
//        }
//        User user3 = new User("Ds","Florin", "Bucur","Sdc");
//        user3.setId(3);
//        try {
//            repoUser.add(3,user3);
//        } catch (RepositoryException e) {
//            assertTrue(Assertions.fail());
//        }
//        List<User> userList = Arrays.asList(user2, user3);
//        Message message = new Message(user1, userList, "Salut");
//        message.setId(1);
//        try {
//            repo.add(1, message);
//        } catch (RepositoryException e) {
//            assertTrue(Assertions.fail());
//        }
//
//        try {
//            repo.add(1, message);
//        } catch (RepositoryException e) {
//            assertEquals(e.getMessage(), "Entity already exists!\n");
//        }
//
//        try {
//            repo.find(1);
//        } catch (RepositoryException e) {
//            e.printStackTrace();
//        }
//        assertEquals(repo.size(), 1);
//        assertFalse(repo.isEmpty());
//        ArrayList<Message> messageList = repo.all();
//        assertEquals(messageList.get(0).getMessage(), "Salut");
//
//        List<User> userList1 = Arrays.asList(user1);
//        Message message1 = new Message(user2, userList1, "Ce faci");
//        message1.setReply(message);
//        message1.setId(2);
//        try {
//            repo.add(2, message1);
//        } catch (RepositoryException e) {
//            assertTrue(fail());
//        }
//        assertEquals(repo.size(), 2);
//
//        try {
//            Message message2 = repo.find(2);
//            assertEquals(message2.getReply(), message);
//        } catch (RepositoryException e) {
//            assertTrue(fail());
//        }
//    }
//
//    @Test
//    void removeMessageTests() {
//        assertEquals(repo.size(), 0);
//        assertTrue(repo.isEmpty());
//        User user1 = new User("fd","Andrei", "Suciu","Ws");
//        user1.setId(1);
//        try {
//            repoUser.add(1, user1);
//        } catch (RepositoryException e) {
//            assertTrue(Assertions.fail());
//        }
//        User user2 = new User("Fds","Tudor", "Sava","Fds");
//        user2.setId(2);
//        try {
//            repoUser.add(2, user2);
//        } catch (RepositoryException e) {
//            assertTrue(Assertions.fail());
//        }
//        User user3 = new User("TRFDe","Florin", "Bucur","fdew");
//        user3.setId(3);
//        try {
//            repoUser.add(3, user3);
//        } catch (RepositoryException e) {
//            assertTrue(Assertions.fail());
//        }
//        List<User> userList = Arrays.asList(user2, user3);
//        Message message = new Message(user1, userList, "Salut");
//        message.setId(1);
//        try {
//            repo.add(1, message);
//        } catch (RepositoryException e) {
//            assertTrue(Assertions.fail());
//        }
//
//        try {
//            Message message1 = repo.remove(1);
//            assertEquals(message1, message);
//        } catch (RepositoryException e) {
//            assertTrue(fail());
//        }
//
//        try {
//            repo.add(1, message);
//        } catch (RepositoryException e) {
//            assertTrue(Assertions.fail());
//        }
//
//        List<User> userList1 = Arrays.asList(user1);
//        Message message1 = new Message(user2, userList1, "Ce faci");
//        message1.setReply(message);
//        message1.setId(2);
//        try {
//            repo.add(2, message1);
//        } catch (RepositoryException e) {
//            assertTrue(fail());
//        }
//
//        try {
//            Message message2 = repo.remove(1);
//            assertEquals(message2, message);
//        } catch (RepositoryException e) {
//            assertTrue(fail());
//        }
//
//        assertTrue(repo.isEmpty());
//        assertEquals(repo.size(), 0);
//    }
//
//    @Test
//    void testUpdateMessage(){
//        assertEquals(repo.size(), 0);
//        assertTrue(repo.isEmpty());

//        User user1 = new User("ij","Andrei", "Suciu","ws");
//        user1.setId(1);
//        try {
//            repoUser.add(1, user1);
//        } catch (RepositoryException e) {
//            assertTrue(Assertions.fail());
//        }
//        User user2 = new User("wsd","Tudor", "Sava","Ws");
//        user2.setId(2);
//        try {
//            repoUser.add(2, user2);
//        } catch (RepositoryException e) {
//            assertTrue(Assertions.fail());
//        }
//        User user3 = new User("df","Florin", "Bucur","gfd");
//        user3.setId(3);
//        try {
//            repoUser.add(3, user3);
//        } catch (RepositoryException e) {
//            assertTrue(Assertions.fail());
//        }
//        List<User> userList = Arrays.asList(user2, user3);
//        Message message = new Message(user1, userList, "Salut");
//        message.setId(1);
//        try {
//            repo.add(1, message);
//        } catch (RepositoryException e) {
//            assertTrue(Assertions.fail());
//        }
//
//        message.setMessage("Buna ziua");
//
//        try {
//            repo.update(1, message);
//            assertEquals(repo.size(), 1);
//            assertFalse(repo.isEmpty());
//        } catch (RepositoryException e) {
//            assertTrue(fail());
//        }
//
//        try {
//            repo.find(1);
//        } catch (RepositoryException e) {
//            assertTrue(fail());
//        }
//
//        Map<Integer, Message> map = repo.getElements();
//        assertEquals(map.get(1), message);
//
//        try {
//            repo.update(2, message);
//        } catch (RepositoryException e) {
//            assertEquals(e.getMessage(), "Entity does not exist!\n");
//        }
//
//        try {
//            repo.find(5);
//        } catch (RepositoryException e) {
//            assertEquals(e.getMessage(), "Entity does not exist!\n");
//        }
//
//        ArrayList<Message> messageList = repo.all();
//        assertEquals(messageList.get(0).getMessage(),"Buna ziua");
//
//        List<User> userList1 = Arrays.asList(user1);
//        Message message1 = new Message(user2, userList1, "Ce faci");
//        message1.setReply(message);
//        message1.setId(2);
//        try {
//            repo.add(2, message1);
//        } catch (RepositoryException e) {
//            assertTrue(fail());
//        }
//
//        Message message2 = new Message(user3, userList1, "Salutare");
//        message2.setId(2);
//
//        try {
//            repo.update(2, message2);
//        } catch (RepositoryException e) {
//            assertTrue(fail());
//        }
//
//        messageList = repo.all();
//        assertEquals(messageList.get(1).getMessage(),"Salutare");
//
//    }
//}
