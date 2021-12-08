package com.example.repository.file;

import com.example.exception.RepositoryException;
import com.example.repository.memory.InMemoryRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractFileRepository<ID, T> extends InMemoryRepository<ID, T> {
    String fileName;

    /**
     * @param fileName string representing the name of the file
     */
    public AbstractFileRepository(String fileName) {
        this.fileName = fileName;
        loadFromFile();
    }

    /**
     * loads the data from the fileName file
     */
    private void loadFromFile(){
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(this.fileName))){
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                List<String> attributes = Arrays.asList(line.split(";"));
                ID id = extractId(attributes.get(0));
                T entity = extractEntity(attributes);
                super.add(id, entity);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * @param t a T element
     * The function appends to the file the t element
     */
    private void appendToFile(T t) {
        String line = createEntityAsString(t);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true));
        ) {
            bw.write(line, 0, line.length());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * The function overrides the file with the current items from the repository
     */
    private void overrideFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            this.all().forEach((entity) -> {
                String line = createEntityAsString(entity);
                try {
                    bw.write(line, 0, line.length());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @param attributes List of strings
     * @return a T entity with the fields extracted from the attributes list
     */
    public abstract T extractEntity(List<String> attributes);


    /**
     * @param entity a T entity
     * @return a string representing the file representation of the entity
     */
    protected abstract String createEntityAsString(T entity);

    /**
     * @param attribute a string
     * @return an ID object representing the id which the attribute assigns
     */
    public abstract ID extractId(String attribute);

    /**
     * @param id a ID object which will serve as the id for the t object
     * @param t a new T object that we want to add to the repository
     * @throws RepositoryException if the object we want to add already exists in the repository
     */
    public void add(ID id, T t) throws RepositoryException {
        super.add(id, t);
        appendToFile(t);
    }

    /**
     * updates the user with the given id
     * @param id the id of the user we want to modify
     * @param t a new user with the modified fields
     * @throws RepositoryException
     */
    public void update(ID id, T t) throws RepositoryException{
        super.update(id, t);
        overrideFile();
    }

    /**
     * @param id integer representing the id of the object that we want to erase, which is
     *           the first element of a pair stored by the hashmap
     * @return the deleted element
     * @throws RepositoryException if the element does not exist
     */
    public T remove(ID id) throws RepositoryException {
        T removedElem = super.remove(id);
        overrideFile();
        return removedElem;
    }

}
