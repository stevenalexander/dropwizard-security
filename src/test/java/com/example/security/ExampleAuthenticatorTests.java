package com.example.security;

import com.yammer.dropwizard.auth.AuthenticationException;
import org.junit.Test;

public class ExampleAuthenticatorTests {

    @Test
    public void authenticateReturnsUserForValidCredentials() throws AuthenticationException {
        ExampleAuthenticator exampleAuthenticator = new ExampleAuthenticator();

        exampleAuthenticator.authenticate(new ExampleCredentials("validToken"));
    }

    @Test(expected = AuthenticationException.class)
    public void authenticateThrowsAuthenticationExceptionForInvalidCredentials() throws AuthenticationException {
        ExampleAuthenticator exampleAuthenticator = new ExampleAuthenticator();

        exampleAuthenticator.authenticate(new ExampleCredentials("failToken"));
    }
}
