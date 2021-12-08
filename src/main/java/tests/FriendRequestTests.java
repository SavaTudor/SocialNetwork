package tests;

import com.example.domain.FriendRequest;
import org.junit.jupiter.api.Test;

import static com.example.domain.Status.APPROVED;
import static com.example.domain.Status.PENDING;
import static org.junit.jupiter.api.Assertions.*;

public class FriendRequestTests {

    @Test
    void test() {
        FriendRequest fr = new FriendRequest(1, 2, PENDING);
        fr.setId(1);
        FriendRequest fr2 = new FriendRequest(1, 2, APPROVED);
        fr2.setId(2);
        assertEquals(fr.getFrom(), 1);
        assertEquals(fr.getTo(), 2);
        assertEquals(fr.getStatus(), PENDING);
        assertEquals(fr.toString(), "from=1, to=2, status=PENDING");
        assertEquals(fr, fr2);
        assertNotEquals(fr.hashCode(), fr2.hashCode());
        FriendRequest fr_eq = new FriendRequest(1, 2, PENDING);
        fr_eq.setId(1);
        assertEquals(fr_eq, fr);
        assertEquals(fr.hashCode(), fr_eq.hashCode());
        assertTrue(fr.isFrom(1));
        assertTrue(fr.isTo(2));

    }


}
