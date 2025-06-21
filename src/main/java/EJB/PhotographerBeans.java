/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package EJB;

import Entity.Photographers;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * EJB Bean for Photographer Entity
 * Handles CRUD operations on photographers
 */
@Stateless
public class PhotographerBeans {

    @PersistenceContext(unitName = "extphotopul")
    private EntityManager em;

    // ✅ Create a new photographer
    public void createPhotographer(Photographers photographer) {
        em.persist(photographer);
    }

    // ✅ Get a photographer by ID
    public Photographers getPhotographerById(int id) {
        return em.find(Photographers.class, id);
    }

    // ✅ Get all photographers
    public List<Photographers> getAllPhotographers() {
        return em.createNamedQuery("Photographers.findAll", Photographers.class).getResultList();
    }

    // ✅ Update a photographer
    public void updatePhotographer(Photographers updatedPhotographer) {
        em.merge(updatedPhotographer);
    }

    // ✅ Delete a photographer by ID
    public boolean deletePhotographer(int id) {
        Photographers photographer = em.find(Photographers.class, id);
        if (photographer != null) {
            em.remove(photographer);
            return true;
        }
        return false;
    }
    public Photographers validateUser(String email, String passwordHash) {
    try {
        List<Photographers> users = em.createQuery("SELECT u FROM Photographers u WHERE u.email = :email AND u.passwordHash = :passwordHash", Photographers.class)
                .setParameter("email", email)
                .setParameter("passwordHash", passwordHash)
                .getResultList();

        if (!users.isEmpty()) {
            return users.get(0); // ✅ Found a match
        }
    } catch (Exception e) {
        e.printStackTrace(); // Optional: log or handle exception
    }

    return null; // ❌ No user found or error occurred
}
// Get photographers by minimum service charge (example custom query)
public List<Photographers> getPhotographersByMinCharge(BigDecimal minCharge) {
    return em.createQuery(
            "SELECT p FROM Photographers p WHERE p.serviceCharge >= :minCharge", Photographers.class)
            .setParameter("minCharge", minCharge)
            .getResultList();
}
//filters mate
public List<Photographers> filterPhotographers(String specialization, Integer minExperience, BigDecimal maxCharge) {
    String jpql = "SELECT p FROM Photographers p WHERE 1=1";
    if (specialization != null && !specialization.isEmpty()) {
        jpql += " AND p.specialization = :specialization";
    }
    if (minExperience != null) {
        jpql += " AND p.experienceYears >= :minExperience";
    }
    if (maxCharge != null) {
        jpql += " AND p.serviceCharge <= :maxCharge";
    }

    TypedQuery<Photographers> query = em.createQuery(jpql, Photographers.class);
    if (specialization != null && !specialization.isEmpty()) {
        query.setParameter("specialization", specialization);
    }
    if (minExperience != null) {
        query.setParameter("minExperience", minExperience);
    }
    if (maxCharge != null) {
        query.setParameter("maxCharge", maxCharge);
    }

    return query.getResultList();
}

}
