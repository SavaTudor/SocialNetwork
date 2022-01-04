package com.example.business;

import com.example.domain.*;
import com.example.exception.RepositoryException;
import com.example.exception.ValidatorException;
import com.example.repository.Repository;
import com.example.repository.database.DataBaseMessageRepository;
import com.example.repository.database.DataBaseUserRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MessageService {
    private Repository<Integer, Message> repository;
    private Repository<Integer, User> userRepository;
    private ValidatorMessage validatorMessage;

    /**
     * The constructor
     * @param url String representing the URL of database
     * @param user String representing the user of the database
     * @param password String representing the password of the user's account
     */
    public MessageService(String url, String user, String password) {
        try {
            this.repository = new DataBaseMessageRepository(url, user, password);
            this.userRepository = new DataBaseUserRepository(url, user, password);
            this.validatorMessage = new ValidatorMessage();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
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
        User fromUser = userRepository.find(from);
        List<User> toUser = new ArrayList<>();
        for(Integer user : to){
            if(user == from)
                throw new RepositoryException("you can not send message to yourself");
            User user1 = userRepository.find(user);
            toUser.add(user1);
        }
        Message message1 = new Message(fromUser, toUser, message);
        validatorMessage.valideaza(message1);
        repository.add(0, message1);

    }

    /**
     * Return all message from database
     * @return List of Message representing all message from database
     */
    public List<Message> all(){
        return repository.all();
    }

    /**
     * Remove a message from database
     * @param id Integer representing the id of message which we want to remove
     * @throws RepositoryException if there is no message with the id given in the database
     */
    public void removeMessage(int id) throws RepositoryException {
        repository.remove(id);
    }

    /**
     * Find a message in database
     * @param id Integer representing the id of the message which we are looking for
     * @return Message representing the message which we are looking for
     * @throws RepositoryException if there is no message with the id given in the database
     */
    public Message findMessage(int id) throws RepositoryException {
        return repository.find(id);
    }

    /**
     * Find all messages for a user
     * @param idUser Integer representing the id of the user
     * @return List of Message representing all messages for a user
     * @throws RepositoryException if there are no messages with for this user in database
     */
    public List<Message> findMessages(int idUser) throws RepositoryException {
        User user = userRepository.find(idUser);
        List<Message> list = repository.all();
        List<Message> listMessages = new ArrayList<>();
        list.forEach(x->{
            if(x.getTo().contains(user))
                listMessages.add(x);
        });
        if (listMessages.size() == 0)
            throw new RepositoryException("no messages for this user");
        return listMessages;
    }

    /**
     * Update a message
     * @param id1 Integer representing the id of message which we want to update
     * @param from User representing the sender of the message
     * @param to List of User representing the receiver of the message
     * @param message String representing the message of the Message
     * @throws RepositoryException if there is another message with the same id in the database or if i want so send myself a message
     * @throws ValidatorException if the new Message is not valid
     */
    public void updateMessage(int id1, int from, List<Integer> to, String message, Integer reply_to) throws RepositoryException, ValidatorException {
        User fromUser = userRepository.find(from);
        List<User> toUser = new ArrayList<>();
        for(Integer user : to){
            if(user == from)
                throw new RepositoryException("you can not send message to yourself");
            User user1 = userRepository.find(user);
            toUser.add(user1);
        }
        Message message1 = new Message(fromUser, toUser, message);
        Message reply = null;
        if(reply_to != null)
            reply = repository.find(reply_to);
        message1.setId(id1);
        message1.setReply(reply);
        validatorMessage.valideaza(message1);
        repository.update(id1, message1);
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
        User fromUser = userRepository.find(from);
        List<User> toUser = new ArrayList<>();
        for(Integer user : to){
            if(user == from)
                throw new RepositoryException("you can not send message to yourself!");
            User user1 = userRepository.find(user);
            toUser.add(user1);
        }
        if(toUser.size() != 1 )
            throw new RepositoryException("receptor invalid!");
        Message message2 = new Message(fromUser, toUser, mess);
        validatorMessage.valideaza(message2);
        repository.add(message,message2);
    }

    /**
     * Find all messages sent by a user
     * @param user Integer representing the id of the user
     * @return List of Message representing all messages sent by a user
     */
    public List<Message> allMessageByUser(int user){
        List<Message> messages = repository.all();
        return messages.stream().filter(x->x.getFrom().getId() == user)
                .collect(Collectors.toList());
    }

    public List<Message> allMessageBetween2Users(int id1, int id2) throws RepositoryException {
        User user1 = userRepository.find(id1);
        User user2 = userRepository.find(id2);
        List<Message> list = repository.all();
        List<Message> listMessages = new ArrayList<>();
        list.forEach(x->{
            if(x.getTo().contains(user1) && x.getFrom().equals(user2))
                listMessages.add(x);
            if(x.getTo().contains(user2) && x.getFrom().equals(user1))
                listMessages.add(x);
        });
        return listMessages;
    }

}
