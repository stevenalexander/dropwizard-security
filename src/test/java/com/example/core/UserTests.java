package com.example.core;

import org.junit.Test;

import static com.yammer.dropwizard.testing.JsonHelpers.asJson;
import static com.yammer.dropwizard.testing.JsonHelpers.fromJson;
import static com.yammer.dropwizard.testing.JsonHelpers.jsonFixture;
import static org.junit.Assert.assertEquals;

public class UserTests {

    @Test
    public void serializesToJson() throws Exception {
        assertEquals(jsonFixture("fixtures/user.json"), asJson(getUser()));
    }

    @Test
    public void deserializesFromJSON() throws Exception {
        assertEquals(getUser(), fromJson(jsonFixture("fixtures/user.json"), User.class));
    }

    private User getUser() {
        User user = new User();
        user.setUsername("myName");
        user.setPassword("myPassword");
        user.setDisplayName("myDisplayName");
        user.setDisplayRole("myDisplayRole");

        return user;
    }
}
