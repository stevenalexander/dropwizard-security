package com.example.resources;

import com.example.core.User;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.LinkedList;
import java.util.List;

@Path("/user")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class UserResource {

    public UserResource() {
        super();
    }

    @GET
    public List<User> getAll(){
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
