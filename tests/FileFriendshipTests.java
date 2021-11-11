import com.domain.Friendship;
import com.domain.User;
import org.junit.jupiter.api.Test;
import repository.Repository;
import repository.file.FileFrienshipRepository;

import static org.junit.jupiter.api.Assertions.*;

public class FileFriendshipTests {
    @Test
    void testExtractAndCreateString() {
        Repository<Integer, Friendship> repo = new FileFrienshipRepository("tests/testData/friendshipTest.csv");
        assertTrue(repo.isEmpty());
        User user1 = new User("Sava", "Tudor");
        user1.setId(1);
        User user2 = new User("Runcan", "Dan");
        user2.setId(2);
        User user3 = new User("Sebi", "Sebi");
        user3.setId(3);
        Friendship fr1 = new Friendship(user1.getId(), user2.getId());
        fr1.setId(1);
        Friendship fr2 = new Friendship(user1.getId(), user3.getId());
        fr2.setId(2);
        try {
            repo.add(1, fr1);
            repo.add(2, fr2);
        } catch (Exception e) {
            fail();
        }
        assertEquals(repo.size(), 2);
        Repository<Integer, Friendship> repo2 = new FileFrienshipRepository("tests/testData/friendshipTest.csv");
        assertEquals(repo2.size(), 2);

        try {
            repo.remove(1);
            repo.remove(2);
        } catch (Exception e) {
            fail();
        }
        assertEquals(repo.size(), 0);
    }
}
