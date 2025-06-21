package restAPI;

import EJB.PaymentBeans;
import Entity.Bookings;
import Entity.Payments;
import Entity.Users;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Path("/payments")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PaymentAPI {

    @Inject
    private PaymentBeans paymentService;

    // ✅ DTO class to avoid entity exposure and circular references
    public static class PaymentDTO {
        public Integer paymentID;
        public BigDecimal amount;
        public String paymentMethod;
        public String paymentStatus;
        public String transactionID;
        public Integer bookingID;
        public Integer userID;

        public PaymentDTO() {}

        public PaymentDTO(Payments p) {
            this.paymentID = p.getPaymentID();
            this.amount = p.getAmount();
            this.paymentMethod = p.getPaymentMethod();
            this.paymentStatus = p.getPaymentStatus();
            this.transactionID = p.getTransactionID();
            this.bookingID = p.getBookingID() != null ? p.getBookingID().getBookingID() : null;
            this.userID = p.getUserID() != null ? p.getUserID().getUserID() : null;
        }

        public Payments toEntity() {
            Payments p = new Payments();
            p.setPaymentID(this.paymentID);
            p.setAmount(this.amount);
            p.setPaymentMethod(this.paymentMethod);
            p.setPaymentStatus(this.paymentStatus);
            p.setTransactionID(this.transactionID);

            if (this.bookingID != null) {
                Bookings b = new Bookings();
                b.setBookingID(this.bookingID);
                p.setBookingID(b);
            }

            if (this.userID != null) {
                Users u = new Users();
                u.setUserID(this.userID);
                p.setUserID(u);
            }

            return p;
        }
    }

    // ✅ GET all payments
    @GET
    public List<PaymentDTO> getAllPayments() {
        return paymentService.getAllPayments()
                .stream()
                .map(PaymentDTO::new)
                .collect(Collectors.toList());
    }

    // ✅ GET payment by ID
    @GET
    @Path("/{id}")
    public Response getPaymentById(@PathParam("id") int id) {
        Payments payment = paymentService.getPaymentById(id);
        if (payment != null) {
            return Response.ok(new PaymentDTO(payment)).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Payment not found").build();
        }
    }

    // ✅ POST - Create a new payment
    @POST
    public Response createPayment(PaymentDTO dto) {
        try {
            Payments payment = dto.toEntity();
            paymentService.createPayment(payment);
            return Response.status(Response.Status.CREATED)
                    .entity("Payment created successfully").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error creating payment: " + e.getMessage()).build();
        }
    }

    // ✅ PUT - Update a payment
    @PUT
    @Path("/{id}")
    public Response updatePayment(@PathParam("id") int id, PaymentDTO dto) {
        Payments existing = paymentService.getPaymentById(id);
        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Payment not found").build();
        }

        existing.setAmount(dto.amount);
        existing.setPaymentMethod(dto.paymentMethod);
        existing.setPaymentStatus(dto.paymentStatus);
        existing.setTransactionID(dto.transactionID);

        if (dto.bookingID != null) {
            Bookings booking = new Bookings();
            booking.setBookingID(dto.bookingID);
            existing.setBookingID(booking);
        }

        if (dto.userID != null) {
            Users user = new Users();
            user.setUserID(dto.userID);
            existing.setUserID(user);
        }

        paymentService.updatePayment(existing);
        return Response.ok("Payment updated").build();
    }

    // ✅ DELETE - Delete a payment
    @DELETE
    @Path("/{id}")
    public Response deletePayment(@PathParam("id") int id) {
        boolean deleted = paymentService.deletePayment(id);
        if (deleted) {
            return Response.ok("Payment deleted").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Payment not found").build();
        }
    }

    // 🔍 GET payments by User ID
    @GET
    @Path("/users/{userId}")
    public List<PaymentDTO> getPaymentsByUserId(@PathParam("userId") int userId) {
        return paymentService.getPaymentsByUserId(userId)
                .stream()
                .map(PaymentDTO::new)
                .collect(Collectors.toList());
    }

    // 🔍 GET payments by Booking ID
    @GET
    @Path("/bookings/{bookingId}")
    public List<PaymentDTO> getPaymentsByBookingId(@PathParam("bookingId") int bookingId) {
        return paymentService.getPaymentsByBookingId(bookingId)
                .stream()
                .map(PaymentDTO::new)
                .collect(Collectors.toList());
    }

    // 🔍 GET payment by Transaction ID
    @GET
    @Path("/transactions/{transactionId}")
    public Response getPaymentByTransactionID(@PathParam("transactionId") String transactionId) {
        Payments payment = paymentService.getPaymentByTransactionID(transactionId);
        if (payment != null) {
            return Response.ok(new PaymentDTO(payment)).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Transaction not found").build();
        }
    }
}
