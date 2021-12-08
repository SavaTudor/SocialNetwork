package com.example.domain;

import com.example.exception.ValidatorException;

public class ValidatorRequest implements Validator<FriendRequest> {
    @Override
    public void valideaza(FriendRequest entity) throws ValidatorException {
        String err = "";
        if (entity.getTo() == entity.getFrom()) {
            err = "Users must be different!\n";
        }
        if (!err.isEmpty()) {
            throw new ValidatorException(err);
        }
    }
}
