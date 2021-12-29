package com.example.domain;

import com.example.exception.ValidatorException;

public class ValidatorUser implements Validator<User> {
    /**
     * @param user a user object whose fields we want to validate
     * @throws ValidatorException if username and/or name and/or surname is/are empty
     */
    public void valideaza(User user) throws ValidatorException {
        String err = "";
        if (user.getFirstName().isEmpty()) {
            err += "Invalid first name!\n";
        }
        if (user.getLastName().isEmpty()) {
            err += "Invalid last name!\n";
        }
        if(user.getUsername().equals(null))
            err += "Invalid username!\n";
        if (!err.isEmpty()) {
            throw new ValidatorException(err);
        }
    }
}
