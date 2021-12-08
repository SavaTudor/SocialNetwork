package tests;

import com.example.domain.Friendship;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.example.repository.database.DataBaseFriendshipRepository;
import com.example.repository.database.DataBaseUserRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

import static com.example.build.Build.*;
import static com.example.build.Build.database_password;
import static org.junit.jupiter.api.Assertions.*;

public class DataBaseFriendshipRepoTests {
    private static DataBaseFriendshipRepository repo;
    private static Connection connection;
    private static Statement statement;
    private String sql;

    @BeforeAll
    static void setUpConnection() throws SQLException {
        repo = new DataBaseFriendshipRepository(test_database_url, database_user, database_password);
        connection = DriverManager.getConnection(test_database_url, database_user, database_password);
        statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM users");
        statement.executeUpdate("INSERT INTO users(id, \"firstname\", \"lastname\") VALUES" +
                "(1, 'SAVA', 'TUDOR'),(2,'SUTEU','SEBI'),(3,'HASIU','BOGDAN')");
    }

    @AfterAll
    static void closeConnection() throws SQLException {
        statement.executeUpdate("DELETE FROM friendships;");
        statement.executeUpdate("DELETE FROM users");
        statement.close();
        connection.close();
    }

    @BeforeEach
    void setUp() {
        sql = "DELETE FROM friendships;";
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
        Friendship f1 = new Friendship(1, 2);
        f1.setId(1);
        Friendship f2 = new Friendship(2, 3);
        f1.setId(2);
        try {
            repo.add(1, f1);
            assertEquals(repo.size(), 1);
            repo.add(2, f2);
            assertEquals(repo.size(), 2);
        } catch (Exception e) {
            fail();
        }
        try {
            repo.add(1, f2);
            fail();
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Entity already exists!\n");
        }
        var all = repo.all();
        assertEquals(all.size(), 2);
    }

    @Test
    void testRemove_Find(){
        Friendship f1 = new Friendship(1, 2, LocalDateTime.of(2021, 11,19,12,11));
        f1.setId(1);
        Friendship f2 = new Friendship(2, 3);
        f1.setId(2);
        try {
            repo.add(1, f1);
            assertEquals(repo.size(), 1);
            repo.add(2, f2);
            assertEquals(repo.size(), 2);
        } catch (Exception e) {
            fail();
        }
        try{
            Friendship found = repo.find(1);
            assertEquals(found.getUserA(), 1);
            assertEquals(found.getUserB(), 2);
            assertEquals(found.getDate().format(formatter), "2021-11-19 12:11:00");
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
        Friendship f1 = new Friendship(1, 2);
        f1.setId(1);
        Friendship f2 = new Friendship(2, 3);
        f2.setId(2);
        try {
            repo.add(1, f1);
            assertEquals(repo.size(), 1);
            repo.add(2, f2);
            assertEquals(repo.size(), 2);
        } catch (Exception e) {
            fail();
        }
        try{
            repo.update(1, new Friendship(1,3));
        }catch (Exception e){
            fail();
        }try{
            repo.update(3, new Friendship(2,3));
        }catch (Exception e){
            assertEquals(e.getMessage(), "Entity does not exist!\n");
        }
        var elems = repo.getElements();
        assertEquals(elems.size(), 2);
    }
}
