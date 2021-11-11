import com.domain.User;
import business.UserService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTests {

    @Test
    void testCreate() {
        UserService service = new UserService();
        assertEquals(service.size(), 0);
        assertTrue(service.isEmpty());
    }

    @Test
    void testAdd() {
        UserService service = new UserService();
        String name1 = "Sava", name2 = "Bogdi";
        String surname1 = "Tudor", surname2 = "Bogdi";
        assertEquals(service.size(), 0);
        try {
            service.add(name1, surname1);
            assertEquals(service.size(), 1);
            service.add(name2, surname2);
            assertEquals(service.size(), 2);
        } catch (Exception e) {
            fail();
        }
        try {
            service.add("", "");
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
        UserService service = new UserService();
        String username1 = "savatudor31", username2 = "bogdanH";
        String name1 = "Sava", name2 = "Bogdi";
        String surname1 = "Tudor", surname2 = "Bogdi";
        try {
            service.add(name1, surname1);
            service.add(name2, surname2);
        } catch (Exception e) {
            fail();
        }
        try {
            User user = service.find(1);
            assertEquals(user.toString(), "1 | Bogdi Bogdi");
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
        UserService service = new UserService();
        String name1 = "Sava", name2 = "Bogdi";
        String surname1 = "Tudor", surname2 = "Bogdi";
        try {
            service.add(name1, surname1);
            service.add(name2, surname2);
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
        try{
            service.add("ceva", "nou");
        }catch (Exception e){
            fail();
        }
    }


    /*
    @Test
    void testAddFriend() {
        UserService service = new UserService();
        String username1 = "savatudor31", username2 = "bogdanH";
        String name1 = "Sava", name2 = "Bogdi";
        String surname1 = "Tudor", surname2 = "Bogdi";
        try {
            service.add(username1, name1, surname1);
            service.add(username2, name2, surname2);
            assertEquals(service.getFriends("savatudor31").size(), 0);
            assertEquals(service.getFriends("bogdanH").size(), 0);
        } catch (Exception e) {
            fail();
        }
        try {
            service.addFriend("savatudor31", "bogdanH");
            assertEquals(service.getFriends("savatudor31").size(), 1);
            assertEquals(service.getFriends("savatudor31").get(0).hashCode(), "bogdanH".hashCode());
        } catch (Exception e) {
            fail();
        }
        try {
            service.addFriend("nuExista", "doesntExist");
            fail();
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Entity does not exist!\n");
        }
        try {
            service.addFriend("savatudor31", "bogdanH");
            fail();
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Users are already friends!\n");
        }
    }

    @Test
    void testRemoveFriend() {
        UserService service = new UserService();
        String username1 = "savatudor31", username2 = "bogdanH";
        String name1 = "Sava", name2 = "Bogdi";
        String surname1 = "Tudor", surname2 = "Bogdi";
        try {
            service.add(username1, name1, surname1);
            service.add(username2, name2, surname2);
            service.addFriend("savatudor31", "bogdanH");
        } catch (Exception e) {
            fail();
        }
        try {
            service.removeFriends("savatudor31", "bogdanH");
        } catch (Exception e) {
            fail();
        }
        try {
            service.removeFriends("savatudor31", "bogdanH");
            fail();
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Users are not friends!\n");
        }

    }
     */

}
