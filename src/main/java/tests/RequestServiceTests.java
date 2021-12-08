package tests;

import com.example.business.Controller;
import com.example.business.RequestService;
import com.example.domain.FriendRequest;
import com.example.domain.Status;
import com.example.exception.RepositoryException;
import com.example.exception.ValidatorException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.example.repository.database.DataBaseMessageRepository;
import com.example.repository.database.DataBaseUserRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static com.example.build.Build.*;
import static com.example.build.Build.database_password;
import static com.example.domain.Status.PENDING;
import static org.junit.jupiter.api.Assertions.*;

public class RequestServiceTests {
    private static Connection connection;
    private static Statement statement;
    private String sql;
    private static RequestService service;

    @BeforeEach
    void setUp1() {
        try {
            statement.executeUpdate("DELETE FROM users_messages;");
            statement.executeUpdate("DELETE FROM messages;");
            statement.executeUpdate("DELETE FROM friendship_invites");
            statement.executeUpdate("DELETE FROM friendships;");
            statement.executeUpdate("DELETE FROM users;");
            statement.executeUpdate("INSERT INTO users(id, \"firstname\", \"lastname\") VALUES" +
                    "(1, 'SAVA', 'TUDOR'),(2,'SUTEU','SEBI'),(3,'HASIU','BOGDAN')");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BeforeAll
    static void setUpConnection() throws SQLException {
        service = new RequestService(test_database_url, database_user, database_password);
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

    @Test
    void testCreate(){
        assertEquals(service.size(),0);
        assertTrue(service.isEmpty());
    }


    @Test
    void testFind() throws ValidatorException, RepositoryException {
        service.add(1,2, PENDING);
        var found = service.find(0);
        assertEquals(1, found.getFrom());
        assertEquals(2, found.getTo());
        assertEquals(PENDING, found.getStatus());
        try{
            service.find(3);
            fail();
        }catch (Exception e){
            assertEquals(e.getMessage(), "Entity does not exist!\n");
        }
    }
}
