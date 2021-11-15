package business;

import com.domain.User;
import com.domain.ValidatorUser;
import exception.RepositoryException;
import exception.ValidatorException;
import repository.Repository;
import repository.database.DataBaseUserRepository;
import repository.file.FileUserRepository;
import repository.memory.InMemoryRepository;

import java.sql.SQLException;
import java.util.ArrayList;

import static build.Build.*;


public class UserService {
    Repository<Integer, User> repository;
    ValidatorUser validator;

    /**
     * Default constructor
     */
    public UserService() {
        try {
            this.repository = new DataBaseUserRepository(database_url, database_user, database_password);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        this.validator = new ValidatorUser();
    }

    public UserService(String fileName) {
        this.repository = new FileUserRepository(fileName);

        this.validator = new ValidatorUser();
    }

    /**
     * @return an integer representing the size of the repository
     */
    public int size() {
        return repository.size();
    }

    /**
     * @return true if the repository is empty, false otherwise
     */
    public boolean isEmpty() {
        return repository.isEmpty();
    }

    /**
     * Generates a new id based on how many records are already in the repository
     *
     * @return an integer representing the generated id
     * @throws RepositoryException
     */
    private int generateId() throws RepositoryException {
        int id = repository.size();
        try {
            while (true) {
                User user = repository.find(id);
                id++;
            }
        } catch (Exception e) {
            return id;
        }
    }

    /**
     * @param firstName string representing the first name of the user we want to add
     * @param lastName  string representing the last name of the user we want to add
     * @throws ValidatorException  if the desired user is not valid
     * @throws RepositoryException if the user already exists in the repository
     */
    public User add(String firstName, String lastName) throws RepositoryException, ValidatorException {
        User user = new User(firstName, lastName);
        int id = generateId();
        user.setId(id);
        validator.valideaza(user);
        repository.add(id, user);
        return user;
    }

    /**
     * Updates the user with the given id
     *
     * @param id        integer representing the id of the user we want to modify
     * @param firstName new first name
     * @param lastName  new last name
     * @throws RepositoryException if a user with the given id does not exist in the repository
     * @throws ValidatorException  if the new names are not valid
     */
    public void update(int id, String firstName, String lastName) throws RepositoryException, ValidatorException {
        User user = new User(firstName, lastName);
        user.setId(id);
        validator.valideaza(user);
        repository.update(id, user);
    }

    /**
     * @return an ArrayList representing all the users in the repository
     */
    public ArrayList<User> all() {
        return repository.all();
    }

    /**
     * @param id integer representing the id of the user we want to find
     * @return a user that has the given id
     * @throws RepositoryException if a user with the given username does not exists in the repository
     */
    public User find(int id) throws RepositoryException {
        return repository.find(id);
    }


    /**
     * @param id integer representing the id of the user we want to erase
     * @throws RepositoryException if a user with the given id does not exist in the repository
     */
    public void remove(int id) throws RepositoryException {
        repository.remove(id);
    }

}
