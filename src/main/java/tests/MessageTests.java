package tests;

import com.example.domain.Message;
import com.example.domain.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MessageTests {

    @Test
    void testMessage(){
        User user1 = new User("Andrei", "Suciu");
        user1.setId(1);
        User user2 = new User("Tudor", "Sava");
        user2.setId(2);
        User user3 = new User("Florin", "Bucur");
        user3.setId(3);
        List<User> userList = Arrays.asList(user2, user3);
        Message message = new Message(user1, userList, "Salut");
        message.setId(1);

        assertEquals(message.getFrom(), user1);
        assertEquals(message.getTo(), userList);
        message.getData();

        message.setMessage("Buna ziua");
        assertEquals(message.getMessage(), "Buna ziua");

        List<User> userList1 = List.of(user2);
        message.setTo(userList1);
        assertEquals(message.getTo(), userList1);

        message.setFrom(user3);
        assertEquals(message.getFrom(), user3);
        message.toString();

        Message message1 = new Message(user2, userList1, "Ce faci");
        message1.setReply(message);
        message1.setId(2);
        message1.toString();

        assertEquals(message.hashCode(), 369933239);

        assertEquals(message, message);
        assertNotEquals(1, message);
        assertNotEquals(null, message);

        Message message2 = new Message(user1, userList1, "Ce faci?");
        message2.setId(1);
        assertEquals(message, message2);
        LocalDateTime date = LocalDateTime.of(2020,11,20,11,11);

        message.setData(date);
        assertEquals(message.getData(), date);

        assertNull(message2.getReply());
        Message message3 = new Message(user2,userList,"Bine, tu?");
        message2.setId(2);
        message3.setReply(message2);
        assertEquals(message3.getReply(), message2);
    }
}
