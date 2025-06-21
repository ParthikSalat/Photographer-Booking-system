package restAPI;

import EJB.PhotographerBeans;
import Entity.Photographers;
import com.mycompany.photographer.resources.JwtUtil;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import javax.json.Json;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Path("/photographers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PhotographerAPI {

    @Inject
    private PhotographerBeans photographerService;

    @PersistenceContext
    private EntityManager em;

    // ✅ DTO Class
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
        public BigDecimal serviceCharge;

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
            this.serviceCharge = p.getServiceCharge();
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
            p.setServiceCharge(this.serviceCharge);
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
                .entity(Json.createObjectBuilder()
                        .add("error", "Photographer not found with ID: " + id)
                        .build())
                .build();
    }

    // ✅ POST - Create photographer
    @POST
    public Response createPhotographer(PhotographerDTO dto) {
        try {
            Photographers p = dto.toEntity();
            photographerService.createPhotographer(p);
            return Response.status(Response.Status.CREATED)
                    .entity(Json.createObjectBuilder()
                            .add("message", "Photographer created")
                            .add("PhotographerID", p.getPhotographerID())
                            .build())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Json.createObjectBuilder()
                            .add("error", e.getMessage())
                            .build())
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
                    .entity(Json.createObjectBuilder()
                            .add("error", "Photographer not found")
                            .build())
                    .build();
        }

        existing.setFullName(dto.fullName);
        existing.setEmail(dto.email);
        existing.setPasswordHash(dto.passwordHash);
        existing.setPhoneNumber(dto.phoneNumber);
        existing.setAddress(dto.address);
        existing.setExperienceYears(dto.experienceYears);
        existing.setSpecialization(dto.specialization);
        existing.setPortfolioURL(dto.portfolioURL);
        existing.setServiceCharge(dto.serviceCharge);

        photographerService.updatePhotographer(existing);

        return Response.ok(Json.createObjectBuilder()
                .add("message", "Photographer updated")
                .build()).build();
    }

    // ✅ DELETE - Delete photographer
    @DELETE
    @Path("/{id}")
    public Response deletePhotographer(@PathParam("id") int id) {
        boolean deleted = photographerService.deletePhotographer(id);
        if (deleted) {
            return Response.ok(Json.createObjectBuilder()
                    .add("message", "Photographer deleted")
                    .build()).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity(Json.createObjectBuilder()
                        .add("error", "Photographer not found")
                        .build()).build();
    }

    // ✅ Login API
    @POST
    @Path("/login")
    public Response login(Photographers logindata) {
        Photographers photographer = photographerService.validateUser(logindata.getEmail(), logindata.getPasswordHash());
        if (photographer != null) {
            String token = JwtUtil.generateToken(
                    photographer.getPhotographerID(),
                    photographer.getFullName(),
                    photographer.getEmail(),
                    "photographer"
            );
            return Response.ok(Json.createObjectBuilder()
                    .add("token", token)
                    .build()).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(Json.createObjectBuilder()
                            .add("error", "Invalid credentials")
                            .build())
                    .build();
        }
    }

    // ✅ GET Photographer Full Name
    @GET
    @Path("/{id}/name")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getPhotographerName(@PathParam("id") int id) {
        Photographers photographer = photographerService.getPhotographerById(id);
        if (photographer == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Photographer not found")
                    .build();
        }
        return Response.ok(photographer.getFullName()).build();
    }

    // ✅ FILTER by specialization, experience and price
    // ✅ FILTER by specialization, experience, price and sort by serviceCharge
@GET
@Path("/filter")
public Response filterPhotographers(@QueryParam("specialization") String specialization,
                                    @QueryParam("minExperience") Integer minExp,
                                    @QueryParam("maxCharge") BigDecimal maxCharge,
                                    @QueryParam("sort") String sortOrder) {

    // Build dynamic JPQL query
    String jpql = "SELECT p FROM Photographers p WHERE 1=1 ";

    if (specialization != null && !specialization.isEmpty()) {
        jpql += "AND p.specialization = :specialization ";
    }
    if (minExp != null) {
        jpql += "AND p.experienceYears >= :minExp ";
    }
    if (maxCharge != null) {
        jpql += "AND p.serviceCharge <= :maxCharge ";
    }

    // ✅ Add sorting by service charge
    if ("desc".equalsIgnoreCase(sortOrder)) {
        jpql += "ORDER BY p.serviceCharge DESC";
    } else if ("asc".equalsIgnoreCase(sortOrder)) {
        jpql += "ORDER BY p.serviceCharge ASC";
    }

    // Create query
    TypedQuery<Photographers> query = em.createQuery(jpql, Photographers.class);

    if (specialization != null && !specialization.isEmpty()) {
        query.setParameter("specialization", specialization);
    }
    if (minExp != null) {
        query.setParameter("minExp", minExp);
    }
    if (maxCharge != null) {
        query.setParameter("maxCharge", maxCharge);
    }

    List<Photographers> results = query.getResultList();
    List<PhotographerDTO> dtoList = results.stream()
            .map(PhotographerDTO::new)
            .collect(Collectors.toList());

    return Response.ok(dtoList).build();
}

}
