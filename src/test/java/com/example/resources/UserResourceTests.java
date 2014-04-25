package com.example.resources;

import com.example.core.User;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.yammer.dropwizard.testing.ResourceTest;
import org.junit.Test;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class UserResourceTests extends ResourceTest {

    @Override
    protected void setUpResources() {
        addResource(new UserResource());
    }

    @Test
    public void getAll() throws Exception {
        List<User> users = client().resource("/user").get(new GenericType<List<User>>() {});
        assertEquals(2, users.size());
        assertEquals("user1", users.get(0).getUsername());
    }

    @Test
    public void get() throws Exception {
        User user = client().resource("/user/test1").get(User.class);
        assertEquals("test1", user.getUsername());
    }

    @Test(expected = UniformInterfaceException.class)
    public void add() throws Exception {
        User user = client().resource("/user").post(User.class);
    }

    @Test(expected = UniformInterfaceException.class)
    public void delete() throws Exception {
        client().resource("/user").delete();
    }
}
