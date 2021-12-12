package tests;

import com.example.business.Controller;
import com.example.domain.Message;
import com.example.domain.User;
import com.example.domain.UsersFriendsDTO;
import com.example.business.Controller;
import com.example.repository.database.DataBaseMessageRepository;
import com.example.repository.database.DataBaseUserRepository;
import com.example.exception.EntityException;
import com.example.exception.RepositoryException;
import com.example.exception.ValidatorException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.example.repository.database.DataBaseMessageRepository;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import static com.example.build.Build.*;
import static org.junit.jupiter.api.Assertions.*;

public class ControllerTests {
    private static DataBaseMessageRepository repo;
    private static DataBaseUserRepository repoUser;
    private static Connection connection;
    private static Statement statement;
    private String sql;
    private static Controller service;

    @BeforeEach
    void setUp1() {
        try {
            statement.executeUpdate("DELETE FROM users_messages;");
            statement.executeUpdate("DELETE FROM messages;");
            statement.executeUpdate("DELETE FROM friendship_invites");
            statement.executeUpdate("DELETE FROM friendships;");
            statement.executeUpdate("DELETE FROM users;");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @BeforeAll
    static void setUpConnection() throws SQLException {
        repo = new DataBaseMessageRepository(test_database_url, database_user, database_password);
        repoUser = new DataBaseUserRepository(test_database_url, database_user, database_password);
        service = new Controller(test_database_url, database_user, database_password);
        connection = DriverManager.getConnection(test_database_url, database_user, database_password);
        statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM friendship_invites");
        statement.executeUpdate("DELETE FROM friendships;");
        statement.executeUpdate("DELETE FROM users");
        statement.executeUpdate("INSERT INTO users(id, \"firstname\", \"lastname\") VALUES" +
                "(1, 'SAVA', 'TUDOR'),(2,'SUTEU','SEBI'),(3,'HASIU','BOGDAN')");
    }

    @AfterAll
    static void closeConnection() throws SQLException {
        statement.executeUpdate("DELETE FROM users_messages;");
        statement.executeUpdate("DELETE FROM messages;");
        statement.executeUpdate("DELETE FROM friendship_invites");
        statement.executeUpdate("DELETE FROM friendships;");
        statement.executeUpdate("DELETE FROM users");
        statement.close();
        connection.close();
    }

    @BeforeEach
    void setUp() {
        try {
            statement.executeUpdate("DELETE FROM friendship_invites;");
            statement.executeUpdate("DELETE FROM friendships;");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testCreate() throws ValidatorException, RepositoryException, EntityException, SQLException {
        service.refreshNetwork();
        statement.executeUpdate("DELETE FROM users;");
        assertEquals(service.allUsers().size(), 0);
        User user1 = new User("Sava", "Tudor");
        user1.setId(0);
        User user2 = new User("Suteu", "Sebi");
        user2.setId(2);
        service.add("Sava", "Tudor");
        assertEquals("Sava", service.allUsers().get(0).getFirstName());
        assertEquals("Tudor", service.allUsers().get(0).getLastName());
        assertEquals(user1, service.findUser(0));
        service.add("Suteu", "Sebi");
        assertEquals(service.allUsers().size(), 2);
        service.removeUser(1);
        assertEquals(service.allUsers().size(), 1);
        service.add("Hasiu", "Bogdan");
        service.add("Suteu", "Sebi");
        service.addFriend(service.allUsers().get(0).getId(), service.allUsers().get(1).getId());
        assertEquals(service.largestCommunity().size(), 2);
        assertEquals(service.communitiesNumber(), 2);
        service.removeFriends(service.allUsers().get(0).getId(), service.allUsers().get(1).getId());
        service.addFriend(service.allUsers().get(0).getId(), service.allUsers().get(2).getId());
        service.removeUser(service.allUsers().get(2).getId());
        assertEquals(service.largestCommunity().size(), 1);
        assertEquals(service.communitiesNumber(), 2);
        service.updateUser(1, "Sava", "Vlad");
        service.getFriends(1);
        service.add("name", "surname");
        service.addFriend(0,1);
        service.updateFriend(0,2,1);
        assertEquals(1, service.getFriends(2).size());
        assertEquals(1,service.allFriendships().size());
    }

    @Test
    void testfindFriendshipByUserAndMonth() {
        service.refreshNetwork();
        List<UsersFriendsDTO> usersFriendsDTOList = null;

        try {
            service.add("Suciu", "Andrei");
        } catch (RepositoryException | ValidatorException e) {
            assertTrue(fail());
        }

        try {
            service.add("Sava", "Tudor");
        } catch (RepositoryException | ValidatorException e) {
            assertTrue(fail());
        }

        try {
            service.add("Stahie", "Dan");
        } catch (RepositoryException | ValidatorException e) {
            assertTrue(fail());
        }

        try {
            service.addFriend(0, 1);
        } catch (RepositoryException | EntityException | ValidatorException e) {
            assertTrue(fail());
        }

        try {
            service.addFriend(0, 2);
        } catch (RepositoryException | EntityException | ValidatorException e) {
            assertTrue(fail());
        }

        try {
            usersFriendsDTOList = service.findFriendshipByUserAndMonth(2, 11);
        } catch (RepositoryException | ValidatorException e) {
            assertTrue(fail());
        }
        assertEquals(usersFriendsDTOList.size(), 1);
        try {
            usersFriendsDTOList = service.findFriendshipByUserAndMonth(3, 11);
        } catch (RepositoryException | ValidatorException e) {
            assertSame("Entity does not exist!\n", e.getMessage());
        }

        try {
            usersFriendsDTOList = service.findFriendshipByUserAndMonth(2, 13);
        } catch (RepositoryException e) {
            assertTrue(fail());
        } catch (ValidatorException e) {
            assertSame("invalid month!\n", e.getMessage());
        }

        try {
            usersFriendsDTOList = service.findFriendshipByUserAndMonth(0, 11);
        } catch (RepositoryException | ValidatorException e) {
            assertTrue(fail());
        }
        assertEquals(usersFriendsDTOList.size(), 2);

        try {
            usersFriendsDTOList = service.getFriends(0);
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
        assertEquals(usersFriendsDTOList.size(), 2);

        try {
            service.removeFriends(0, 1);
        } catch (RepositoryException | EntityException e) {
            assertTrue(fail());
        }

        try {
            service.removeFriends(0, 2);
        } catch (RepositoryException | EntityException e) {
            assertTrue(fail());
        }

        try {
            service.removeUser(0);
        } catch (RepositoryException e) {
            assertTrue(fail());
        }

        try {
            service.removeUser(1);
        } catch (RepositoryException e) {
            assertTrue(fail());
        }

        try {
            service.removeUser(2);
        } catch (RepositoryException e) {
            assertTrue(fail());
        }
    }




    @Test
    void testFriendRequest() {
        try {
            statement.executeUpdate("INSERT INTO users(id, \"firstname\", \"lastname\") VALUES" +
                    "(1, 'SAVA', 'TUDOR'),(2,'SUTEU','SEBI'),(3,'HASIU','BOGDAN')");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        service.refreshNetwork();
        int id = 0;
        try {
            id = service.add("User3", "New3").getId();
            service.addFriendRequest(1, 3);
            service.addFriendRequest(1, id);
            service.addFriendRequest(2, 3);
            service.addFriendRequest(id, 3);
        } catch (ValidatorException | RepositoryException e) {
            e.printStackTrace();
        }
        try {
            service.respondFriendRequest(1, id, "REJECT");
        } catch (ValidatorException | EntityException | RepositoryException e) {
            e.printStackTrace();
        }
        assertEquals(service.getFriendRequests(3).size(), 3);
        service.respondToAllRequests(3, "APPROVE");


    }

    @Test
    void addMessageTests() {
        Controller service = new Controller(test_database_url, database_user, database_password);
        try {
            service.add("Suciu", "Andrei");
        } catch (RepositoryException | ValidatorException e) {
            assertTrue(fail());
        }

        try {
            service.add("Sava", "Tudor");
        } catch (RepositoryException | ValidatorException e) {
            assertTrue(fail());
        }

        try {
            service.add("Stahie", "Dan");
        } catch (RepositoryException | ValidatorException e) {
            assertTrue(fail());
        }

        List<Integer> to = Arrays.asList(0, 2);
        try {
            service.addNewMessage(1, to, "Salut");
        } catch (RepositoryException | ValidatorException e) {
            assertTrue(fail());
        }

        to = Arrays.asList(0, 1);
        try {
            service.addNewMessage(1,to,"Noroc");
        } catch (RepositoryException e) {
            assertEquals(e.getMessage(), "you can not send message to yourself");
        } catch (ValidatorException e) {
            assertTrue(fail());
        }

        List<Message> list = service.allMessage();
        assertEquals(list.size(), 1);
    }

    @Test
    void removeMessageTests() {
        Controller service = new Controller(test_database_url, database_user, database_password);
        try {
            service.add("Suciu", "Andrei");
        } catch (RepositoryException | ValidatorException e) {
            assertTrue(fail());
        }

        try {
            service.add("Sava", "Tudor");
        } catch (RepositoryException | ValidatorException e) {
            assertTrue(fail());
        }

        try {
            service.add("Stahie", "Dan");
        } catch (RepositoryException | ValidatorException e) {
            assertTrue(fail());
        }

        List<Integer> to = Arrays.asList(0, 2);
        try {
            service.addNewMessage(1, to, "Salut");
        } catch (RepositoryException e) {
            System.out.println(e.getMessage());
        } catch (ValidatorException e) {
            assertTrue(fail());
        }

        List<Message> list = service.allMessage();
        assertEquals(list.size(), 1);

        to = List.of(1);
        try {
            service.replyMessage(2, to, "Ce faci?", 1);
        } catch (RepositoryException | ValidatorException e) {
            assertTrue(fail());
        }

        try {
            service.removeMessage(1);
        } catch (RepositoryException e) {
            assertTrue(fail());
        }
        List<Message> list1 = service.allMessage();
        assertEquals(list1.size(), 0);
    }


    @Test
    void findMessageTest() {
        Controller service = new Controller(test_database_url, database_user, database_password);
        try {
            service.add("Suciu", "Andrei");
        } catch (RepositoryException | ValidatorException e) {
            assertTrue(fail());
        }

        try {
            service.add("Sava", "Tudor");
        } catch (RepositoryException | ValidatorException e) {
            assertTrue(fail());
        }

        try {
            service.add("Stahie", "Dan");
        } catch (RepositoryException | ValidatorException e) {
            assertTrue(fail());
        }

        List<Integer> to = Arrays.asList(0, 2);
        try {
            try {
                service.addNewMessage(1, to, "Salut");
            } catch (ValidatorException e) {
                assertTrue(fail());
            }
        } catch (RepositoryException e) {
            assertTrue(fail());
        }

        List<Message> list = service.allMessage();
        assertEquals(list.size(), 1);

        try {
            Message message = service.findMessage(1);
            assertEquals(message.getId(), 1);
            assertEquals(message.getMessage(), "Salut");
        } catch (RepositoryException e) {
            assertTrue(fail());
        }
        List<Message> messages = service.allMessageByUser(1);
        assertEquals(messages.size(), 1);

    }

    @Test
    void findMessagesTest() {
        Controller service = new Controller(test_database_url, database_user, database_password);
        try {
            service.add("Suciu", "Andrei");
        } catch (RepositoryException | ValidatorException e) {
            assertTrue(fail());
        }

        try {
            service.add("Sava", "Tudor");
        } catch (RepositoryException | ValidatorException e) {
            assertTrue(fail());
        }

        try {
            service.add("Stahie", "Dan");
        } catch (RepositoryException | ValidatorException e) {
            assertTrue(fail());
        }

        List<Integer> to = Arrays.asList(0, 2);
        try {
            service.addNewMessage(1, to, "Salut");
        } catch (RepositoryException | ValidatorException e) {
            assertTrue(fail());
        }

        List<Message> list = service.allMessage();
        assertEquals(list.size(), 1);

        try {
            List<Message> messages = service.findMessages(2);
            assertEquals(messages.size(), 1);
        } catch (RepositoryException e) {
            assertTrue(fail());
        }

        try {
            service.findMessages(1);
        } catch (RepositoryException e) {
            assertEquals(e.getMessage(), "no messages for this user");
        }

        List<Message> messages = service.allMessageByUser(1);
        assertEquals(messages.size(), 1);
    }

    @Test
    void updateMessageTest() {
        Controller service = new Controller(test_database_url, database_user, database_password);
        try {
            service.add("Suciu", "Andrei");
        } catch (RepositoryException | ValidatorException e) {
            assertTrue(fail());
        }

        try {
            service.add("Sava", "Tudor");
        } catch (RepositoryException | ValidatorException e) {
            assertTrue(fail());
        }

        try {
            service.add("Stahie", "Dan");
        } catch (RepositoryException | ValidatorException e) {
            assertTrue(fail());
        }

        List<Integer> to = Arrays.asList(0, 2);
        try {
            service.addNewMessage(1, to, "Salut");
        } catch (RepositoryException e) {
            System.out.println(e.getMessage());
        } catch (ValidatorException e) {
            assertTrue(fail());
        }

        List<Message> list = service.allMessage();
        assertEquals(list.size(), 1);

        to = List.of(0);

        try {
            Integer reply = null;
            service.updateMessage(1, 1, to, "Buna ziua", reply);
            Message message = service.findMessage(1);
            assertEquals(message.getMessage(), "Buna ziua");
        } catch (RepositoryException | ValidatorException e) {
            assertTrue(fail());
        }

        to = List.of(1);
        try {
            service.replyMessage(0, to, "Ce faci?", 1);
        } catch (RepositoryException | ValidatorException e) {
            assertTrue(fail());
        }

        to = List.of(1);
        try {
            Integer reply = 2;
            service.updateMessage(1, 1, to, "Buna ziua", reply);
        } catch (RepositoryException e) {
            assertEquals(e.getMessage(), "you can not send message to yourself");
        } catch (ValidatorException e) {
            assertTrue(fail());
        }

        to = List.of(1);
        try {
            Integer reply = 1;
            service.updateMessage(2, 0, to, "Buna ziua", reply);
        } catch (RepositoryException | ValidatorException e) {
            assertTrue(fail());
        }

    }

    @Test
    void replyMessageTest(){
        Controller service = new Controller(test_database_url, database_user, database_password);
        try {
            service.add("Suciu", "Andrei");
        } catch (RepositoryException | ValidatorException e) {
            assertTrue(fail());
        }

        try {
            service.add("Sava","Tudor");
        } catch (RepositoryException | ValidatorException e) {
            assertTrue(fail());
        }

        try {
            service.add("Stahie","Dan");
        } catch (RepositoryException | ValidatorException e) {
            assertTrue(fail());
        }

        try {
            service.add("Spiridon","Dragos");
        } catch (RepositoryException | ValidatorException e) {
            assertTrue(fail());
        }

        List<Integer> to = Arrays.asList(0,2);
        try {
            service.addNewMessage(1, to, "Salut");
        } catch (RepositoryException | ValidatorException e) {
            assertTrue(fail());
        }

        List<Message> list = service.allMessage();
        assertEquals(list.size(), 1);

        to = List.of(1);
        try {
            service.replyMessage(2,to,"Ce faci?", 1);
        } catch (RepositoryException | ValidatorException e) {
            assertTrue(fail());
        }

        list = service.allMessage();
        assertEquals(list.size(), 2);

        List<Message> list1 = service.allMessageByUser(2);
        assertEquals(list1.size(), 1);

        to = List.of(2);
        try {
            service.replyMessage(2,to,"Ce faci?", 1);
        } catch (RepositoryException e) {
            assertEquals(e.getMessage(), "you can not send message to yourself!");
        } catch (ValidatorException e) {
            assertTrue(fail());
        }

        to = List.of(2);
        try {
            service.replyMessage(3,to,"Ce faci?", 1);
        } catch (RepositoryException e) {
            assertEquals(e.getMessage(), "sender invalid!");
        } catch (ValidatorException e) {
            assertTrue(fail());
        }
    }


    @Test
    void getConversationTest(){
        Controller service = new Controller(test_database_url, database_user, database_password);
        try {
            service.add("Suciu", "Andrei");
        } catch (RepositoryException | ValidatorException e) {
            assertTrue(fail());
        }

        try {
            service.add("Sava","Tudor");
        } catch (RepositoryException | ValidatorException e) {
            assertTrue(fail());
        }

        try {
            service.add("Stahie","Dan");
        } catch (RepositoryException | ValidatorException e) {
            assertTrue(fail());
        }

        List<Integer> to = Arrays.asList(0,2);
        try {
            service.addNewMessage(1, to, "Salut");
        } catch (RepositoryException | ValidatorException e) {
            assertTrue(fail());
        }

        List<Message> list = service.allMessage();
        assertEquals(list.size(), 1);

        to = List.of(1);
        try {
            service.replyMessage(2,to,"Ce faci?", 1);
        } catch (RepositoryException | ValidatorException e) {
            assertTrue(fail());
        }

        list = service.allMessage();
        assertEquals(list.size(), 2);

        List<Message> conversation = null;
        try {
            conversation = service.getConversation(2, 1);
        } catch (RepositoryException e) {
            assertTrue(fail());
        }
        assertEquals(conversation.size(), 2);
    }


}