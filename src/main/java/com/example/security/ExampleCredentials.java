package com.example.security;

import java.math.BigInteger;

/**
 * This class is an example of a POJO used to hold custom credentials to be used to link a request to a specific principle (User)
 */
public class ExampleCredentials {
    private final String token;

    public ExampleCredentials(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
