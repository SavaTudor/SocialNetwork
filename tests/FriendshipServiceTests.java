import com.domain.Friendship;
import com.domain.User;
import business.FriendshipService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class FriendshipServiceTests {
    @BeforeEach
    void setUp() throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("tests/testData/usersTest.csv"));
        bufferedWriter.write("");
        BufferedWriter bufferedWriter1 = new BufferedWriter(new FileWriter("tests/testData/friendshipTest.csv"));
        bufferedWriter1.write("");

    }
    @Test
    void testCreate(){
        FriendshipService service = new FriendshipService("tests/testData/friendshipTest.csv");
        assertTrue(service.isEmpty());
    }

    @Test
    void tests(){
        User user1 = new User("Sava", "Tudor");
        user1.setId(1);
        User user2 = new User("Suteu", "Sebi");
        user2.setId(2);
        User user3 = new User("aaa", "bbb");
        user3.setId(3);
        FriendshipService service = new FriendshipService("tests/testData/friendshipTest.csv");
        try {
            service.add(user1.getId(), user3.getId());
        }catch (Exception e){
            fail();
        }
        try {
            service.update(0,1,2);
        }catch (Exception e) {
            fail();
        }
        Friendship fr = new Friendship(user1.getId(),user2.getId());
        try {
            assertEquals(service.find(0), fr);
        }catch (Exception e){
            fail();
        }
        try{
            service.add(user1.getId(), user2.getId());
            fail();
        }catch (Exception e){
            assertEquals(e.getMessage(), "Entity already exists!\n");
        }
        assertEquals(service.all().size(), 1);
        try{
            service.findByUsers(1,2);
        }catch (Exception e){
            fail();
        }
        try{
            service.remove(0);
        }catch (Exception e){
            fail();
        }
        try{
            service.remove(0);
            fail();
        }catch (Exception e){
            assertEquals(e.getMessage(), "Entity does not exist!\n");
        }
        try{
            service.findByUsers(2,3);
            fail();
        }catch (Exception e){
            assertEquals(e.getMessage(), "Entity does not exist!\n");
        }

    }
}
