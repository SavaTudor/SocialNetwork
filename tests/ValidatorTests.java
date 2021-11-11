import com.domain.User;
import com.domain.ValidatorUser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ValidatorTests {

    @Test
    void tests(){
        User userGood = new User("Sava", "Tudor");
        userGood.setId(1);
        User userBad = new User("", "");
        ValidatorUser validatorUser = new ValidatorUser();
        try {
            validatorUser.valideaza(userGood);
        }catch(Exception e){
            fail();
        }
        try {
            validatorUser.valideaza(userBad);
            fail();
        }catch (Exception e){
            assertEquals(e.getMessage(), "Invalid first name!\nInvalid last name!\n");
        }
    }
}
