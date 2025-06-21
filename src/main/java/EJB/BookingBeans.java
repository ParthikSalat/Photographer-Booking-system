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
  public void createBooking(Bookings booking) {
    // Load managed entities
    Users user = em.find(Users.class, booking.getUserID().getUserID());
    Photographers photographer = em.find(Photographers.class, booking.getPhotographerID().getPhotographerID());

    if (user == null || photographer == null) {
        throw new IllegalArgumentException("Invalid User or Photographer ID");
    }

    // Assign managed entities
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
    
    public boolean deleteBookingsByPhotographer(int photographerId) {
    List<Bookings> bookings = em.createQuery("SELECT b FROM Bookings b WHERE b.photographerID = :pid", Bookings.class)
                                .setParameter("pid", photographerId)
                                .getResultList();
    if (bookings.isEmpty()) {
        return false;
    }

    for (Bookings booking : bookings) {
        em.remove(em.contains(booking) ? booking : em.merge(booking));
    }
    return true;
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
    // ✅ Availability check before creating booking
public boolean isPhotographerAvailable(int photographerId, java.util.Date bookingDate) {
    Long count = em.createQuery(
            "SELECT COUNT(b) FROM Bookings b WHERE b.photographerID.photographerID = :pid AND FUNCTION('DATE', b.bookingDate) = FUNCTION('DATE', :bdate)",
            Long.class)
            .setParameter("pid", photographerId)
            .setParameter("bdate", bookingDate)
            .getSingleResult();

    return count == 0;
}

}
