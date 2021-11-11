package com.domain;

import java.util.ArrayList;
import java.util.Objects;

public class User extends Entity<Integer> {
    private String firstName, lastName;

    /**
     * Contstructor for the user class
     * @param firstName string representing the name of the user (0&lt;name.length)
     * @param lastName  string representing the surname of the user(0&lt;surname.length)
     */
    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Gets the User's firstName
     * @return String representing the User's first name
     */
    public String getFirstName() {
        return firstName;
    }


    /**
     * Attributes the new firstName to the user
     *
     * @param firstName a non-empty string
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the User's lastName
     *
     * @return String representing the User's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Attributes the new lastName to the user
     *
     * @param lastName a non-empty string
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Checks if two users are equal
     * @param o another User
     * @return true if the users ids are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return this.getId().equals(user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    /**
     * @return a string of the format firstName lastName
     */
    @Override
    public String toString() {
        return super.getId() + " | " + firstName + " " + lastName;
    }

}
