import com.domain.Friendship;
import com.domain.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FriendshipTests {

    @Test
    public void testCreate(){
        User userA = new User("Sava", "Tudor");
        userA.setId(1);
        User userB = new User("Sebi", "Sebi");
        userB.setId(2);

        Friendship fr = new Friendship(1,2);
        fr.setId(1);
        Friendship fr_same = new Friendship(2,1);


        assertEquals(fr.getUserA(), 1);
        assertEquals(fr.getUserB(), 2);
        assertEquals(fr.toString(), "1 | 1 2");
        assertEquals(fr, fr_same);
        fr.isPart(1);

    }
}
