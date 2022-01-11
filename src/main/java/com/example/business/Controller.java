package com.example.business;

import com.example.domain.*;
import com.example.exception.EntityException;
import com.example.exception.RepositoryException;
import com.example.exception.ValidatorException;
import utils.Graph;
import java.time.LocalDateTime;
import java.util.Observable;
import java.util.*;
import java.util.stream.Collectors;


import com.example.domain.*;
import com.example.exception.EntityException;
import com.example.exception.RepositoryException;
import com.example.exception.ValidatorException;
import utils.Graph;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Controller extends Observable {
    private UserService serviceUsers;
    private FriendshipService serviceFriendships;
    private RequestService serviceRequests;
    private MessageService messageService;
    private Graph<Integer> network;


    /**
     * @param filename1 a string representing the filename where the users are stored
     * @param filename2 a string representing the filename where the friendships are stored
     *                  This is the constructor for when we store the data in files
     */
    /*
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
    }*/

    /**
     * constructor
     *
     * @param url      the url of database
     * @param dbuser   the user of database
     * @param password the password of database
     */
    public Controller(String url, String dbuser, String password) {
        serviceUsers = new UserService(url, dbuser, password);
        serviceFriendships = new FriendshipService(url, dbuser, password);
        serviceRequests = new RequestService(url, dbuser, password);
        messageService = new MessageService(url, dbuser, password);
        network = new Graph<>();
        serviceUsers.all().forEach(user -> {
            network.addVertex(user.getId());
        });
        serviceFriendships.all().forEach(fr -> {
            network.addEdge(fr.getUserA(), fr.getUserB());
        });
    }

    /**
     * Deletes the current friendship network and updates it with new data from the friendship service
     */
    public void refreshNetwork() {
        network.delete();
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
    public User add(String username, String name, String surname, String password) throws RepositoryException, ValidatorException {
        User user = serviceUsers.add(username, name, surname, password);
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
    public void updateUser(int id, String username, String firstName, String lastName, String password) throws RepositoryException, ValidatorException {
        serviceUsers.update(id, username, firstName, lastName, password);
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
    public List<UsersFriendsDTO> getFriends(int id) throws RepositoryException {
        User user = serviceUsers.find(id);
        List<UsersFriendsDTO> rez = new ArrayList<>();
        network.getEdges(id).forEach(nod -> {
            try {
                User other = findUser(nod);
                Friendship fr = serviceFriendships.findByUsers(id, other.getId());
                rez.add(new UsersFriendsDTO(user, other, fr.getDate()));
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
        serviceFriendships.add(username1, username2, LocalDateTime.now());
        network.addEdge(username1, username2);
        setChanged();
        notifyObservers();
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
        setChanged();
        notifyObservers();
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

    /**
     * Get all the friendships from the friendship service
     * @return a List of friendships with all the friendships
     */
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

    /**
     * Gets all the friends of the user with the given id, created in a given month
     *
     * @param user  the user's id
     * @param month the friendship's month
     * @return a list of UsersFriendsDTO
     * @throws RepositoryException if the user with the given id does not exist
     * @throws ValidatorException  if month is not valid
     */
    public List<UsersFriendsDTO> findFriendshipByUserAndMonth(int user, int month) throws RepositoryException, ValidatorException {
        //validate the params
        if (month < 0 || month > 12)
            throw new ValidatorException("invalid month!\n");
        User user1 = serviceUsers.find(user);

        List<Friendship> friendshipList = serviceFriendships.all();
        List<UsersFriendsDTO> usersFriendsDTOList = new ArrayList<>();
        friendshipList.stream()
                .filter(x -> x.getDate().getMonth().getValue() == month)
                .forEach(x -> {
                    if (x.getUserA() == user || x.getUserB() == user) {
                        try {
                            User user2 = serviceUsers.find(x.getUserB());
                            usersFriendsDTOList.add(new UsersFriendsDTO(user1, user2, x.getDate()));
                        } catch (RepositoryException e) {
                            e.printStackTrace();
                        }
                    }
                });
        return usersFriendsDTOList;
    }


    /**
     * @param from integer representing the id of the user who is sending a friend request
     * @param to integer representing the id of the user who is receiving the friend request
     * @throws ValidatorException if the new request is not valid
     * @throws RepositoryException if between the two users already exists a friendship or a request
     */
    public void addFriendRequest(int from, int to) throws ValidatorException, RepositoryException {
        serviceRequests.add(from, to, Status.PENDING);
    }

    /**
     * @param from integer representing the id of the user who sent the request
     * @param to integer representing the id of the user who received the friend request
     * @param st the response to the request
     * @throws RepositoryException if there is not a request between the two users
     * @throws ValidatorException if the new status is not valid
     * @throws EntityException
     */
    public void respondFriendRequest(int from, int to, String st) throws RepositoryException, ValidatorException, EntityException {
        Status status = st.equals("APPROVE") ? Status.APPROVED :
                Status.REJECTED;
        FriendRequest fr = serviceRequests.findByUsers(from, to);
        serviceRequests.update(fr.getId(), from, to, status);
        if(status==Status.APPROVED){
            addFriend(from, to);
            serviceRequests.remove(fr.getId());
            setChanged();
            notifyObservers();
        }else{
//            daca la respingerea unei cereri de prietenie vrem sa o stergem din repository,
//            pentru a putea permite o noua cerere intre cei doi useri
            serviceRequests.remove(fr.getId());
        }
    }

    /**
     *
     * @param from integer representing the id of the user who sent the request
     * @param to integer representing the id of the user who received the request
     * @throws RepositoryException if a request between the two users does not exists
     * the function deletes the request betweent the user with the id from and the
     *      user with the id to
     */
    public void deleteFriendRequest(int from, int to) throws RepositoryException {
        FriendRequest fr = serviceRequests.findByUsers(from, to);
        serviceRequests.remove(fr.getId());
    }

    /**
     *
     * @param id the id of the user of which we want to see the friend requests
     * @return a list of userRequestsDto which represents the users which have sent the user with the given id
     *          a friend request
     */
    public List<UsersRequestsDTO> getFriendRequests(int id) {
        return serviceRequests.all().stream().filter(fr -> fr.isTo(id)).map(fr -> {
            UsersRequestsDTO dto = null;
            try {
                dto = new UsersRequestsDTO(serviceUsers.find(fr.getFrom()), serviceUsers.find(fr.getTo()), fr.getStatus());
            } catch (RepositoryException e) {
                e.printStackTrace();
            }
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * @param id the id of the user who sent the friend requests
     * @return a list of userRequestsDto which represents the users to which the users with the given id
     * has sent a friend request
     */
    public List<UsersRequestsDTO> sentFriendRequests(int id) {
        return serviceRequests.all().stream().
                filter(fr -> fr.isFrom(id)).
                map(fr -> {
                    UsersRequestsDTO dto = null;
                    try {
                        dto = new UsersRequestsDTO(serviceUsers.find(fr.getFrom()), serviceUsers.find(fr.getTo()), fr.getStatus());
                    } catch (RepositoryException e) {
                        e.printStackTrace();
                    }
                    return dto;
                }).collect(Collectors.toList());
    }

    /**
     * @param id integer representing the id of the user who is responding to all his requests
     * @param st string representing the response
     */
    public void respondToAllRequests(int id, String st){
        getFriendRequests(id).forEach(fr-> {
            try {
                respondFriendRequest(fr.getFrom().getId(), fr.getTo().getId(), st);
                setChanged();
                notifyObservers();
            } catch (RepositoryException | EntityException | ValidatorException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Add a message in database
     * @param from User representing the sender of the message
     * @param to List of User representing the receiver of the message
     * @param message String representing the message of the Message
     * @throws RepositoryException if there is another message with the same id in the database or if i want so send myself a message
     * @throws ValidatorException if the new Message is not valid
     */
    public void addNewMessage(int from, List<Integer> to, String message) throws RepositoryException, ValidatorException {
        messageService.addNewMessage(from, to, message);
        setChanged();
        notifyObservers();
    }

    /**
     * @return a list of messages representing all the sent messages
     */
    public List<Message> allMessage() {
        List<MessageDTO> messageDTOS = messageService.all();
        List<Message> messages = new ArrayList<>();
        for(MessageDTO messageDTO : messageDTOS){
            List<User> to = new ArrayList<>();
            for(Integer user : messageDTO.getTo())
            {
                try {
                    User user1 = serviceUsers.find(user);
                    to.add(user1);
                } catch (RepositoryException e) {
                    e.printStackTrace();
                }
            }
            User from = null;
            try {
                from = serviceUsers.find(messageDTO.getFrom());
            } catch (RepositoryException e) {
                e.printStackTrace();
            }
            Message message = new Message(from, to, messageDTO.getMessage());
            message.setData(messageDTO.getData());
            message.setId(messageDTO.getId());
            if(messageDTO.getReply() != 0){
                try {
                    Message message1 = findMessage(messageDTO.getReply());
                    message.setReply(message1);
                } catch (RepositoryException e) {
                    e.printStackTrace();
                }
            }
            messages.add(message);
        }
        return messages;
    }


    /**
     * Remove a message from database
     * @param id Integer representing the id of message which we want to remove
     * @throws RepositoryException if there is no message with the id given in the database
     */
    public void removeMessage(int id) throws RepositoryException {
        messageService.removeMessage(id);
        setChanged();
        notifyObservers();
    }

    /**
     * Find a message in database
     * @param id Integer representing the id of the message which we are looking for
     * @return Message representing the message which we are looking for
     * @throws RepositoryException if there is no message with the id given in the database
     */
    public Message findMessage(int id) throws RepositoryException {
        MessageDTO messageDTO = messageService.findMessage(id);
        Message message;
        List<User> toUser = new ArrayList<>();
        for (Integer user : messageDTO.getTo()) {
            try {
                User user1 = serviceUsers.find(user);
                toUser.add(user1);
            } catch (RepositoryException e) {
                e.printStackTrace();
            }
        }
        User from = null;
        try {
            from = serviceUsers.find(messageDTO.getFrom());
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
        message = new Message(from, toUser, messageDTO.getMessage());
        message.setData(messageDTO.getData());
        message.setId(messageDTO.getId());
        if (messageDTO.getReply() != 0) {
            try {
                Message message1 = findMessage(messageDTO.getReply());
                message.setReply(message1);
            } catch (RepositoryException e) {
                e.printStackTrace();
            }
        }
        message.setReply(null);
        return message;

    }

    /**
     * Find all messages for a user
     * @param idUser Integer representing the id of the user
     * @return List of Message representing all messages for a user
     * @throws RepositoryException if there are no messages with for this user in database
     */
    public List<Message> findMessages(int idUser) throws RepositoryException {
        List<MessageDTO> messageDTOS = messageService.findMessages(idUser);
        List<Message> messages = new ArrayList<>();
        for(MessageDTO messageDTO : messageDTOS){
            List<User> to = new ArrayList<>();
            for(Integer user : messageDTO.getTo())
            {
                try {
                    User user1 = serviceUsers.find(user);
                    to.add(user1);
                } catch (RepositoryException e) {
                    e.printStackTrace();
                }
            }
            User from = null;
            try {
                from = serviceUsers.find(messageDTO.getFrom());
            } catch (RepositoryException e) {
                e.printStackTrace();
            }
            Message message = new Message(from, to, messageDTO.getMessage());
            message.setData(messageDTO.getData());
            message.setId(messageDTO.getId());
            if(messageDTO.getReply() != 0){
                try {
                    Message message1 = findMessage(messageDTO.getReply());
                    message.setReply(message1);
                } catch (RepositoryException e) {
                    e.printStackTrace();
                }
            }
            messages.add(message);
        }
        return messages;
    }

    /**
     * @param id1 integer representing the id of the message
     * @param from integer representing the user who sent the message
     * @param to list of integers representing the users who received the message
     * @param message string representing the message
     * @throws RepositoryException if any of the given ids does not exist
     */
    public void updateMessage(int id1, int from, List<Integer> to, String message, Integer reply) throws RepositoryException, ValidatorException {
        messageService.updateMessage(id1, from, to, message, reply);
    }

    /**
     * Reply message
     * @param from User representing the sender of the message
     * @param to List of User representing the receiver of the message
     * @param mess String representing the message of the Message
     * @param message Integer representing the id of the message which we want to reply
     * @throws RepositoryException if there is another message with the same id in the database or if i want so send myself a message or
     * if the sender is invalid or if the receptor is iinvalid
     * @throws ValidatorException if the new Message is not valid
     */
    public void replyMessage(int from, List<Integer> to, String mess, int message) throws RepositoryException, ValidatorException {
        messageService.replyMessage(from, to, mess, message);
        setChanged();
        notifyObservers();
    }

    public void replyAll(int from, String mess) throws ValidatorException, RepositoryException {
        List<User> users = getFriendsForAUser(from);
        List<Integer> to = new ArrayList<>();
        users.forEach(x->to.add(x.getId()));
        messageService.addNewMessage(from, to, mess);
        setChanged();
        notifyObservers();

    }

    /**
     * return a list of Message sent between tho users sorted by data
     * @param id1 Integer representing the id of the first User
     * @param id2 Integer representing the id of the second User
     * @return List of Messages
     * @throws RepositoryException if id1 or id 2 are not valid
     */
    public List<MessageDTO> getConversation(int id1, int id2) throws RepositoryException {
        List<MessageDTO> messagesD = messageService.all();
        return messagesD.stream().
                filter(x->((x.getFrom() == id1 && x.getTo().contains(id2)) || (x.getFrom() == id2 && x.getTo().contains(id1)))
                )
                .sorted(Comparator.comparing(MessageDTO::getData))
                .collect(Collectors.toList());
//        List<Message> messages = new ArrayList<>();
//        for(MessageDTO messageDTO : messageDTOS){
//            List<User> to = new ArrayList<>();
//            for(Integer user : messageDTO.getTo())
//            {
//                try {
//                    User user1 = serviceUsers.find(user);
//                    to.add(user1);
//                } catch (RepositoryException e) {
//                    e.printStackTrace();
//                }
//            }
//            System.out.println("gata to");
//            User from = null;
//            try {
//                from = serviceUsers.find(messageDTO.getFrom());
//            } catch (RepositoryException e) {
//                e.printStackTrace();
//            }
//            Message message = new Message(from, to, messageDTO.getMessage());
//            message.setData(messageDTO.getData());
//            message.setId(messageDTO.getId());
//            if(messageDTO.getReply() != 0){
//                try {
//                    Message message1 = findMessage(messageDTO.getReply());
//                    System.out.println("pauza");
//                    message.setReply(message1);
//                } catch (RepositoryException e) {
//                    e.printStackTrace();
//                }
//            }
//            messages.add(message);
//        }
//        return messages;
    }

    /**
     * Find all messages sent by a user
     * @param id Integer representing the id of the user
     * @return List of Message representing all messages sent by a user
     */
    public List<Message> allMessageByUser(int id){
        List <MessageDTO> messageDTOS = messageService.allMessageByUser(id);
        List<Message> messages = new ArrayList<>();
        for(MessageDTO messageDTO : messageDTOS){
            List<User> to = new ArrayList<>();
            for(Integer user : messageDTO.getTo())
            {
                try {
                    User user1 = serviceUsers.find(user);
                    to.add(user1);
                } catch (RepositoryException e) {
                    e.printStackTrace();
                }
            }
            User from = null;
            try {
                from = serviceUsers.find(messageDTO.getFrom());
            } catch (RepositoryException e) {
                e.printStackTrace();
            }
            Message message = new Message(from, to, messageDTO.getMessage());
            message.setData(messageDTO.getData());
            message.setId(messageDTO.getId());
            if(messageDTO.getReply() != 0){
                try {
                    Message message1 = findMessage(messageDTO.getId());
                    message.setReply(message1);
                } catch (RepositoryException e) {
                    e.printStackTrace();
                }
            }
            messages.add(message);
        }
        return messages;
    }

    public List<User> getNoFriend(int id){
        List<User> users = new ArrayList<>();
        List<User> userList = serviceUsers.all();
        List<Integer> users1 = network.getEdges(id);
        userList.
                forEach(x->{
                    if(!users1.contains(x.getId()))
                        users.add(x);
                });
        return users;
    }

    public Friendship getFriendship(int id1, int id2){
        List<Friendship> friendships = serviceFriendships.all();
        for(Friendship friendship : friendships)
            if((friendship.getUserB() == id1 && friendship.getUserA() == id2) || (friendship.getUserB() == id2 && friendship.getUserA() == id1))
                return friendship;

        return null;
    }


    public List<User> getFriendsForAUser(int user) throws RepositoryException, ValidatorException {
        List<Friendship> friendshipList = serviceFriendships.all();
        List<User> users = new ArrayList<>();
        friendshipList.stream().filter(x -> x.getUserA() == user || x.getUserB() == user).forEach(x -> {
            try {
                if(x.getUserA() == user)
                {
                    User user1 = serviceUsers.find(x.getUserB());
                    users.add(user1);
                }
                else
                {
                    User user1 = serviceUsers.find(x.getUserA());
                    users.add(user1);
                }

            } catch (RepositoryException e) {
                e.printStackTrace();
            }
        });
        return users;
    }

    public MessageDTO findMessageDTO(int id) throws RepositoryException {
        return messageService.findMessage(id);
    }


    public List<Friendship> friendshipsBetween2Dates(int user, int day1, int month1, int year1, int day2, int month2, int year2) throws Exception{
        LocalDateTime date1 = LocalDateTime.of(year1, month1, day1,0,0);
        LocalDateTime date2 = LocalDateTime.of(year2, month2, day2,23,59);
        ArrayList<Friendship> friendships = serviceFriendships.all();
        return friendships.stream().filter(x-> ((x.getUserA() == user || x.getUserB() == user) && x.getDate().compareTo(date1) >=0 && x.getDate().compareTo(date2) <= 0)
        ).collect(Collectors.toList());
    }

    public List<MessageDTO> messagesBetween2Dates(int user, int day1, int month1, int year1, int day2, int month2, int year2) throws Exception{
        LocalDateTime date1 = LocalDateTime.of(year1, month1, day1,0,0);
        LocalDateTime date2 = LocalDateTime.of(year2, month2, day2,23,59);
        List<MessageDTO> messages = messageService.all();
        return messages.stream().filter(x-> (x.getTo().contains(user) && x.getData().compareTo(date1) >=0 && x.getData().compareTo(date2) <= 0)
        ).collect(Collectors.toList());
    }
}