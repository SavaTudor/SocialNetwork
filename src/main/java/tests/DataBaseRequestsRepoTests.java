package tests;

import com.example.domain.FriendRequest;
import com.example.domain.Friendship;
import com.example.domain.Status;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.example.repository.database.DataBaseFriendshipRepository;
import com.example.repository.database.DataBaseRequestsRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static com.example.build.Build.*;
import static com.example.build.Build.database_password;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

public class DataBaseRequestsRepoTests {
    private static DataBaseRequestsRepository repo;
    private static Connection connection;
    private static Statement statement;
    private String sql;

    @BeforeAll
    static void setUpConnection() throws SQLException {
        repo = new DataBaseRequestsRepository(test_database_url, database_user, database_password);
        connection = DriverManager.getConnection(test_database_url, database_user, database_password);
        statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM friendship_invites;");
        statement.executeUpdate("DELETE FROM users");
        statement.executeUpdate("INSERT INTO users(id, \"firstname\", \"lastname\") VALUES" +
                "(1, 'SAVA', 'TUDOR'),(2,'SUTEU','SEBI'),(3,'HASIU','BOGDAN')");
    }

    @AfterAll
    static void closeConnection() throws SQLException {
        statement.executeUpdate("DELETE FROM friendship_invites;");
        statement.executeUpdate("DELETE FROM users");
        statement.close();
        connection.close();
    }


    @BeforeEach
    void setUp() {
        sql = "DELETE FROM friendship_invites;";
        try {
            statement.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testOpen() {
        assertEquals(repo.size(), 0);
        assertTrue(repo.isEmpty());
    }

    @Test
    void testAdd_All() {
        FriendRequest fr1 = new FriendRequest(1,2, Status.PENDING);
        fr1.setId(1);
        FriendRequest fr2 = new FriendRequest(1,3, Status.APPROVED);
        fr2.setId(2);
        try {
            repo.add(1, fr1);
            assertEquals(repo.size(), 1);
            repo.add(2, fr2);
            assertEquals(repo.size(), 2);
        } catch (Exception e) {
            fail();
        }
        try {
            repo.add(1, fr2);
            fail();
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Entity already exists!\n");
        }
        var all = repo.all();
        assertEquals(all.size(), 2);
    }

    @Test
    void testRemove_Find() {
        FriendRequest fr1 = new FriendRequest(1,2, Status.PENDING);
        fr1.setId(1);
        FriendRequest fr2 = new FriendRequest(1,3, Status.APPROVED);
        fr2.setId(2);
        try {
            repo.add(1, fr1);
            assertEquals(repo.size(), 1);
            repo.add(2, fr2);
            assertEquals(repo.size(), 2);
        } catch (Exception e) {
            fail();
        }
        try{
            FriendRequest found = repo.find(1);
            assertEquals(found.getFrom(), 1);
            assertEquals(found.getTo(), 2);
            assertEquals(found.getStatus(), Status.PENDING);
        }catch (Exception e){
            fail();
        }
        try{
            repo.find(3);
            fail();
        }catch (Exception e){
            assertEquals(e.getMessage(), "Entity does not exist!\n");
        }
        try{
            repo.remove(2);
            assertEquals(repo.size(),1);
        }catch (Exception e){
            fail();
        }
        try{
            repo.remove(3);
            fail();
        }catch (Exception e){
            assertEquals(e.getMessage(), "Entity does not exist!\n");
        }
    }

    @Test
    void testUpdate_Get(){
        FriendRequest fr1 = new FriendRequest(1, 2,Status.PENDING);
        fr1.setId(1);
        FriendRequest fr2 = new FriendRequest(1, 3, Status.APPROVED);
        fr2.setId(2);
        try {
            repo.add(1, fr1);
            assertEquals(repo.size(), 1);
            repo.add(2, fr2);
            assertEquals(repo.size(), 2);
        } catch (Exception e) {
            fail();
        }
        try{
            repo.update(1, new FriendRequest(1,2, Status.APPROVED));
        }catch (Exception e){
            fail();
        }try{
            repo.update(3, new FriendRequest(2,3, Status.REJECTED));
        }catch (Exception e){
            assertEquals(e.getMessage(), "Entity does not exist!\n");
        }
        var elems = repo.getElements();
        assertEquals(elems.size(), 2);
    }
}