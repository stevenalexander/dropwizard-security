package com.example.resources;

import com.example.core.User;
import com.yammer.dropwizard.auth.Auth;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;

@Path("/user")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class UserResource {

    public UserResource() {
        super();
    }

    /*
    * Using the Auth attribute will use the injected provider to authenticate all requests to this path
    * You can also use the principal to apply authorisation in code dynamically
     */
    @GET
    public List<User> getAll(@Auth User principal){

        if (!principal.getDisplayRole().equals(User.ROLE_ADMIN)) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }

        List<User> users = new LinkedList<>();
        users.add(
            new User()
                .setUsername("user1")
                .setDisplayName("User 1")
                .setDisplayRole("Admin")
        );
        users.add(
            new User()
                .setUsername("user2")
                .setDisplayName("User 2")
                .setDisplayRole("DBA")
        );

        return users;
    }

    @GET
    @Path("/{username}")
    public User get(@PathParam("username") String username){
        return new User()
            .setUsername(username)
            .setDisplayName(username)
            .setDisplayRole("DBA");
    }

    @POST
    public User add(@Valid User user) {
        return user;
    }

    @PUT
    @Path("/{username}")
    public User update(@PathParam("username") String username, @Valid User user) {
        return user;
    }

    @DELETE
    @Path("/{username}")
    public void delete(@PathParam("username") String username) {
    }
}
