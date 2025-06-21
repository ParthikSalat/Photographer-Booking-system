/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package restAPI;

import EJB.BookingBeans;
import Entity.Bookings;
import Entity.Photographers;
import Entity.Users;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * REST API for managing Bookings
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

@Path("/bookings")

public class BookingAPI {

    @Inject
    private BookingBeans bookingService;

    // ✅ DTO to prevent circular references and control input/output
    public static class BookingDTO {
        public Integer bookingID;
        public Date bookingDate;
        public String status;
        public String paymentStatus;
        public Integer userID;
        public Integer photographerID;

        public BookingDTO() {}

        public BookingDTO(Bookings b) {
            this.bookingID = b.getBookingID();
            this.bookingDate = b.getBookingDate();
            this.status = b.getStatus();
            this.paymentStatus = b.getPaymentStatus();
            this.userID = b.getUserID() != null ? b.getUserID().getUserID() : null;
            this.photographerID = b.getPhotographerID() != null ? b.getPhotographerID().getPhotographerID() : null;
        }

        public Bookings toEntity() {
    Bookings b = new Bookings();
    b.setBookingID(this.bookingID);
    b.setBookingDate(this.bookingDate);
    b.setStatus(this.status);
    b.setPaymentStatus(this.paymentStatus);

    // Add user & photographer entities based on ID
    if (this.userID != null) {
        Users user = new Users();
        user.setUserID(this.userID);
        b.setUserID(user);
    }

    if (this.photographerID != null) {
        Photographers photographer = new Photographers();
        photographer.setPhotographerID(this.photographerID);
        b.setPhotographerID(photographer);
    }

    return b;
}

    }

    // ✅ GET all bookings
    @GET
    public List<BookingDTO> getAllBookings() {
        return bookingService.getAllBookings()
                .stream()
                .map(BookingDTO::new)
                .collect(Collectors.toList());
    }

    // ✅ GET booking by ID
    @GET
    @Path("/{id}")
    public Response getBookingById(@PathParam("id") int id) {
        Bookings booking = bookingService.getBookingById(id);
        if (booking == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Booking not found with ID: " + id)
                    .build();
        }
        return Response.ok(new BookingDTO(booking)).build();
    }

   @POST
@Produces(MediaType.APPLICATION_JSON) // ✅ Ensures JSON response
public Response createBooking(BookingDTO dto) {
    try {
        // Check availability before booking
        boolean available = bookingService.isPhotographerAvailable(dto.photographerID, dto.bookingDate);
        if (!available) {
            Map<String, Object> conflictResponse = new HashMap<>();
            conflictResponse.put("message", "Photographer is not available on the selected date.");
            return Response.status(Response.Status.CONFLICT)
                           .entity(conflictResponse)
                           .build();
        }

        // Create the booking if available
       Bookings booking = dto.toEntity();
bookingService.createBooking(booking);


        // ✅ Return JSON instead of plain text
        Map<String, Object> successResponse = new HashMap<>();
        successResponse.put("message", "Booking created successfully");
        successResponse.put("bookingId", booking.getBookingID());

        return Response.status(Response.Status.CREATED)
                       .entity(successResponse)
                       .build();
    } catch (IllegalArgumentException e) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", e.getMessage());
        return Response.status(Response.Status.BAD_REQUEST)
                       .entity(errorResponse)
                       .build();
    } catch (Exception e) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", "Error: " + e.getMessage());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                       .entity(errorResponse)
                       .build();
    }
}

@PUT
@Path("/{id}")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public Response updateBooking(@PathParam("id") int id, BookingDTO dto) {
    if (dto == null) {
        return Response.status(Response.Status.BAD_REQUEST).entity("Invalid booking data").build();
    }

    Bookings updatedBooking = dto.toEntity();
    updatedBooking.setBookingID(id);

    try {
        boolean success = bookingService.updateBooking(updatedBooking, dto.userID, dto.photographerID);
        if (success) {
            return Response.ok("Booking updated successfully").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Booking not found").build();
        }
    } catch (IllegalArgumentException e) {
        return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
    }
}


    // ✅ DELETE - Delete booking by ID
    @DELETE
    @Path("/{id}")
    public Response deleteBooking(@PathParam("id") int id) {
        boolean deleted = bookingService.deleteBooking(id);
        if (deleted) {
            return Response.ok("Booking deleted").build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Booking not found").build();
    }
    
    @DELETE
@Path("/photographer/{photographerId}")
public Response deleteBookingsByPhotographer(@PathParam("photographerId") int photographerId) {
    boolean deleted = bookingService.deleteBookingsByPhotographer(photographerId);
    if (deleted) {
        return Response.ok("Bookings deleted for photographer ID: " + photographerId).build();
    }
    return Response.status(Response.Status.NOT_FOUND).entity("No bookings found for photographer ID: " + photographerId).build();
}


    // ✅ GET bookings by user ID
    @GET
    @Path("/users/{userId}")
    public List<BookingDTO> getBookingsByUser(@PathParam("userId") int userId) {
        return bookingService.getBookingsByUserId(userId)
                .stream()
                .map(BookingDTO::new)
                .collect(Collectors.toList());
    }

    // ✅ GET bookings by photographer ID
    @GET
    @Path("/photographers/{photographerId}")
    public List<BookingDTO> getBookingsByPhotographer(@PathParam("photographerId") int photographerId) {
        return bookingService.getBookingsByPhotographerId(photographerId)
                .stream()
                .map(BookingDTO::new)
                .collect(Collectors.toList());
        
        
    }
    
      @GET
        @Path("/test")
        public String testEndpoint() {
            return "API is working!";
            }
        // ✅ GET total count of bookings
@GET
@Path("/count")
public Response getBookingCount() {
    int count = bookingService.getAllBookings().size();
    Map<String, Object> response = new HashMap<>();
    response.put("count", count);
    return Response.ok(response).build();
}

        }


