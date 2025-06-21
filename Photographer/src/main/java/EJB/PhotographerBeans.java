/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package EJB;

import Entity.Photographers;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
}
