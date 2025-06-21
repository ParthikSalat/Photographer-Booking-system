/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package EJB;

import Entity.Bookings;
import Entity.Payments;
import Entity.Photographers;
import Entity.Users;
import java.math.BigDecimal;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Stateless EJB Bean to manage Payments entity,
 * including foreign key relations to Bookings and Users.
 */
@Stateless
public class PaymentBeans {

    @PersistenceContext(unitName = "extphotopul")
    private EntityManager em;

 public void createPayment(Payments payment) {
    if (payment.getBookingID() != null) {
        Bookings booking = em.find(Bookings.class, payment.getBookingID().getBookingID());
        if (booking == null) {
            throw new IllegalArgumentException("Invalid Booking ID");
        }
        payment.setBookingID(booking);

        // Print Booking Info
        System.out.println("Booking found: ID = " + booking.getBookingID());
        System.out.println("User ID: " + (booking.getUserID() != null ? booking.getUserID().getUserID() : "null"));

        payment.setUserID(booking.getUserID());

        if (booking.getPhotographerID() != null) {
            Photographers photographer = em.find(Photographers.class, booking.getPhotographerID().getPhotographerID());
            
            if (photographer == null) {
                throw new IllegalArgumentException("Invalid Photographer ID");
            }

            // Print Photographer Info
            System.out.println("Photographer found: ID = " + photographer.getPhotographerID());
            System.out.println("Service Charge: " + photographer.getServiceCharge());

           // Get charges from photographer entity
            BigDecimal photographerCharge = photographer.getServiceCharge();
         BigDecimal platformCharge = photographerCharge.multiply(new BigDecimal("0.12"));

// Calculate total amount
            BigDecimal totalAmount = photographerCharge.add(platformCharge);

// Now set in payment entity
            payment.setAmount(totalAmount);
            payment.setPhotographerAmount(photographerCharge);
            payment.setPlatformCommission(platformCharge);

            // Print calculated amounts
            System.out.println("Total Amount: " + totalAmount);
            System.out.println("Photographer Share: " + photographerCharge);
            System.out.println("Platform Commission: " + platformCharge);
        } else {
            throw new IllegalArgumentException("No photographer linked to booking");
        }
    } else {
        throw new IllegalArgumentException("Booking ID required");
    }

    em.persist(payment);
    System.out.println("Payment inserted successfully!");
}



    // ✅ Get payment by ID
    public Payments getPaymentById(int id) {
        return em.find(Payments.class, id);
    }

    // ✅ Get all payments
    public List<Payments> getAllPayments() {
        return em.createNamedQuery("Payments.findAll", Payments.class).getResultList();
    }

    // ✅ Update a payment
    public void updatePayment(Payments updatedPayment) {
        // Ensure foreign keys are updated from managed entities
        if (updatedPayment.getBookingID() != null) {
            Bookings booking = em.find(Bookings.class, updatedPayment.getBookingID().getBookingID());
            updatedPayment.setBookingID(booking);
        }

        if (updatedPayment.getUserID() != null) {
            Users user = em.find(Users.class, updatedPayment.getUserID().getUserID());
            updatedPayment.setUserID(user);
        }

        em.merge(updatedPayment);
    }

    // ✅ Delete payment by ID
    public boolean deletePayment(int id) {
        Payments payment = em.find(Payments.class, id);
        if (payment != null) {
            em.remove(payment);
            return true;
        }
        return false;
    }

    // 🔍 Get all payments for a specific user
    public List<Payments> getPaymentsByUserId(int userId) {
        TypedQuery<Payments> query = em.createQuery(
            "SELECT p FROM Payments p WHERE p.userID.userID = :userId", Payments.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    // 🔍 Get all payments for a specific booking
    public List<Payments> getPaymentsByBookingId(int bookingId) {
        TypedQuery<Payments> query = em.createQuery(
            "SELECT p FROM Payments p WHERE p.bookingID.bookingID = :bookingId", Payments.class);
        query.setParameter("bookingId", bookingId);
        return query.getResultList();
    }

    // 🔍 Get a payment by transaction ID
    }
