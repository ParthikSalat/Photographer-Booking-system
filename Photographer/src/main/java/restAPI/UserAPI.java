/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package restAPI;

import EJB.UserBeans;
import Entity.Users;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author preet
 */
@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserAPI {

    @Inject
    private UserBeans userService;

    // ✅ GET all users
    @GET
    public List<Users> getAllUsers() {
        return userService.getAllUsers();
    }

    // ✅ GET user by ID
    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") int id) {
        Users user = userService.getUserById(id);
        if (user != null) {
            return Response.ok(user).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                       .entity("User not found with ID: " + id)
                       .build();
    }

    // ✅ POST - create new user
    @POST
    public Response createUser(Users user) {
        try {
            userService.createUser(user);
            return Response.status(Response.Status.CREATED)
                           .entity("User created with ID: " + user.getUserID())
                           .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Error creating user: " + e.getMessage())
                           .build();
        }
    }

    // ✅ PUT - update existing user
    @PUT
    @Path("/{id}")
    public Response updateUser(@PathParam("id") int id, Users user) {
        try {
            Users existingUser = userService.getUserById(id);
            if (existingUser == null) {
                return Response.status(Response.Status.NOT_FOUND)
                               .entity("User not found with ID: " + id)
                               .build();
            }

            // Set new data
            existingUser.setFullName(user.getFullName());
            existingUser.setEmail(user.getEmail());
            existingUser.setPasswordHash(user.getPasswordHash());
            existingUser.setPhoneNumber(user.getPhoneNumber());
            existingUser.setAddress(user.getAddress());

            userService.updateUser(existingUser);

            return Response.ok("User updated successfully").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Error updating user: " + e.getMessage())
                           .build();
        }
    }

    // ✅ DELETE user by ID
    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") int id) {
        try {
            boolean deleted = userService.deleteUser(id);
            if (!deleted) {
                return Response.status(Response.Status.NOT_FOUND)
                               .entity("User not found with ID: " + id)
                               .build();
            }
            return Response.ok("User deleted with ID: " + id).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Error deleting user: " + e.getMessage())
                           .build();
        }
    }
}
