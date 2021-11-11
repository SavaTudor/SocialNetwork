package repository;

import com.domain.User;
import exception.RepositoryException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public interface Repository<ID, T> {

    /**
     * @return an integer representing the number of elements in the repository
     */
    int size();

    /**
     * @return true if the repository is empty, false otherwise
     */
    boolean isEmpty();

    /**
     * @param t a new T object that we want to add to the repository
     * @throws RepositoryException if the object already exists
     */
    void add(ID id, T t) throws RepositoryException;

    /**
     * @param id integer representing the id of the object that we want to erase, which is
     *           the first element of a pair stored by the hashmap
     * @return a T element representing the deleted item
     * @throws RepositoryException if the object does not exist in the repository
     */
    T remove(ID id) throws RepositoryException;

    /**
     * @return an ArrayList representing all the elements in the repository
     */
    ArrayList<T> all();


    /**
     * @param id integer representing the id of the object that we want to find
     * @return the object, if it exists in the repository
     * @throws RepositoryException if the object does not exist in the repository
     */
    T find(ID id) throws RepositoryException;

    /**
     * Updates the user with the given id
     *
     * @param id the id of the user we want to modify
     * @param t  a new user with the modified fields
     * @throws RepositoryException
     */
    void update(ID id, T t) throws RepositoryException;

    /**
     * Gets a hashmap with the pairs (ID, T) for the elements in the repository
     * @return HashMap<ID, T>, where the ID is T.getId()
     */
    HashMap<ID, T> getElements();
}
