package com.example.security;

import com.example.core.User;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.LowLevelAppDescriptor;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.dropwizard.config.LoggingFactory;
import com.yammer.dropwizard.jersey.DropwizardResourceConfig;
import org.junit.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for provider based on Dropwizard BasicAuthProviderTests
 * https://github.com/dropwizard/dropwizard/blob/master/dropwizard-auth/src/test/java/io/dropwizard/auth/basic/BasicAuthProviderTest.java
 *
 * This is where you test your provider acts as expected when injected and used by the auth attributes in a resource
 */
public class ExampleSecurityProviderTests extends JerseyTest {

    static {
        LoggingFactory.bootstrap();
    }

    @Path("/test/")
    @Produces(MediaType.TEXT_PLAIN)
    public static class ExampleResource {
        @GET
        public String show(@Auth User principal) {
            return principal.getUsername();
        }
    }

    @Override
    protected AppDescriptor configure() {
        final DropwizardResourceConfig config = new DropwizardResourceConfig(true);
        final ExampleAuthenticator authenticator = new ExampleAuthenticator();

        config.getSingletons().add(new ExampleSecurityProvider<>(authenticator));
        config.getSingletons().add(new ExampleResource());

        return new LowLevelAppDescriptor.Builder(config).build();
    }

    @Test
    public void respondsToMissingCredentialsWith401() throws Exception {
        try {
            client().resource("/test")
                    .get(String.class);

            fail("Should have thrown 401");
        } catch (UniformInterfaceException ex) {
            assertEquals(ex.getResponse().getStatus(), 401);
        }
    }

    @Test
    public void transformsCredentialsToPrincipals() throws Exception {
        assertEquals(client().resource("/test")
                             .header(ExampleSecurityProvider.CUSTOM_HEADER, "validTokenReturnedAsUsername")
                             .get(String.class),
                "validTokenReturnedAsUsername");
    }

    @Test
    public void respondsToNonBasicCredentialsWith401() throws Exception {
        try {
            client().resource("/test")
                    .header(ExampleSecurityProvider.CUSTOM_HEADER, "failToken")
                    .get(String.class);

            fail("Should have thrown 401");
        } catch (UniformInterfaceException ex) {
            assertEquals(ex.getResponse().getStatus(), 401);
        }
    }

}
