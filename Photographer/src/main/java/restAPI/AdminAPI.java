/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package restAPI;

import EJB.AdminBeans;
import Entity.Admins;
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
@Path("/admins")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AdminAPI {

    @Inject
    private AdminBeans adminService;

    // ✅ GET all admins
    @GET
    public List<Admins> getAllAdmins() {
        return adminService.getAllAdmins();
    }

    // ✅ GET admin by ID
    @GET
    @Path("/{id}")
    public Response getAdminById(@PathParam("id") int id) {
        Admins admin = adminService.getAdminById(id);
        if (admin != null) {
            return Response.ok(admin).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                       .entity("Admin not found with ID: " + id)
                       .build();
    }

    // ✅ POST - create new admin
    @POST
    public Response createAdmin(Admins admin) {
        try {
            adminService.createAdmin(admin);
            return Response.status(Response.Status.CREATED)
                           .entity("Admin created with ID: " + admin.getAdminID())
                           .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Error creating admin: " + e.getMessage())
                           .build();
        }
    }

    // ✅ PUT - update existing admin
    @PUT
    @Path("/{id}")
    public Response updateAdmin(@PathParam("id") int id, Admins updatedAdmin) {
        try {
            Admins existingAdmin = adminService.getAdminById(id);
            if (existingAdmin == null) {
                return Response.status(Response.Status.NOT_FOUND)
                               .entity("Admin not found with ID: " + id)
                               .build();
            }

            // Set updated fields
            existingAdmin.setFullName(updatedAdmin.getFullName());
            existingAdmin.setEmail(updatedAdmin.getEmail());
            existingAdmin.setPasswordHash(updatedAdmin.getPasswordHash());
            existingAdmin.setRole(updatedAdmin.getRole());

            adminService.updateAdmin(existingAdmin);

            return Response.ok("Admin updated successfully").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Error updating admin: " + e.getMessage())
                           .build();
        }
    }

    // ✅ DELETE admin by ID
    @DELETE
    @Path("/{id}")
    public Response deleteAdmin(@PathParam("id") int id) {
        try {
            boolean deleted = adminService.deleteAdmin(id);
            if (!deleted) {
                return Response.status(Response.Status.NOT_FOUND)
                               .entity("Admin not found with ID: " + id)
                               .build();
            }
            return Response.ok("Admin deleted with ID: " + id).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Error deleting admin: " + e.getMessage())
                           .build();
        }
    }
}
