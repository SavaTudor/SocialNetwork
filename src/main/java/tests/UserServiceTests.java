package tests;

import com.example.domain.User;
import com.example.business.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTests {
    @BeforeEach
    void setUp() throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("tests/testData/usersTest.csv"));
        bufferedWriter.write("");
        BufferedWriter bufferedWriter1 = new BufferedWriter(new FileWriter("tests/testData/friendshipTest.csv"));
        bufferedWriter1.write("");

    }

    @Test
    void testCreate() {
        UserService service = new UserService("tests/testData/usersTest.csv");
        assertEquals(service.size(), 0);
        assertTrue(service.isEmpty());
    }

    @Test
    void testAdd() {
        UserService service = new UserService("tests/testData/usersTest.csv");
        String name1 = "Sava", name2 = "Bogdi";
        String surname1 = "Tudor", surname2 = "Bogdi";
        assertEquals(service.size(), 0);
        try {
            service.add("a",name1, surname1,"dfgh");
            assertEquals(service.size(), 1);
            service.add("b",name2, surname2,"sdfg");
            assertEquals(service.size(), 2);
        } catch (Exception e) {
            fail();
        }
        try {
            service.add("c","", "","zsdf");
            fail();
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Invalid first name!\nInvalid last name!\n");
        }
        ArrayList<User> users = service.all();
        assertEquals(users.size(), 2);
        User user1 = users.get(0);
        User user2 = users.get(1);
        assertEquals(user1.toString(), "0 | Sava Tudor");
        assertEquals(user2.toString(), "1 | Bogdi Bogdi");
    }

    @Test
    void testFind() {
        UserService service = new UserService("tests/testData/usersTest.csv");
        String username1 = "savatudor31", username2 = "bogdanH";
        String name1 = "Sava", name2 = "Bogdi";
        String surname1 = "Tudor", surname2 = "Bogdi";
        try {
            service.add("d",name1, surname1,"cdf");
            service.add("e",name2, surname2,"zaed");
        } catch (Exception e) {
            fail();
        }
        try {
            User user = service.find(1);
            //assertEquals(user.toString(), "1 | Bogdi Bogdi");
        } catch (Exception e) {
            fail();
        }
        try {
            User user1 = service.find(3);
            fail();
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Entity does not exist!\n");
        }
    }

    @Test
    void testRemove() {
        UserService service = new UserService("tests/testData/usersTest.csv");
        String name1 = "Sava", name2 = "Bogdi";
        String surname1 = "Tudor", surname2 = "Bogdi";
        try {
            service.add("q",name1, surname1,"fg");
            service.add("w",name2, surname2,"xdfg");
        } catch (Exception e) {
            fail();
        }
        assertEquals(service.size(), 2);
        try {
            service.remove(0);
        } catch (Exception e) {
            fail();
        }
        assertEquals(service.size(), 1);
        try {
            service.remove(3);
            fail();
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Entity does not exist!\n");
        }
        try {
            service.add("f","ceva", "nou","cdfg");
        } catch (Exception e) {
            fail();
        }
    }

}
