/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package restAPI;

import EJB.BookingBeans;
import Entity.Bookings;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST API for managing Bookings
 */
@Path("/bookings")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
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

    // ✅ POST - Create new booking
    @POST
    public Response createBooking(BookingDTO dto) {
        try {
            Bookings booking = dto.toEntity();
            bookingService.createBooking(booking, dto.userID, dto.photographerID);
            return Response.status(Response.Status.CREATED)
                    .entity("Booking created with ID: " + booking.getBookingID())
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error: " + e.getMessage())
                    .build();
        }
    }

    // ✅ PUT - Update existing booking
    @PUT
    @Path("/{id}")
    public Response updateBooking(@PathParam("id") int id, BookingDTO dto) {
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
}
