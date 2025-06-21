package restAPI;

import EJB.GalleryBeans;
import Entity.Gallery;
import Entity.Photographers;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST API for Gallery using EJB and DTOs
 */
@Path("/gallery")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GalleryAPI {

    @Inject
    private GalleryBeans galleryService;

    // ✅ DTO (Data Transfer Object) to avoid circular references
    public static class GalleryDTO {
        public Integer imageID;
        public String imageURL;
        public Integer photographerID;

        public GalleryDTO() {}

        public GalleryDTO(Gallery g) {
            this.imageID = g.getImageID();
            this.imageURL = g.getImageURL();
            this.photographerID = g.getPhotographerID().getPhotographerID();
        }

        public Gallery toEntity() {
            Gallery g = new Gallery();
            g.setImageID(this.imageID);
            g.setImageURL(this.imageURL);
            // Assume the photographerID is mapped correctly here in actual API interaction
            return g;
        }
    }

    // ✅ GET all gallery images
    @GET
    public List<GalleryDTO> getAllGalleryImages() {
        return galleryService.getAllGalleryImages()
                .stream()
                .map(GalleryDTO::new)
                .collect(Collectors.toList());
    }

    // ✅ GET gallery image by ID
    @GET
    @Path("/{id}")
    public Response getGalleryImageById(@PathParam("id") int id) {
        Gallery g = galleryService.getGalleryImageById(id);
        if (g != null) {
            return Response.ok(new GalleryDTO(g)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Gallery image not found with ID: " + id)
                .build();
    }

    // ✅ POST - Create new gallery image
    @POST
    public Response createGalleryImage(GalleryDTO dto) {
        try {
            Gallery g = dto.toEntity();
            // Fetch photographer based on photographerID from DTO
            Photographers photographer = new Photographers();  // Mock example - normally you would fetch this from the database
            photographer.setPhotographerID(dto.photographerID);
            galleryService.createGalleryImage(g, photographer);
            return Response.status(Response.Status.CREATED)
                    .entity("Gallery image created with ID: " + g.getImageID())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error: " + e.getMessage())
                    .build();
        }
    }

    // ✅ PUT - Update gallery image
    @PUT
    @Path("/{id}")
    public Response updateGalleryImage(@PathParam("id") int id, GalleryDTO dto) {
        Gallery existing = galleryService.getGalleryImageById(id);
        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Gallery image not found").build();
        }

        existing.setImageURL(dto.imageURL);
        // Optionally update the photographerID if needed
        galleryService.updateGalleryImage(existing);

        return Response.ok("Gallery image updated").build();
    }

    // ✅ DELETE - Remove gallery image
    @DELETE
    @Path("/{id}")
    public Response deleteGalleryImage(@PathParam("id") int id) {
        boolean deleted = galleryService.deleteGalleryImage(id);
        if (deleted) {
            return Response.ok("Gallery image deleted").build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Gallery image not found").build();
    }

    // ✅ GET gallery images by photographer ID
    @GET
    @Path("/photographer/{photographerID}")
    public List<GalleryDTO> getGalleryImagesByPhotographer(@PathParam("photographerID") int photographerID) {
        // Fetch photographer based on photographerID
        Photographers photographer = new Photographers();  // Mock example - normally you would fetch this from the database
        photographer.setPhotographerID(photographerID);
        
        List<Gallery> galleryImages = galleryService.getGalleryImagesByPhotographer(photographer);
        return galleryImages.stream()
                            .map(GalleryDTO::new)
                            .collect(Collectors.toList());
    }
}
