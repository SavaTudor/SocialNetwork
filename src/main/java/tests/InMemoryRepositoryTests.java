package tests;

import com.example.domain.User;
import com.example.exception.RepositoryException;
import org.junit.jupiter.api.Test;
import com.example.repository.memory.InMemoryRepository;
import com.example.repository.Repository;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;


public class InMemoryRepositoryTests {

    @Test
    void testCreate() {
        Repository<String, User> repository = new InMemoryRepository<>();
        assertEquals(repository.size(), 0);
        assertTrue(repository.isEmpty());
    }

    @Test
    void testAdd() {
        Repository<Integer, User> repository = new InMemoryRepository<>();
        assertEquals(repository.size(), 0);
        User user1 = new User("QAsd","Sava", "Tudor","qws");
        user1.setId(1);
        User user2 = new User("ws","david", "David","1qa");
        user2.setId(2);
        try {
            repository.add(1, user1);
        } catch (Exception e) {
            fail();
        }
        assertEquals(repository.size(), 1);
        try {
            repository.add(2, user2);
        } catch (Exception e) {
            fail();
        }
        assertEquals(repository.size(), 2);
        try {
            repository.add(1, user1);
            fail();
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Entity already exists!\n");
        }
        ArrayList<User> all = repository.all();
        User first = all.get(0);
        assertEquals(first.getFirstName(), "Sava");
    }

    @Test
    void testRemove() throws RepositoryException {
        Repository<Integer, User> repository = new InMemoryRepository<>();
        User user1 = new User("qas","Sava", "Tudor","eds");
        user1.setId(1);
        User user2 = new User("fdsqwd","david", "David","hygtfcdx");
        user2.setId(2);
        repository.add(1, user1);
        repository.add(2, user2);
        assertEquals(repository.size(), 2);
        User deletedUser = repository.remove(2);
        assertEquals(repository.size(), 1);
        assertEquals(deletedUser.getFirstName(), "david");
        try {
            repository.remove(2);
            fail();
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Entity does not exist!\n");
        }
    }

    @Test
    void testFind() throws RepositoryException {
        Repository<Integer, User> repository = new InMemoryRepository<>();
        User user1 = new User( "Red","Sava", "Tudor","frdes");
        user1.setId(1);
        User user2 = new User("qas", "david", "David","fd");
        user2.setId(2);
        repository.add(1, user1);
        repository.add(2, user2);
        assertEquals(repository.size(), 2);
        User foundUser = repository.find(1);
        assertEquals(foundUser.toString(), "1 | Sava Tudor");
        try {
            repository.find(3);
            fail();
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Entity does not exist!\n");
        }
    }

    @Test
    void testRand() throws RepositoryException {
        Repository<Integer, User> repository = new InMemoryRepository<>();
        User user1 = new User("qasd", "Sava", "Tudor","ws");
        user1.setId(1);
        User user2 = new User("ds", "david", "David","ws");
        user2.setId(2);
        repository.add(1, user1);
        repository.add(2, user2);
        assertEquals(repository.size(), 2);
        HashMap<Integer, User> map = repository.getElements();
        assertEquals(map.size(), 2);
        User user3 = new User("fde","Suteu", "Sebi","ytrfd");
        user3.setId(1);
        repository.update(1, user3);
        User found = repository.find(1);
        assertEquals(found.getFirstName(), "Suteu");
        assertEquals(found.getLastName(), "Sebi");
    }
}
