package com.example.security;

import com.example.core.User;
import com.google.common.base.Optional;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;

/**
 * This is an example authenticator that takes the credentials extracted from the request by the SecurityProvider
 * and authenticates the principle
 */
public class ExampleAuthenticator implements Authenticator<ExampleCredentials, User> {

    @Override
    public Optional<User> authenticate(ExampleCredentials credentials) throws AuthenticationException {

        // This is where you should call your authentication service and validate the token
        if (credentials.getToken().startsWith("fail")) {
            throw new AuthenticationException("Invalid credentials");
        } else {
            User user = new User();
            user.setDisplayName("User for token " + credentials.getToken());
            user.setUsername(credentials.getToken());
            user.setDisplayRole(credentials.getToken().contains("Admin") ? User.ROLE_ADMIN : User.ROLE_EDITOR);

            return Optional.fromNullable(user);
        }
    }
}
