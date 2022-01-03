package tests;

import com.example.domain.*;
import com.example.exception.ValidatorException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ValidatorTests {

    @Test
    void testsUser() {
        User userGood = new User("a","Sava", "Tudor","ijhgv");
        userGood.setId(1);
        User userBad = new User("b","", "","oijhg");
        ValidatorUser validatorUser = new ValidatorUser();
        try {
            validatorUser.valideaza(userGood);
        } catch (Exception e) {
            fail();
        }
        try {
            validatorUser.valideaza(userBad);
            fail();
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Invalid first name!\nInvalid last name!\n");
        }
    }

    @Test
    void testFriendship() {
        Friendship frGood = new Friendship(1, 2, LocalDateTime.now());
        frGood.setId(1);
        Friendship frBad = new Friendship(3, 3, LocalDateTime.now());
        frBad.setId(3);
        ValidatorFriendship validatorFriendship = new ValidatorFriendship();
        try {
            validatorFriendship.valideaza(frGood);
        } catch (Exception e) {
            fail();
        }
        try {
            validatorFriendship.valideaza(frBad);
            fail();
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Users must be different!\n");
        }
    }

    @Test
    void testRequest() {
        FriendRequest frGood = new FriendRequest(1, 2, Status.PENDING);
        FriendRequest frBad = new FriendRequest(3, 3, Status.APPROVED);
        ValidatorRequest validatorRequest = new ValidatorRequest();
        try {
            validatorRequest.valideaza(frGood);
        } catch (Exception e) {
            fail();
        }
        try {
            validatorRequest.valideaza(frBad);
            fail();
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Users must be different!\n");
        }
    }

    @Test
    void validatorMessageTest(){
        User user1 = new User("a","Andrei", "Suciu","ijh");
        user1.setId(1);
        User user2 = new User("b","Tudor", "Sava","ujhgv");
        user2.setId(2);
        User user3 = new User("c","Florin", "Bucur","oij");
        user3.setId(3);
        List<User> userList = Arrays.asList(user2, user3);
        Message message = new Message(user1, userList, "Salut");
        message.setId(1);

        List<User> userList1 = List.of(user1);
        Message message1 = new Message(user2, userList1, "Hi noroc!");
        message1.setId(1);
        message1.setReply(message);
        ValidatorMessage validatorMessage = new ValidatorMessage();
        try {
            validatorMessage.valideaza(message);
        } catch (ValidatorException e) {
            assertTrue(fail());
        }
        message1.setMessage("");
        message1.setTo(null);
        try {
            validatorMessage.valideaza(message1);
        } catch (ValidatorException e) {
            assertEquals(e.getMessage(), "recipient must be not empty!\nmessage must be not empty!\n");
        }
    }
}

