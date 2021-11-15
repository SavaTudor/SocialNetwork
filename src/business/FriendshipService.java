package business;

import com.domain.Friendship;
import com.domain.ValidatorFriendship;
import exception.RepositoryException;
import exception.ValidatorException;
import repository.Repository;
import repository.database.DataBaseFriendshipRepository;
import repository.file.FileFrienshipRepository;
import repository.memory.InMemoryRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static build.Build.*;

public class FriendshipService {
    Repository<Integer, Friendship> repository;
    ValidatorFriendship validatorFriendship;

    /**
     * @param fileName name of the file containing data for friendships
     *                 This is the constructor for when we have data stored in files
     */
    public FriendshipService(String fileName) {

        repository = new FileFrienshipRepository(fileName);


        validatorFriendship = new ValidatorFriendship();
    }


    /**
     * This is the constructor for when we save data in memory
     */
    public FriendshipService() {
        try {
            repository = new DataBaseFriendshipRepository(database_url, database_user, database_password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * @return number of elements in the repository
     */
    public int size() {
        return repository.size();
    }

    /**
     * Checks if the repository is empty
     *
     * @return true if it is empty, false otherwise
     */
    public boolean isEmpty() {
        return size() == 0;
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
                Friendship fr = repository.find(id);
                id++;
            }
        } catch (Exception e) {
            return id;
        }
    }

    /**
     * Checks if two users have a friendship
     *
     * @param userA id of the first user
     * @param userB id of the second user
     * @return true if they have a Friendship, false otherwise
     */
    private boolean existsFr(int userA, int userB) {
        List<Friendship> all = repository.all();
        for (Friendship fr : all) {
            if (fr.getUserA() == userA && fr.getUserB() == userB ||
                    fr.getUserA() == userB && fr.getUserB() == userA) {
                return true;
            }
        }
        return false;
    }


    /**
     * @param username1 string representing the username of the first user
     * @param username2 string representing the username of the second user
     * @throws RepositoryException if any of the given users does not exist
     *                             The function adds in the repository a new record representing a new frienship between two users
     */
    public void add(int username1, int username2) throws RepositoryException, ValidatorException {
        int id = generateId();
        Friendship fr = new Friendship(username1, username2);
        fr.setId(id);
        if (!existsFr(username1, username2)) {
            validatorFriendship.valideaza(fr);
            repository.add(id, fr);
        } else {
            throw new RepositoryException("Entity already exists!\n");
        }
    }

    /**
     * @param id       the id of the friendship we want to modify
     * @param newUser1 the new first user of the friendship
     * @param newUser2 the new second user of the friendship
     * @throws RepositoryException if the friendship with the given id does not exist
     */
    public void update(int id, int newUser1, int newUser2) throws RepositoryException, ValidatorException {
        Friendship fr = new Friendship(newUser1, newUser2);
        fr.setId(id);
        validatorFriendship.valideaza(fr);
        repository.update(id, fr);
    }

    /**
     * @param id integer representing the id of the friendship
     * @return a Friendship object with the given id, if it exists in the repository
     * @throws RepositoryException if it does not exist
     */
    public Friendship find(int id) throws RepositoryException {
        return repository.find(id);
    }

    /**
     * @param id integer representing the id of the friendship
     * @throws RepositoryException if a friendship with the given id does not exist
     *                             The function removes the friendship with the given id
     */
    public void remove(int id) throws RepositoryException {
        repository.remove(id);
    }


    /**
     * @return an ArrayList of friendships representing all the friendships from the repository
     */
    public ArrayList<Friendship> all() {
        return repository.all();
    }

    /**
     * Returns a friendship between two given users
     *
     * @param user1 id of the first user
     * @param user2 id of the second user
     * @return a Friendship object with the two given users, if it exists in the repository
     * @throws RepositoryException if between the users does not exist a friendship in the repository
     */
    public Friendship findByUsers(int user1, int user2) throws RepositoryException {
        List<Friendship> all = repository.all();
        for (Friendship fr : all) {
            if (fr.getUserA() == user1 && fr.getUserB() == user2 ||
                    fr.getUserA() == user2 && fr.getUserB() == user1) {
                return fr;
            }
        }
        throw new RepositoryException("Entity does not exist!\n");
    }
}
