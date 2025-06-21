package EJB;

import Entity.Bookings;
import Entity.Users;
import Entity.Photographers;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * EJB for managing Bookings entity with proper FK handling
 */
@Stateless
public class BookingBeans {

    @PersistenceContext(unitName = "extphotopul")
    private EntityManager em;

    // ✅ Create a new booking with foreign key references
    public void createBooking(Bookings booking, int userId, int photographerId) {
        Users user = em.find(Users.class, userId);
        Photographers photographer = em.find(Photographers.class, photographerId);

        if (user == null || photographer == null) {
            throw new IllegalArgumentException("Invalid User or Photographer ID");
        }

        booking.setUserID(user);
        booking.setPhotographerID(photographer);
        em.persist(booking);
    }

    // ✅ Find booking by ID
    public Bookings getBookingById(int id) {
        return em.find(Bookings.class, id);
    }

    // ✅ Get all bookings
    public List<Bookings> getAllBookings() {
        return em.createNamedQuery("Bookings.findAll", Bookings.class).getResultList();
    }

    // ✅ Update booking with FK check
    public boolean updateBooking(Bookings updatedBooking, int userId, int photographerId) {
        Bookings existing = em.find(Bookings.class, updatedBooking.getBookingID());
        if (existing == null) {
            return false;
        }

        Users user = em.find(Users.class, userId);
        Photographers photographer = em.find(Photographers.class, photographerId);

        if (user == null || photographer == null) {
            throw new IllegalArgumentException("Invalid User or Photographer ID");
        }

        existing.setBookingDate(updatedBooking.getBookingDate());
        existing.setStatus(updatedBooking.getStatus());
        existing.setPaymentStatus(updatedBooking.getPaymentStatus());
        existing.setUserID(user);
        existing.setPhotographerID(photographer);

        em.merge(existing);
        return true;
    }

    // ✅ Delete a booking by ID
    public boolean deleteBooking(int id) {
        Bookings booking = em.find(Bookings.class, id);
        if (booking != null) {
            em.remove(booking);
            return true;
        }
        return false;
    }

    // ✅ Get bookings for a specific user
    public List<Bookings> getBookingsByUserId(int userId) {
        return em.createQuery("SELECT b FROM Bookings b WHERE b.userID.userID = :userId", Bookings.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    // ✅ Get bookings for a specific photographer
    public List<Bookings> getBookingsByPhotographerId(int photographerId) {
        return em.createQuery("SELECT b FROM Bookings b WHERE b.photographerID.photographerID = :photographerId", Bookings.class)
                .setParameter("photographerId", photographerId)
                .getResultList();
    }
}
