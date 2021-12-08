package com.example.build;

import java.time.format.DateTimeFormatter;

public class Build {
    public static final String database_url = "jdbc:postgresql://socialnetwork.cqvqiaammati.eu-central-1.rds.amazonaws.com:5432/SocialNetwork";
    public static final String database_user = "postgres";
    public static final String database_password = "12345678";
    public static final String test_database_url = "jdbc:postgresql://socialnetwork.cqvqiaammati.eu-central-1.rds.amazonaws.com:5432/SocialNetwork_test";
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


}
