package business;

import com.domain.Friendship;
import com.domain.User;
import exception.EntityException;
import exception.RepositoryException;
import exception.ValidatorException;
import utils.Graph;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Controller {
    private UserService serviceUsers;
    private FriendshipService serviceFriendships;
    private Graph<Integer> network;


    /**
     * @param filename1 a string representing the filename where the users are stored
     * @param filename2 a string representing the filename where the friendships are stored
     *                  This is the constructor for when we store the data in files
     */
    public Controller(String filename1, String filename2) {
        serviceUsers = new UserService(filename1);
        serviceFriendships = new FriendshipService(filename2);
        network = new Graph<>();
        serviceUsers.all().forEach(user -> {
            network.addVertex(user.getId());
        });
        serviceFriendships.all().forEach(fr -> {
            network.addEdge(fr.getUserA(), fr.getUserB());
        });
    }

    /**
     * This is the constructor for when we store data in memory
     */
    public Controller() {
        serviceUsers = new UserService();
        serviceFriendships = new FriendshipService();
        network = new Graph<>();
        serviceUsers.all().forEach(user -> {
            network.addVertex(user.getId());
        });
        serviceFriendships.all().forEach(fr -> {
            network.addEdge(fr.getUserA(), fr.getUserB());
        });
    }

    /**
     * @param name    string representing the name of the new user we want to add
     * @param surname string representing the surname of the new user we want to add
     * @throws RepositoryException if the given user already exists
     * @throws ValidatorException  if the given strings are not valid
     *                             The function adds a new user to the User repository
     */
    public User add(String name, String surname) throws RepositoryException, ValidatorException {
        User user = serviceUsers.add(name, surname);
        network.addVertex(user.getId());
        return user;
    }

    /**
     * Updates the user with the given id
     *
     * @param id        integer representing the id of the user we want to modify
     * @param firstName new first name
     * @param lastName  new last name
     * @throws RepositoryException if the user with the given id does not exist
     * @throws ValidatorException  if the new fields are not valid
     */
    public void updateUser(int id, String firstName, String lastName) throws RepositoryException, ValidatorException {
        serviceUsers.update(id, firstName, lastName);
    }

    /**
     * @return an ArrayList containing all the users
     */
    public ArrayList<User> allUsers() {
        return serviceUsers.all();
    }


    /**
     * @param id integer representing the id of the user we want to find
     * @return the user with the given username
     * @throws RepositoryException if the searched user does not exist
     */
    public User findUser(int id) throws RepositoryException {
        return serviceUsers.find(id);
    }

    /**
     * Gets all the friends of the user with the given id
     *
     * @param id the user's id
     * @return a List of users
     * @throws RepositoryException if the user with the given id does not exist
     */
    public List<User> getFriends(int id) throws RepositoryException {
        User user = serviceUsers.find(id);
        List<User> rez = new ArrayList<>();
        network.getEdges(id).forEach(nod -> {
            try {
                rez.add(findUser(nod));
            } catch (RepositoryException e) {
                e.printStackTrace();
            }
        });
        return rez;
    }

    /**
     * @param id integer representing the id of the user we want to remove
     * @throws RepositoryException if the user does not exist
     *                             The function erases from the repository the user with the given username
     */
    public void removeUser(int id) throws RepositoryException {
        serviceFriendships.all().forEach((fr) -> {
            if (fr.isPart(id)) {
                try {
                    serviceFriendships.remove(fr.getId());
                } catch (RepositoryException e) {
                    e.printStackTrace();
                }
            }
        });
        serviceUsers.remove(id);
        network.removeVertex(id);


    }


    /**
     * @param username1 integer representing the id of the first user
     * @param username2 integer representing the id of the second user
     * @throws RepositoryException if any of the users does not exist in the repository
     * @throws EntityException     if the friendship between the users already exists
     *                             The function creates a new Friendship between the two given users
     */
    public void addFriend(int username1, int username2) throws RepositoryException, EntityException, ValidatorException {
        serviceFriendships.add(username1, username2);
        network.addEdge(username1, username2);
    }

    /**
     * Updates a friendship with the given id
     *
     * @param id       integer representing the id of the friendship
     * @param newUserA the new first user of a frienship
     * @param newUserB the new second user of a friendship
     * @throws RepositoryException if a friendship with the given id does not exist
     * @throws EntityException     if a friendship with the given id does not exist
     */
    public void updateFriend(int id, int newUserA, int newUserB) throws RepositoryException, EntityException, ValidatorException {
        Friendship old = serviceFriendships.find(id);
        User userA = serviceUsers.find(old.getUserA()), userB = serviceUsers.find(old.getUserB());
        network.removeEdge(userA.getId(), userB.getId());
        serviceFriendships.update(id, newUserA, newUserB);

        //checks if the new users are in the repository
        User newUser1 = serviceUsers.find(newUserA), newUser2 = serviceUsers.find(newUserB);

        network.addEdge(newUserA, newUserB);
    }

    /**
     * @param username1 int representing the id of the first user
     * @param username2 int representing the id of the second user
     * @throws RepositoryException if any of the users does not exist in the repository
     * @throws EntityException     if the friendship between the users does not exist
     *                             The function removes the frienship between the two given users
     */
    public void removeFriends(int username1, int username2) throws RepositoryException, EntityException {
        Friendship friendship = serviceFriendships.findByUsers(username1, username2);
        serviceFriendships.remove(friendship.getId());
        network.removeEdge(username1, username2);
    }

    /**
     * @return an ArrayList of users representing the largest community
     */
    public List<User> largestCommunity() {
        List<List<Integer>> communities = network.components();
        List<User> rez = new ArrayList<>();
        for (List<Integer> list : communities) {
            if (list.size() > rez.size()) {
                rez.clear();
                List<User> temp = new ArrayList<>();
                for (int id : list) {
                    try {
                        temp.add(serviceUsers.find(id));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                rez.addAll(temp);
            }
        }
        return rez;
    }

    public List<Friendship> allFriendships() {
        return serviceFriendships.all();
    }

    /**
     * @return an integer representing the number of communities
     */
    public int communitiesNumber() {
        return network.components().size();
    }

    /**
     * Prints the users network
     */
    public void printNetwork() {
        System.out.println(network.toString());
    }
}
