package tests;

import com.example.domain.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTests {
    @Test
    void test() {
        String name = "Sava";
        String surname = "Tudor";
        User user = new User(name, surname);
        user.setId(1);
        assertEquals(user.getId(), 1);
        assertEquals(user.getFirstName(), name);
        assertEquals(user.getLastName(), surname);
        String newName = "Pop";
        String newSurname = "Valentin";
        user.setFirstName(newName);
        user.setLastName(newSurname);

        String anotherName = "David";
        String anotherSurname = "Istrate";
        User otherUser = new User(newName, newSurname);
        otherUser.setId(1);
        User anotherUser = new User(anotherName, anotherSurname);
        anotherUser.setId(2);
        assertEquals(user, otherUser);
        assertEquals(user.toString(), "1 | Pop Valentin");
    }

}
