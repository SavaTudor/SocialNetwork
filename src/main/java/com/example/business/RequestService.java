package com.example.business;

import com.example.domain.FriendRequest;
import com.example.domain.Status;
import com.example.domain.ValidatorRequest;
import com.example.exception.RepositoryException;
import com.example.exception.ValidatorException;
import com.example.repository.Repository;
import com.example.repository.database.DataBaseRequestsRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RequestService {
    private Connection connection;
    private Statement statement;
    Repository<Integer, FriendRequest> repository;
    ValidatorRequest validatorRequest;

    /**
     * constructor
     *
     * @param url      the url of database
     * @param user     the user of database
     * @param password the password of database
     */
    public RequestService(String url, String user, String password) {
        validatorRequest = new ValidatorRequest();
        try {
            repository = new DataBaseRequestsRepository(url, user, password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public RequestService(Connection connection, Statement statement) {
        this.connection = connection;
        this.statement = statement;
        repository = new DataBaseRequestsRepository(connection, statement);
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
                FriendRequest fr = repository.find(id);
                id++;
            }
        } catch (Exception e) {
            return id;
        }
    }

    /**
     * Checks if two users have a request betweend them
     *
     * @param userA id of the first user
     * @param userB id of the second user
     * @return true if they have a request betweent them, false otherwise
     */
    private boolean existsFr(int userA, int userB) {
        List<FriendRequest> all = repository.all();
        for (FriendRequest fr : all) {
            if (fr.getFrom() == userA && fr.getTo() == userB ||
                    fr.getFrom() == userB && fr.getTo() == userA) {
                return true;
            }
        }
        return false;
    }


    /**
     * @param username1 string representing the username of the first user
     * @param username2 string representing the username of the second user
     * @throws RepositoryException if any of the given users does not exist
     *                             The function adds in the repository a new record representing a new friend request
     */
    public void add(int username1, int username2, Status status) throws RepositoryException, ValidatorException {
        int id = generateId();
        FriendRequest fr = new FriendRequest(username1, username2, status);
        fr.setId(id);
        if (!existsFr(username1, username2)) {
            validatorRequest.valideaza(fr);
            repository.add(id, fr);
        } else {
            throw new RepositoryException("Entity already exists!\n");
        }
    }

    /**
     * @param id       the id of the friendship we want to modify
     * @param newUser1 the new first user of the friendship
     * @param newUser2 the new second user of the friendship
     * @param status   the new status of the request
     * @throws RepositoryException if the request with the given id does not exist
     */
    public void update(int id, int newUser1, int newUser2, Status status) throws RepositoryException, ValidatorException {
        FriendRequest fr = new FriendRequest(newUser1, newUser2, status);
        fr.setId(id);
        validatorRequest.valideaza(fr);
        repository.update(id, fr);
    }

    /**
     * @param id integer representing the id of the request
     * @return a FriendRequest object with the given id, if it exists in the repository
     * @throws RepositoryException if it does not exist
     */
    public FriendRequest find(int id) throws RepositoryException {
        return repository.find(id);
    }

    /**
     * @param id integer representing the id of the request
     * @throws RepositoryException if a friendship with the given id does not exist
     *                             The function removes the friendship with the given id
     */
    public void remove(int id) throws RepositoryException {
        repository.remove(id);
    }


    /**
     * @return an ArrayList of FriendRequests representing all the friendships from the repository
     */
    public ArrayList<FriendRequest> all() {
        return repository.all();
    }

    /**
     * Returns a request between two given users
     *
     * @param user1 id of the first user
     * @param user2 id of the second user
     * @return a FriendRequest object with the two given users, if it exists in the repository
     * @throws RepositoryException if between the users does not exist a friend request in the repository
     */
    public FriendRequest findByUsers(int user1, int user2) throws RepositoryException {
        List<FriendRequest> all = repository.all();
        for (FriendRequest fr : all) {
            if (fr.getFrom() == user1 && fr.getTo() == user2 ||
                    fr.getFrom() == user2 && fr.getTo() == user1) {
                return fr;
            }
        }
        throw new RepositoryException("Entity does not exist!\n");
    }

}
