package repository.memory;

import exception.RepositoryException;
import repository.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class InMemoryRepository<ID, T> implements Repository<ID, T> {
    private HashMap<ID, T> elements;

    /**
     * Default constructor
     */
    public InMemoryRepository() {
        this.elements = new HashMap<>();
    }

    /**
     * @return an integer representing the number of elements in the repository
     */
    @Override
    public int size() {
        return elements.size();
    }

    /**
     * @return true if the repository is empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * @param t a new T object that we want to add to the repository
     * @throws RepositoryException if the object already exists
     */
    @Override
    public void add(ID id, T t) throws RepositoryException {
        if (elements.containsKey(id)) {
            throw new RepositoryException("Entity already exists!\n");
        }
        elements.put(id, t);
    }


    /**
     * Updates the user with the given id
     * @param id the id of the user we want to modify
     * @param t a new user with the modified fields
     * @throws RepositoryException
     */
    public void update(ID id, T t) throws RepositoryException{
        if(!elements.containsKey(id)){
            throw new RepositoryException("Entity does not exist!\n");
        }
        elements.put(id, t);
    }


    /**
     * @param id integer representing the id of the object that we want to erase, which is
     *           the first element of a pair stored by the hashmap
     * @return a T element representing the deleted item
     * @throws RepositoryException if the object does not exist in the repository
     */
    @Override
    public T remove(ID id) throws RepositoryException {
        if (elements.containsKey(id)) {
            return elements.remove(id);
        }
        throw new RepositoryException("Entity does not exist!\n");
    }

    /**
     * @return an ArrayList representing all the elements in the repository
     */
    @Override
    public ArrayList<T> all() {
        return new ArrayList<T>(elements.values());
    }

    public HashMap<ID, T> getElements() {
        return elements;
    }

    /**
     * @param id integer representing the id of the object that we want to find
     * @return the object, if it exists in the repository
     * @throws RepositoryException if the object does not exist in the repository
     */
    public T find(ID id) throws RepositoryException {
        if (elements.containsKey(id)) {
            return elements.get(id);
        }
        throw new RepositoryException("Entity does not exist!\n");
    }
}
