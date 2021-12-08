package com.example.domain;

import com.example.exception.ValidatorException;

public class ValidatorMessage implements Validator<Message>{

    /**
     * validate the message
     * @params entity Message representing the message to be validated
     * @throws ValidatorException if the message is not valid
     */
    @Override
    public void valideaza(Message entity) throws ValidatorException {
        String errors = "";
        if(entity.getTo() == null)
            errors += "recipient must be not empty!\n";
        if(entity.getMessage() == "")
            errors += "message must be not empty!\n";
        if (!errors.isEmpty()) {
            throw new ValidatorException(errors);
        }
    }
}

