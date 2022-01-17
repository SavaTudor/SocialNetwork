package com.example.domain;

import com.example.exception.ValidatorException;

public class ValidatorEvent implements Validator<Event> {

    @Override
    public void valideaza(Event entity) throws ValidatorException {
        String err = "";
        if (entity.getName().isEmpty()) {
            err += "Invalid name!\n";
        }
        if(entity.getDate()==null){
            err += "Invalid date!\n";
        }
        if(!err.isEmpty()){
            throw new ValidatorException(err);
        }
    }
}
