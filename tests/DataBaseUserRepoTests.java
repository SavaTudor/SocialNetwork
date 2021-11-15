import com.domain.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.database.DataBaseUserRepository;

import java.sql.*;

import static build.Build.*;
import static org.junit.jupiter.api.Assertions.*;

public class DataBaseUserRepoTests {
    private static DataBaseUserRepository repo;
    private static Connection connection;
    private static Statement statement;
    private String sql;

    @BeforeAll
    static void setUpConnection() throws SQLException {
        repo = new DataBaseUserRepository(test_database_url, database_user, database_password);
        connection = DriverManager.getConnection(test_database_url, database_user, database_password);
        statement = connection.createStatement();
    }

    @AfterAll
    static void closeConnection() throws SQLException{
        statement.executeUpdate("DELETE FROM users;");
        statement.close();
        connection.close();
    }

    @BeforeEach
    void setUp() {
        sql = "DELETE FROM users;";
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
        User user1 = new User("Sava", "Tudor");
        user1.setId(1);
        User user2 = new User("Suteu", "Sebi");
        user2.setId(2);
        assertTrue(repo.isEmpty());
        try {
            repo.add(1, user1);
        } catch (Exception e) {
            fail();
        }
        assertEquals(repo.size(), 1);
        try{
            repo.add(2, user2);
        }catch (Exception e){
            fail();
        }
        assertEquals(repo.size(), 2);
        try {
            repo.add(1, user1);
            fail();
        }catch (Exception e){
            assertEquals(e.getMessage(), "Entity already exists!\n");
        }
        var all = repo.all();
        assertEquals(all.size(), 2);
        assertEquals(all.get(0).getFirstName(), "Sava");
    }

    @Test
    void testRemove_Find(){
        User user1 = new User("Sava", "Tudor");
        user1.setId(1);
        User user2 = new User("Suteu", "Sebi");
        user2.setId(2);
        assertTrue(repo.isEmpty());
        try {
            repo.add(1, user1);
        } catch (Exception e) {
            fail();
        }
        assertEquals(repo.size(), 1);
        try{
            repo.add(2, user2);
        }catch (Exception e){
            fail();
        }
        assertEquals(repo.size(), 2);
        try{
            User found = repo.find(1);
            assertEquals(found.getFirstName(), "Sava");
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
            User removed = repo.remove(2);
            assertEquals(removed.getFirstName(), "Suteu");
        }catch (Exception e){
            fail();
        }
        try{
            repo.remove(2);
            fail();
        }catch (Exception e){
            assertEquals(e.getMessage(), "Entity does not exist!\n");
        }
    }

    @Test
    void testUpdate_get(){
        User user1 = new User("Sava", "Tudor");
        user1.setId(1);
        User user2 = new User("Suteu", "Sebi");
        user2.setId(2);
        assertTrue(repo.isEmpty());
        try {
            repo.add(1, user1);
        } catch (Exception e) {
            fail();
        }
        assertEquals(repo.size(), 1);
        try{
            repo.add(2, user2);
        }catch (Exception e){
            fail();
        }
        assertEquals(repo.size(), 2);
        try{
            repo.update(2, new User("Hasiu", "Bogdan"));
            User found = repo.find(2);
            assertEquals(found.getFirstName(), "Hasiu");
            assertEquals(found.getLastName(), "Bogdan");
        }catch (Exception e){
            fail();
        }
        try{
            repo.update(3, new User("asda", "dsad"));
            fail();
        }catch (Exception e){
            assertEquals(e.getMessage(), "Entity does not exist!\n");
        }
        var elems = repo.getElements();
        assertEquals(elems.size(), 2);
        assertTrue(elems.containsKey(1));
        assertTrue(elems.containsKey(2));
    }
}
