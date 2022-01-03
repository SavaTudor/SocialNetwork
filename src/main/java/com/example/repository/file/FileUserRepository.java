package com.example.repository.file;

import com.example.domain.User;

import java.util.List;

public class FileUserRepository extends AbstractFileRepository<Integer, User>{


    public FileUserRepository(String fileName) {
        super(fileName);
    }

    /**
     * @param attributes List of strings
     * @return a User object with the fields from attributes
     */
    @Override
    public User extractEntity(List<String> attributes) {
        User user = new User(attributes.get(1), attributes.get(2),attributes.get(3),attributes.get(4));
        user.setId(extractId(attributes.get(0)));
        return user;
    }

    /**
     * @param entity a User entity
     * @return a string representing the user's representation in the file (username;name;surname)
     */
    @Override
    protected String createEntityAsString(User entity) {
        return entity.getId()+";"+entity.getFirstName()+";"+entity.getLastName()+'\n';
    }

    @Override
    public Integer extractId(String attribute) {
        return Integer.parseInt(attribute);
    }
}
