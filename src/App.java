import business.Controller;
import exception.RepositoryException;
import exception.ValidatorException;
import presentation.UI;

public class App {
    public static void main(String[] args) throws ValidatorException, RepositoryException {


//        repository in fisier
//        Controller cont = new Controller("date/users.csv", "date/friendships.csv");

//        repository in baza de date
        Controller cont = new Controller();
        UI ui = new UI(cont);
        ui.run();
    }
}
