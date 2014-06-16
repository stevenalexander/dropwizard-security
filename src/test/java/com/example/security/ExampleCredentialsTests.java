package com.example.security;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ExampleCredentialsTests {

    @Test
    public void getToken() {
        ExampleCredentials credentials = new ExampleCredentials("1234");
        assertEquals("1234", credentials.getToken());
    }
}
