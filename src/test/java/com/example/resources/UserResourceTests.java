package com.example.resources;

import com.example.core.User;
import com.example.core.UserTests;
import com.example.security.ExampleAuthenticator;
import com.example.security.ExampleSecurityProvider;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.yammer.dropwizard.testing.ResourceTest;
import com.yammer.dropwizard.validation.InvalidEntityException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.ws.rs.core.MediaType;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class UserResourceTests extends ResourceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Override
    protected void setUpResources() {
        addResource(new UserResource());

        // Need to add SecurityProvider to all resource tests for resources using Auth, or you get 415 response
        // you can inject the authenticator to mock authentication results while ensuring you test true to how
        // the call will be made
        final ExampleAuthenticator authenticator = new ExampleAuthenticator();
        addProvider(new ExampleSecurityProvider<>(authenticator));
    }

    @Test
    public void getAll() throws Exception {
        List<User> users = client().resource("/user")
                                   .header(ExampleSecurityProvider.CUSTOM_HEADER, "validAdminToken")
                                   .get(new GenericType<List<User>>() {});
        assertEquals(2, users.size());
        assertEquals("user1", users.get(0).getUsername());
    }

    @Test
    public void getAllThrows401WhenNotAuthenticatedToken() throws Exception {
        try {
            client().resource("/user")
                    .get(new GenericType<List<User>>() {});

            fail("Should have thrown 401");
        } catch (UniformInterfaceException ex) {
            assertEquals(ex.getResponse().getStatus(), 401);
        }
    }

    @Test
    public void getAllThrows401WhenPrincipalNotDisplayRoleAdmin() throws Exception {
        try {
            client().resource("/user")
                    .header(ExampleSecurityProvider.CUSTOM_HEADER, "validBasicToken")
                    .get(new GenericType<List<User>>() {});

            fail("Should have thrown 401");
        } catch (UniformInterfaceException ex) {
            assertEquals(ex.getResponse().getStatus(), 401);
        }
    }

    @Test
    public void get() throws Exception {
        User user = client().resource("/user/test1").get(User.class);
        assertEquals("test1", user.getUsername());
    }

    @Test
    public void update() throws Exception {
        User user = UserTests.getUser();

        User updatedUser = client().resource("/user/test1")
                .type(MediaType.APPLICATION_JSON)
                .put(User.class, user);

        assertEquals(user, updatedUser);
    }

    @Test
    public void update_invalid_user() throws Exception {
        expectedException.expect(InvalidEntityException.class);

        User user = UserTests.getUser().setDisplayName("");

        User updatedUser = client().resource("/user/test1")
                .type(MediaType.APPLICATION_JSON)
                .put(User.class, user);
    }

    @Test()
    public void add() throws Exception {
        User newUser = UserTests.getUser();

        User user = client().resource("/user")
                .type(MediaType.APPLICATION_JSON)
                .post(User.class, newUser);

        assertEquals(newUser, user);
    }

    @Test()
    public void add_invalid_user() throws Exception {
        expectedException.expect(InvalidEntityException.class);

        User newUser = UserTests.getUser().setUsername(null);

        User user = client().resource("/user")
                .type(MediaType.APPLICATION_JSON)
                .post(User.class, newUser);
    }

    @Test()
    public void delete() throws Exception {
        client().resource("/user/test1").delete();
    }
}
