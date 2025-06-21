/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package EJB;

import Entity.Bookings;
import Entity.Payments;
import Entity.Users;

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

    // ✅ Create a new payment
    public void createPayment(Payments payment) {
        // Ensure that Booking and User entities are managed before persisting
        if (payment.getBookingID() != null) {
            Bookings booking = em.find(Bookings.class, payment.getBookingID().getBookingID());
            payment.setBookingID(booking);
        }

        if (payment.getUserID() != null) {
            Users user = em.find(Users.class, payment.getUserID().getUserID());
            payment.setUserID(user);
        }

        em.persist(payment);
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
    public Payments getPaymentByTransactionID(String transactionID) {
        try {
            return em.createNamedQuery("Payments.findByTransactionID", Payments.class)
                     .setParameter("transactionID", transactionID)
                     .getSingleResult();
        } catch (Exception e) {
            return null; // Could log this or throw a custom exception
        }
    }
}
