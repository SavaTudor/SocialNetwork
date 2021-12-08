package com.example.domain;


import java.time.LocalDateTime;

import static com.example.build.Build.formatter;

public class Friendship extends Entity<Integer> {
    int userA, userB;
    LocalDateTime date;

    /**
     * Constructor for the Friendship class
     *
     * @param userA an integer representing the id of the first person
     * @param userB an integer represinting the id of the second person
     */
    public Friendship(int userA, int userB) {
        this.userA = userA;
        this.userB = userB;
        date = LocalDateTime.now();
    }

    public Friendship(int userA, int userB, LocalDateTime date) {
        this.userA = userA;
        this.userB = userB;
        this.date = date;
    }

    /**
     * Getter for the first person
     *
     * @return integer representing their id
     */
    public int getUserA() {
        return userA;
    }


    /**
     * Getter for the second user
     *
     * @return integer representing their id
     */
    public int getUserB() {
        return userB;
    }

    /**
     * Getter for the date that the friendship has been created
     *
     * @return LocalDateTime representing the date that the friendship has been created
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Checks if two Friendships are equal
     *
     * @param o another Friendship
     * @return true if the users are the same in both of the friendships, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship that = (Friendship) o;
        return userA == that.userA && userB == that.userB || userB == that.userA && userA == that.userB;
    }

    /**
     * Gets the string format of the friendship
     *
     * @return String with the format id | userA userB
     */
    @Override
    public String toString() {
        return getId() + " | " + userA + " " + userB + " " + date.format(formatter);
    }


    /**
     * Checks if the user with the given id is part of the Friendship
     *
     * @param user integer representing the id of the user
     * @return true if he is part of the Friendship, false otherwise
     */
    public boolean isPart(int user) {
        return userA == user || userB == user;
    }
}
