package com.example.repository.file;

import com.example.domain.Friendship;

import java.util.List;

public class FileFrienshipRepository extends AbstractFileRepository<Integer, Friendship> {

    /**
     * @param fileName name of the file where the friendships are stored
     */
    public FileFrienshipRepository(String fileName) {
        super(fileName);
    }

    /**
     * @param attribute a string
     * @return a string representing the id
     */
    @Override
    public Integer extractId(String attribute) {
        return Integer.parseInt(attribute);
    }

    /**
     * @param attributes List of strings
     * @return a Friendship object which as the fields of attributes
     */
    @Override
    public Friendship extractEntity(List<String> attributes) {
        Friendship fr = new Friendship(Integer.parseInt(attributes.get(1)), Integer.parseInt(attributes.get(2)));
        fr.setId(extractId(attributes.get(0)));
        return fr;
    }

    /**
     * @param entity a Friendship entity
     * @return a string representing the file representation of a friendship (username1username2;username1;username2)
     */
    @Override
    protected String createEntityAsString(Friendship entity) {
        return entity.getId() + ";" + entity.getUserA() + ";" + entity.getUserB() + "\n";
    }
}
