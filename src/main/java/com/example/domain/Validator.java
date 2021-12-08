package com.example.domain;

import com.example.exception.ValidatorException;


public interface Validator<E> {
    public void valideaza(E entity) throws ValidatorException;
}
