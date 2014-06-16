# Dropwizard Security

Sample application showing how to use security providers in your Dropwizard application.

Includes an example custom security provider which can be injected into your service and used in Resource methods to
authenticate requests and apply authorisation. Can be extended to implement a full authentication and authorisation for
the service.

## Setup

To compile:

```
gradle oneJar
```

To run:

```
java -jar build/libs/dropwizard-security-standalone.jar server config.yml
```

To test:

```
gradle test
```

## Introduction

The majority of the provider in this sample project was based off the [BasicAuthProvider](https://dropwizard.github.io/dropwizard/manual/auth.html) from Dropwizard auth library, I
wanted to do this to understand better how the injectable security provider works and figure out how it could be used to
implement authentication and authorisation.

Also wanted to ensure I could easily extend and test any resources using the security, as the overhead for a security
system is important in keeping your code clean and not obscuring the intended purpose with security code.

## Details

Implementing the custom security provider requires three classes (in com.example.security):

* [ExampleCredentials](https://github.com/stevenalexander/dropwizard-security/blob/master/src/main/java/com/example/security/ExampleCredentials.java)

    Holds the credentials extracted from the request, e.g. username/password or a token from a cookie.

* [ExampleAuthenticator](https://github.com/stevenalexander/dropwizard-security/blob/master/src/main/java/com/example/security/ExampleAuthenticator.java)

    Takes the credentials and authenticates them, returning a principle (user) object, throwing an
    AuthenticationException if the credentials are invalid.

* [ExampleSecurityProvider](https://github.com/stevenalexander/dropwizard-security/blob/master/src/main/java/com/example/security/ExampleSecurityProvider.java)

    Is injected into the service so any request decorated with the Dropwizard Auth attribute will be handled by this provider.
    Extracts credentials from requests, uses an authenticator to check them and throws WebExceptions if not authenticated.

    ```
    /**
     * An example security provider that will look at each request when received by an endpoint using the auth attribute
     * and check that it has a header value containing a token and will authenticate the token to get the Principle (User)
     * for the request (otherwise throw an AuthenticationException). That Principle is the authenticated User associated
     * with the request and the resource method handling the request can use it to check authorisation to perform actions.
     *
     * @param <T> The Principle class (User) to be returned when a request is authenticated
     */
    public class ExampleSecurityProvider<T> implements InjectableProvider <Auth, Parameter> {
        ...
        public ExampleSecurityProvider(Authenticator<ExampleCredentials, T> authenticator) {
            this.authenticator = authenticator;
        }
        ...
        private static class ExampleSecurityInjectable<T> extends AbstractHttpContextInjectable<T> {
            ...
            @Override
            public T getValue(HttpContext c) {
                // This is where the credentials are extracted from the request
                final String header = c.getRequest().getHeaderValue(CUSTOM_HEADER);
                try {
                    if (header != null) {
                        final Optional<T> result = authenticator.authenticate(new ExampleCredentials(header));
                        if (result.isPresent()) {
                            return result.get();
                        }
                    }
                } catch (AuthenticationException e) {
                    throw new WebApplicationException(Response.Status.UNAUTHORIZED);
                }

                if (required) {
                    throw new WebApplicationException(Response.Status.UNAUTHORIZED);
                }

                return null;
            }
        }
    ```

The security provider is injected into the service in the Service class.

```
public class ExampleService extends com.yammer.dropwizard.Service<ExampleConfiguration> {
    ...
    @Override
    public void run(ExampleConfiguration configuration, Environment environment) throws Exception {
        environment.addResource(new UserResource());

        // Adds security provider so resource methods decorated with auth attribute will use this authenticator
        environment.addProvider(new ExampleSecurityProvider<User>(new ExampleAuthenticator()));
    }
```

Resources using authentication just add the Auth attribute to their method signature.

```
@Path("/user")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class UserResource {
    ...
    /*
    * Using the Auth attribute will use the injected provider to authenticate all requests to this path
    * You can also use the principal to apply authorisation in code dynamically
     */
    @GET
    public List<User> getAll(@Auth User principal){

        if (!principal.getDisplayRole().equals(User.ROLE_ADMIN)) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
        ...
```

Testing the resources requires injecting a provider into the test service (note, I used the full one but for simplicity
you can inject a mock which would return a controllable principal).

```
public class UserResourceTests extends ResourceTest {
    ...
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
    ...
```

## Conclusion

Using the injectable security provider you can implement an extremely flexible authentication and authorisation system
in your service, while keeping the code overhead in your resource methods and tests minimal via the Auth attributes. In
terms of simplicity it blows away a lot of security libraries I've worked with for other frameworks.