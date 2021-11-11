import com.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.Repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class FileUserRepository {
    @BeforeEach
    void setUp() throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("tests/testData/usersTest.csv"));
        bufferedWriter.write("");
        BufferedWriter bufferedWriter1 = new BufferedWriter(new FileWriter("tests/testData/friendshipTest.csv"));
        bufferedWriter1.write("");

    }

    @Test
    void testExtractAndCreateAsString() {
        Repository<Integer, User> repository = new repository.file.FileUserRepository("tests/testData/usersTest.csv");
        assertEquals(0, repository.size());
        User user1 = new User("Sava", "Tudor");
        user1.setId(1);
        User user2 = new User("Runcan", "Dan");
        user2.setId(2);
        try {
            repository.add(1, user1);
            assertEquals(1, repository.size());
            repository.add(2, user2);
            assertEquals(2, repository.size());
        } catch (Exception e) {
            fail();
        }
        Repository<Integer, User> repository2 = new repository.file.FileUserRepository("tests/testData/usersTest.csv");
        assertEquals(2, repository2.size());

        try {
            repository.remove(1);
            repository.remove(2);
        } catch (Exception e) {
            fail();
        }
    }
}
