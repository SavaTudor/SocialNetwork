package tests;

import com.example.domain.Friendship;
import com.example.domain.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FriendshipTests {

    @Test
    public void testCreate(){
        User userA = new User("Sava", "Tudor");
        userA.setId(1);
        User userB = new User("Sebi", "Sebi");
        userB.setId(2);

        Friendship fr = new Friendship(1,2,LocalDateTime.of(2021, 11,18, 15,30));
        fr.setId(1);
        Friendship fr_same = new Friendship(2,1);


        assertEquals(fr.getUserA(), 1);
        assertEquals(fr.getUserB(), 2);
        assertEquals(fr.toString(), "1 | 1 2 2021-11-18 15:30:00");
        assertEquals(fr, fr_same);
        fr.isPart(1);

        User user3 = new User("Suciu", "Andrei");
        user3.setId(3);
        Friendship fr1 = new Friendship(1,3, LocalDateTime.of(2021, 11,18, 15,30));
        assertEquals(fr1.getUserA(), 1);
        assertEquals(fr1.getUserB(), 3);
        assertEquals(fr1.getDate(), LocalDateTime.of(2021, 11,18, 15,30));
    }
}
