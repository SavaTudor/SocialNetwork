package com.example.domain;

import com.example.exception.ValidatorException;

public class ValidatorFriendship implements Validator<Friendship> {
    @Override
    public void valideaza(Friendship entity) throws ValidatorException {
        String err = "";
        if (entity.getUserA() == entity.getUserB()) {
            err = "Users must be different!\n";
        }
        if (!err.isEmpty()) {
            throw new ValidatorException(err);
        }
    }
}
