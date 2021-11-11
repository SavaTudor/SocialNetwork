import com.domain.User;
import business.Controller;
import exception.EntityException;
import exception.RepositoryException;
import exception.ValidatorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class ControllerTests {
    @BeforeEach
    void setUp() throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("tests/testData/usersTest.csv"));
        bufferedWriter.write("");
        BufferedWriter bufferedWriter1 = new BufferedWriter(new FileWriter("tests/testData/friendshipTest.csv"));
        bufferedWriter1.write("");

    }
    @Test
    void testCreate() throws ValidatorException, RepositoryException, EntityException {
        Controller service = new Controller("tests/testData/usersTest.csv", "tests/testData/friendshipTest.csv");
        assertEquals(service.allUsers().size(), 0);
        User user1 = new User("Sava", "Tudor");
        user1.setId(0);
        User user2 = new User("Suteu", "Sebi");
        user2.setId(2);
        service.add("Sava", "Tudor");
        assertSame("Sava", service.allUsers().get(0).getFirstName());
        assertSame("Tudor", service.allUsers().get(0).getLastName());
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
    }
}
