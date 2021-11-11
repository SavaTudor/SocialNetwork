package com.domain;

import exception.ValidatorException;

public interface Validator<E> {
    public void valideaza(E entity) throws ValidatorException;
}
