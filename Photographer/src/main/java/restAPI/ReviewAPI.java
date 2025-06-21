/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package restAPI;

import EJB.ReviewBeans;
import Entity.Reviews;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST API for Reviews
 */
@Path("/reviews")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ReviewAPI {

    @Inject
    private ReviewBeans reviewService;

    // ✅ DTO (Data Transfer Object) to prevent circular references
    public static class ReviewDTO {
        public Integer reviewID;
        public Integer rating;
        public String comment;
        public Integer userID;
        public Integer photographerID;

        public ReviewDTO() {}

        public ReviewDTO(Reviews r) {
            this.reviewID = r.getReviewID();
            this.rating = r.getRating();
            this.comment = r.getComment();
            this.userID = r.getUserID() != null ? r.getUserID().getUserID() : null;
            this.photographerID = r.getPhotographerID() != null ? r.getPhotographerID().getPhotographerID() : null;
        }

        public Reviews toEntity() {
            Reviews r = new Reviews();
            r.setReviewID(this.reviewID);
            r.setRating(this.rating);
            r.setComment(this.comment);
            return r;
        }
    }

    // ✅ GET all reviews
    @GET
    public List<ReviewDTO> getAllReviews() {
        return reviewService.getAllReviews()
                .stream()
                .map(ReviewDTO::new)
                .collect(Collectors.toList());
    }

    // ✅ GET review by ID
    @GET
    @Path("/{id}")
    public Response getReviewById(@PathParam("id") int id) {
        Reviews review = reviewService.getReviewById(id);
        if (review != null) {
            return Response.ok(new ReviewDTO(review)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Review not found with ID: " + id)
                .build();
    }

    // ✅ POST - Create new review
    @POST
    public Response createReview(ReviewDTO dto) {
        try {
            Reviews review = dto.toEntity();
            reviewService.createReview(review, dto.userID, dto.photographerID);
            return Response.status(Response.Status.CREATED)
                    .entity("Review created with ID: " + review.getReviewID())
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error: " + e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Server error: " + e.getMessage())
                    .build();
        }
    }

    // ✅ PUT - Update review
    @PUT
    @Path("/{id}")
    public Response updateReview(@PathParam("id") int id, ReviewDTO dto) {
        Reviews review = dto.toEntity();
        review.setReviewID(id); // ensure path ID is applied

        try {
            boolean updated = reviewService.updateReview(review, dto.userID, dto.photographerID);
            if (updated) {
                return Response.ok("Review updated").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Review not found").build();
            }
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid User or Photographer ID").build();
        }
    }

    // ✅ DELETE - Remove review
    @DELETE
    @Path("/{id}")
    public Response deleteReview(@PathParam("id") int id) {
        boolean deleted = reviewService.deleteReview(id);
        if (deleted) {
            return Response.ok("Review deleted").build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Review not found").build();
    }

    // ✅ GET reviews by Photographer ID
    @GET
    @Path("/photographers/{photographerId}")
    public List<ReviewDTO> getReviewsByPhotographer(@PathParam("photographerId") int photographerId) {
        return reviewService.getReviewsByPhotographerId(photographerId)
                .stream()
                .map(ReviewDTO::new)
                .collect(Collectors.toList());
    }

    // ✅ GET reviews by User ID
    @GET
    @Path("/users/{userId}")
    public List<ReviewDTO> getReviewsByUser(@PathParam("userId") int userId) {
        return reviewService.getReviewsByUserId(userId)
                .stream()
                .map(ReviewDTO::new)
                .collect(Collectors.toList());
    }
}

