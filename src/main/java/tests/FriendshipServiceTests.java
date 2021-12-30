package tests;

import com.example.domain.Friendship;
import com.example.domain.User;
import com.example.business.FriendshipService;
import com.example.exception.RepositoryException;
import com.example.exception.ValidatorException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

import static com.example.build.Build.*;
import static org.junit.jupiter.api.Assertions.*;

public class FriendshipServiceTests {
    private static Connection connection;
    private static Statement statement;
    private String sql;
    private static FriendshipService servicedb;

    @BeforeAll
    static void setCon() throws SQLException {
        connection = DriverManager.getConnection(test_database_url, database_user, database_password);
        statement = connection.createStatement();
        servicedb = new FriendshipService(test_database_url, database_user, database_password);
    }

    @BeforeEach
    void setUp() throws IOException, SQLException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("tests/testData/usersTest.csv"));
        bufferedWriter.write("");
        BufferedWriter bufferedWriter1 = new BufferedWriter(new FileWriter("tests/testData/friendshipTest.csv"));
        bufferedWriter1.write("");
        statement.executeUpdate("DELETE FROM friendships;");
        statement.executeUpdate("DELETE FROM users");
        statement.executeUpdate("INSERT INTO users(id, \"firstname\", \"lastname\") VALUES" +
                "(1, 'SAVA', 'TUDOR'),(2,'SUTEU','SEBI'),(3,'HASIU','BOGDAN')");
    }

    @AfterAll
    static void cleanUp() throws SQLException {
        statement.executeUpdate("DELETE FROM friendships;");
        statement.executeUpdate("DELETE FROM users");
        statement.close();
        connection.close();
    }

    @Test
    void testCreate() {
        FriendshipService service = new FriendshipService("tests/testData/friendshipTest.csv");
        assertTrue(service.isEmpty());
    }

    @Test
    void tests() {
        User user1 = new User("gfds","Sava", "Tudor","juhygtf");
        user1.setId(1);
        User user2 = new User("fd","Suteu", "Sebi","Gv");
        user2.setId(2);
        User user3 = new User("Fd","aaa", "bbb","Ws");
        user3.setId(3);
        FriendshipService service = new FriendshipService("tests/testData/friendshipTest.csv");
        try {
            service.add(user1.getId(), user3.getId(), LocalDateTime.now());
        } catch (Exception e) {
            fail();
        }
        try {
            service.update(0, 1, 2);
        } catch (Exception e) {
            fail();
        }
        Friendship fr = new Friendship(user1.getId(), user2.getId());
        try {
            assertEquals(service.find(0), fr);
        } catch (Exception e) {
            fail();
        }
        try {
            service.add(user1.getId(), user2.getId(), LocalDateTime.now());
            fail();
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Entity already exists!\n");
        }
        assertEquals(service.all().size(), 1);
        try {
            service.findByUsers(1, 2);
        } catch (Exception e) {
            fail();
        }
        try {
            service.remove(0);
        } catch (Exception e) {
            fail();
        }
        try {
            service.remove(0);
            fail();
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Entity does not exist!\n");
        }
        try {
            service.findByUsers(2, 3);
            fail();
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Entity does not exist!\n");
        }

    }

    @Test
    void testCreateDb(){
        assertEquals(servicedb.size(),0);
        assertTrue(servicedb.isEmpty());
    }

    @Test
    void testAdd() throws ValidatorException, RepositoryException {
        assertEquals(servicedb.size(),0);
        servicedb.add(1,2, LocalDateTime.now());
        assertEquals(servicedb.size(),1);
        servicedb.add(2,3, LocalDateTime.now());
        assertEquals(servicedb.size(),2);
        try{
            servicedb.add(1,2,LocalDateTime.now());
            fail();
        }catch (Exception e){
            assertEquals(e.getMessage(), "Entity already exists!\n");
        }
        try{
            servicedb.add(1,1,LocalDateTime.now());
            fail();
        }catch (Exception e){
            assertEquals(e.getMessage(), "Users must be different!\n");
        }
    }
}
