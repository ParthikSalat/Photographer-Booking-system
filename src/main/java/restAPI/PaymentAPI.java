package restAPI;

import EJB.PaymentBeans;
import Entity.Payments;
import Entity.Bookings;
import Entity.Users;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.json.Json;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Path("/payments")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PaymentAPI {

    @Inject
    private PaymentBeans paymentService;

    // ✅ DTO to avoid recursion
 public static class PaymentDTO {
    public Integer paymentID;
    public String paymentMethod;
    public String paymentStatus;
    public Integer bookingID;
    public Integer userID;

    public PaymentDTO() {}

    public PaymentDTO(Payments p) {
        this.paymentID = p.getPaymentID();
        this.paymentMethod = p.getPaymentMethod();
        this.paymentStatus = p.getPaymentStatus();
        this.bookingID = (p.getBookingID() != null) ? p.getBookingID().getBookingID() : null;
        this.userID = (p.getUserID() != null) ? p.getUserID().getUserID() : null;
    }

    public Payments toEntity() {
        Payments p = new Payments();
        p.setPaymentID(this.paymentID);
        p.setPaymentMethod(this.paymentMethod);
        p.setPaymentStatus(this.paymentStatus);

        if (this.bookingID != null) {
            Bookings booking = new Bookings();
            booking.setBookingID(this.bookingID);
            p.setBookingID(booking);
        }
        if (this.userID != null) {
            Users user = new Users();
            user.setUserID(this.userID);
            p.setUserID(user);
        }
        return p;
    }
}


    // ✅ GET All Payments
    @GET
    public List<PaymentDTO> getAllPayments() {
        return paymentService.getAllPayments()
                .stream()
                .map(PaymentDTO::new)
                .collect(Collectors.toList());
    }

    // ✅ GET Payment by ID
    @GET
    @Path("/{id}")
    public Response getPaymentById(@PathParam("id") int id) {
        Payments payment = paymentService.getPaymentById(id);
        if (payment != null) {
            return Response.ok(new PaymentDTO(payment)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity(Json.createObjectBuilder().add("error", "Payment not found with ID: " + id).build())
                .build();
    }

    // ✅ POST - Create Payment
  @POST
public Response createPayment(PaymentDTO dto) {
    try {
        Payments payment = dto.toEntity();

        if (payment.getPaymentMethod() == null || payment.getPaymentStatus() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(Json.createObjectBuilder().add("error", "Payment method and status required").build())
                .build();
        }

        paymentService.createPayment(payment);
        return Response.status(Response.Status.CREATED)
                .entity(Json.createObjectBuilder()
                        .add("message", "Payment created successfully")
                        .add("PaymentID", payment.getPaymentID())
                        .build())
                .build();
    } catch (Exception e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(Json.createObjectBuilder().add("error", e.getMessage()).build())
                .build();
    }
}


    // ✅ PUT - Update Payment
//    @PUT
//    @Path("/{id}")
//    public Response updatePayment(@PathParam("id") int id, PaymentDTO dto) {
//        Payments existing = paymentService.getPaymentById(id);
//        if (existing == null) {
//            return Response.status(Response.Status.NOT_FOUND)
//                    .entity(Json.createObjectBuilder().add("error", "Payment not found").build())
//                    .build();
//        }
//
//        existing.setAmount(dto.amount);
//        existing.setPaymentMethod(dto.paymentMethod);
//        existing.setPaymentStatus(dto.paymentStatus);
//        existing.setPhotographerAmount(dto.photographerAmount);
//        existing.setPlatformCommission(dto.platformCommission);
//
//        if (dto.bookingID != null) {
//            Bookings booking = new Bookings();
//            booking.setBookingID(dto.bookingID);
//            existing.setBookingID(booking);
//        }
//
//        if (dto.userID != null) {
//            Users user = new Users();
//            user.setUserID(dto.userID);
//            existing.setUserID(user);
//        }
//
//        paymentService.updatePayment(existing);
//        return Response.ok(Json.createObjectBuilder().add("message", "Payment updated").build()).build();
//    }

    // ✅ DELETE - Delete Payment
    @DELETE
    @Path("/{id}")
    public Response deletePayment(@PathParam("id") int id) {
        boolean deleted = paymentService.deletePayment(id);
        if (deleted) {
            return Response.ok(Json.createObjectBuilder().add("message", "Payment deleted").build()).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity(Json.createObjectBuilder().add("error", "Payment not found").build())
                .build();
    }
}
