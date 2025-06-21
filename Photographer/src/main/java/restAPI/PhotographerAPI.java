package restAPI;

import EJB.PhotographerBeans;
import Entity.Photographers;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST API for Photographers using EJB and DTOs
 */
@Path("/photographers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PhotographerAPI {

    @Inject
    private PhotographerBeans photographerService;

    // ✅ DTO (Data Transfer Object) to avoid circular references
    public static class PhotographerDTO {
        public Integer photographerID;
        public String fullName;
        public String email;
        public String passwordHash;
        public String phoneNumber;
        public String address;
        public Integer experienceYears;
        public String specialization;
        public String portfolioURL;

        public PhotographerDTO() {}

        public PhotographerDTO(Photographers p) {
            this.photographerID = p.getPhotographerID();
            this.fullName = p.getFullName();
            this.email = p.getEmail();
            this.passwordHash = p.getPasswordHash();
            this.phoneNumber = p.getPhoneNumber();
            this.address = p.getAddress();
            this.experienceYears = p.getExperienceYears();
            this.specialization = p.getSpecialization();
            this.portfolioURL = p.getPortfolioURL();
        }

        public Photographers toEntity() {
            Photographers p = new Photographers();
            p.setPhotographerID(this.photographerID);
            p.setFullName(this.fullName);
            p.setEmail(this.email);
            p.setPasswordHash(this.passwordHash);
            p.setPhoneNumber(this.phoneNumber);
            p.setAddress(this.address);
            p.setExperienceYears(this.experienceYears);
            p.setSpecialization(this.specialization);
            p.setPortfolioURL(this.portfolioURL);
            return p;
        }
    }

    // ✅ GET all photographers
    @GET
    public List<PhotographerDTO> getAllPhotographers() {
        return photographerService.getAllPhotographers()
                .stream()
                .map(PhotographerDTO::new)
                .collect(Collectors.toList());
    }

    // ✅ GET photographer by ID
    @GET
    @Path("/{id}")
    public Response getPhotographerById(@PathParam("id") int id) {
        Photographers p = photographerService.getPhotographerById(id);
        if (p != null) {
            return Response.ok(new PhotographerDTO(p)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Photographer not found with ID: " + id)
                .build();
    }

    // ✅ POST - Create new photographer
    @POST
    public Response createPhotographer(PhotographerDTO dto) {
        try {
            Photographers p = dto.toEntity();
            photographerService.createPhotographer(p);
            return Response.status(Response.Status.CREATED)
                    .entity("Photographer created with ID: " + p.getPhotographerID())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error: " + e.getMessage())
                    .build();
        }
    }

    // ✅ PUT - Update photographer
    @PUT
    @Path("/{id}")
    public Response updatePhotographer(@PathParam("id") int id, PhotographerDTO dto) {
        Photographers existing = photographerService.getPhotographerById(id);
        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Photographer not found").build();
        }

        existing.setFullName(dto.fullName);
        existing.setEmail(dto.email);
        existing.setPasswordHash(dto.passwordHash);
        existing.setPhoneNumber(dto.phoneNumber);
        existing.setAddress(dto.address);
        existing.setExperienceYears(dto.experienceYears);
        existing.setSpecialization(dto.specialization);
        existing.setPortfolioURL(dto.portfolioURL);

        photographerService.updatePhotographer(existing);

        return Response.ok("Photographer updated").build();
    }

    // ✅ DELETE - Remove photographer
    @DELETE
    @Path("/{id}")
    public Response deletePhotographer(@PathParam("id") int id) {
        boolean deleted = photographerService.deletePhotographer(id);
        if (deleted) {
            return Response.ok("Photographer deleted").build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Photographer not found").build();
    }
}
