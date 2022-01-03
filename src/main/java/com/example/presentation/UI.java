package com.example.presentation;

import com.example.business.Controller;
import com.example.domain.User;
import com.example.domain.UsersFriendsDTO;
import com.example.exception.EntityException;
import com.example.exception.RepositoryException;
import com.example.exception.ValidatorException;

import java.util.List;
import java.util.Scanner;

import static com.example.build.Build.formatter;

public class UI {
    private Controller service;

    /**
     * Constructor for the UI class
     */
    public UI(Controller cont) {
        this.service = cont;
    }

    /**
     * Reads a first and last name and calls the add function from the service with the given parameters
     *
     * @param input a Scanner for input
     * @throws ValidatorException  if the given strings are not valid
     * @throws RepositoryException if the object already exists in the repository
     */
    private void add(Scanner input) throws ValidatorException, RepositoryException {
        String firstName, lastName;
        System.out.println("Enter your desired firstName!");
        firstName = input.nextLine();
        System.out.println("Enter your desired lastName!");
        lastName = input.nextLine();
        System.out.println("Added the new user with the id: " + service.add(null, firstName, lastName,"oijhgvc").getId());
    }

    /**
     * Reads an id and searches for a user with the given id
     *
     * @param input a Scanner for input
     * @throws RepositoryException if the user that we search is not in the repository
     */
    private void find(Scanner input) throws RepositoryException {
        int id;
        System.out.println("Enter the id of the user you are searching!");
        id = Integer.parseInt(input.nextLine());
        User user = service.findUser(id);
        System.out.println(user.toString());
        System.out.print("Friends: ");
        service.getFriends(id).forEach(x -> {
            System.out.print(x + "; ");
        });
        System.out.println();
    }

    /**
     * Reads an id and new first and last names and calls the updateuser function from the controller
     *
     * @param input a Scanner for input
     * @throws RepositoryException if the user with the given id does not exist
     * @throws ValidatorException  if the new fields are not valid
     */
    private void updateUser(Scanner input) throws RepositoryException, ValidatorException {
        int id;
        String firstName, lastName;
        System.out.println("Enter the id of the user you want to update!");
        id = Integer.parseInt(input.nextLine());
        System.out.println("Enter the new firstName!");
        firstName = input.nextLine();
        System.out.println("Enter the new lastName!");
        lastName = input.nextLine();
        service.updateUser(id, null, firstName, lastName,"sdfgbn");
    }

    /**
     * Reads an id and removes the user with the given id
     *
     * @param input a Scanner for input
     * @throws RepositoryException if the user that we want to delete does not exist
     */
    private void removeUser(Scanner input) throws RepositoryException {
        int id;
        System.out.println("Enter the id of the user you want to delete!");
        id = Integer.parseInt(input.nextLine());
        service.removeUser(id);
    }

    /**
     * Reads the ids of two users and creates a friendship between them
     *
     * @param input a Scanner for input
     * @throws EntityException if the Friendship already exists
     */
    private void addFriendship(Scanner input) throws EntityException, RepositoryException, ValidatorException {
        int username1, username2;
        System.out.println("Enter the id of the first person!");
        username1 = Integer.parseInt(input.nextLine());
        System.out.println("Enter the id of the second person");
        username2 = Integer.parseInt(input.nextLine());
        service.addFriend(username1, username2);
    }

    /**
     * Reads the ids of two users and deletes the frienship between them
     *
     * @param input a Scanner for input
     * @throws EntityException if the Friendship does not exist;
     */
    private void removeFriendship(Scanner input) throws EntityException, RepositoryException {
        int username1, username2;
        System.out.println("Enter the id of the first person!");
        username1 = Integer.parseInt(input.nextLine());
        System.out.println("Enter the id of the second person");
        username2 = Integer.parseInt(input.nextLine());
        service.removeFriends(username1, username2);
    }


    /**
     * Prints the number of communities from the network
     */
    private void communitiesNumber() {
        int sol = service.communitiesNumber();
        System.out.println("The number of communities is " + sol);
    }

    /**
     * Prints the size of the largest community and, if the user wants, it prints the users from the community
     *
     * @param input a Scanner for input
     */
    private void largestCommunity(Scanner input) {
        List<User> largest = service.largestCommunity();
        System.out.println("Largest community has " + largest.size() + " members");
        System.out.println("Do you want to see the members?(Y/N)");
        String rasp = input.nextLine();
        if (rasp.equals("Y")) {
            largest.forEach((user) -> {
                System.out.print(user + "  ");
            });
            System.out.println();
        }
    }

    /**
     * Reads an id of a friendship and two user ids and calls the updateFriendship function from the controller
     *
     * @param input a Scanner for input
     * @throws EntityException     if the friendship does not exist
     * @throws RepositoryException
     */
    private void updateFriendsip(Scanner input) throws EntityException, RepositoryException, ValidatorException {
        int id;
        int firstUser, secondUser;
        System.out.println("Enter the id of the friendship you want to update!");
        id = Integer.parseInt(input.nextLine());
        System.out.println("Enter the id of the new first user!");
        firstUser = Integer.parseInt(input.nextLine());
        System.out.println("Enter the id of the new second user!");
        secondUser = Integer.parseInt(input.nextLine());
        service.updateFriend(id, firstUser, secondUser);
    }

    /**
     * Prints all users
     */
    private void allUsers() {
        service.allUsers().forEach((user) -> System.out.println(user.toString()));
    }

    private void allFriendships() {
        service.allFriendships().forEach(friendship -> {
            try {
                System.out.println(friendship.getId() + ": " + service.findUser(friendship.getUserA()) + " - " + service.findUser(friendship.getUserB()) + " since: " + friendship.getDate().format(formatter));
            } catch (RepositoryException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Prints the network
     */
    private void printNetwork() {
        service.printNetwork();
    }

    /**
     * @param input Scanner for input
     * @throws ValidatorException  if month is not valid
     * @throws RepositoryException if user does not exist
     */
    private void findFriendshipByUserAndMonth(Scanner input) throws ValidatorException, RepositoryException {
        int id;
        int month;
        System.out.println("Enter the id of user!");
        id = Integer.parseInt(input.nextLine());
        System.out.println("Enter the month!");
        month = Integer.parseInt(input.nextLine());
        List<UsersFriendsDTO> usersFriendsDTOList = service.findFriendshipByUserAndMonth(id, month);
        if (usersFriendsDTOList.size() == 0)
            System.out.println("lista vida!\n");
        usersFriendsDTOList.forEach(System.out::println);
    }


    private void loggin(Scanner input) {
        int id;
        System.out.println("Enter the id of first user!");
        id = Integer.parseInt(input.nextLine());
        MessageUi run = null;
        try {
            run = new MessageUi(service, id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        try {
            run.run();
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
        } catch (RepositoryException e) {
            System.out.println(e.getMessage());
        }
    }

    private void sendFriendInvite(Scanner input) throws RepositoryException, ValidatorException {
        int id, id1;
        System.out.println("Login with your id");
        id = Integer.parseInt(input.nextLine());

        System.out.println("Logged in as " + service.findUser(id));
        System.out.println("Sent friend request to id: ");
        id1 = Integer.parseInt(input.nextLine());
        service.findUser(id1);
        service.addFriendRequest(id, id1);
        System.out.println("Friend request sent!");
    }

    private void seeFriendInvites(Scanner input) throws RepositoryException {
        int id;
        System.out.println("Login with your id");
        id = Integer.parseInt(input.nextLine());
        System.out.println("Logged in as " + service.findUser(id));
        var requests = service.getFriendRequests(id);
        if (requests.size() == 1) {
            System.out.println("You have " + requests.size() + " friend request!");
        } else {
            System.out.println("You have " + requests.size() + " friend requests!");
        }
        System.out.print("From: ");
        requests.forEach(dto -> System.out.println(dto.getFrom()));
    }

    private void respondInvite(Scanner input) throws RepositoryException, ValidatorException, EntityException {
        int id, id1;
        String st;
        System.out.println("Login with your id");
        id = Integer.parseInt(input.nextLine());
        System.out.println("Logged in as " + service.findUser(id));

        System.out.println("Respond to user with the id: ");
        id1 = Integer.parseInt(input.nextLine());

        System.out.println("APPROVE / REJECT ?");
        st = input.nextLine();
        service.respondFriendRequest(id1, id, st);
    }

    private void respondToAllInvites(Scanner input) throws RepositoryException {
        int id;
        String st;
        System.out.println("Login with your id");
        id = Integer.parseInt(input.nextLine());
        System.out.println("Logged in as " + service.findUser(id));

        System.out.println("APPROVE / REJECT all requests?");
        st = input.nextLine();
        service.respondToAllRequests(id, st);
    }

    /**
     * Prints the menu
     */
    private void menu() {
        System.out.println("1.Add");
        System.out.println("2.Show all users");
        System.out.println("3.Find user");
        System.out.println("4.Remove user");
        System.out.println("5.Create friendship");
        System.out.println("6.Delete friendship");
        System.out.println("7.Number of communities");
        System.out.println("8.Largest community");
        System.out.println("9.Print network");
        System.out.println("10.Update user");
        System.out.println("11.Show all friendships");
        System.out.println("12.Update friendship");
        System.out.println("13.Find friendship by user's id and month");
        System.out.println("14.Login");
        System.out.println("20.Send friend request");
        System.out.println("21.See friend requests");
        System.out.println("22. Respond to friend request");
        System.out.println("23. Respond to all friend requests");
        System.out.println("0.Exit");
    }

    /**
     * Reads a command and calls the appropriate function
     *
     * @throws ValidatorException
     * @throws RepositoryException
     */
    public void run() throws ValidatorException, RepositoryException {
        Scanner input = new Scanner(System.in);
        int cmd;
        do {
            menu();
            cmd = Integer.parseInt(input.nextLine());
            try {
                switch (cmd) {
                    case 1 -> add(input);
                    case 2 -> allUsers();
                    case 3 -> find(input);
                    case 4 -> removeUser(input);
                    case 5 -> addFriendship(input);
                    case 6 -> removeFriendship(input);
                    case 7 -> communitiesNumber();
                    case 8 -> largestCommunity(input);
                    case 9 -> printNetwork();
                    case 10 -> updateUser(input);
                    case 11 -> allFriendships();
                    case 12 -> updateFriendsip(input);
                    case 13 -> findFriendshipByUserAndMonth(input);
                    case 14-> loggin(input);
                    case 20 -> sendFriendInvite(input);
                    case 21 -> seeFriendInvites(input);
                    case 22 -> respondInvite(input);
                    case 23 -> respondToAllInvites(input);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } while (cmd != 0);
    }


}
