package com.example;

import com.example.core.User;
import com.example.resources.UserResource;

import com.example.security.ExampleAuthenticator;
import com.example.security.ExampleSecurityProvider;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.config.Environment;

public class ExampleService extends com.yammer.dropwizard.Service<ExampleConfiguration> {

    public static void main(String[] args) throws Exception
    {
        new com.example.ExampleService().run(args);
    }

    @Override
    public void initialize(Bootstrap bootstrap) {
        bootstrap.setName("dropwizard-security");
    }

    @Override
    public void run(ExampleConfiguration configuration, Environment environment) throws Exception {
        environment.addResource(new UserResource());

        // Adds security provider so resource methods decorated with auth attribute will use this authenticator
        environment.addProvider(new ExampleSecurityProvider<User>(new ExampleAuthenticator()));
    }
}
