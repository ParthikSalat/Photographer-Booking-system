/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package EJB;

import Entity.Gallery;
import Entity.Photographers;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * EJB Bean for Gallery Entity
 * Handles CRUD operations on gallery images
 */
@Stateless
public class GalleryBeans {

    @PersistenceContext(unitName = "extphotopul")
    private EntityManager em;

    // ✅ Create a new gallery image
    public void createGalleryImage(Gallery gallery, Photographers photographer) {
        gallery.setPhotographerID(photographer);  // Setting the photographer
        em.persist(gallery);
    }

    // ✅ Get a gallery image by ID
    public Gallery getGalleryImageById(int id) {
        return em.find(Gallery.class, id);
    }

    // ✅ Get all gallery images
    public List<Gallery> getAllGalleryImages() {
        return em.createNamedQuery("Gallery.findAll", Gallery.class).getResultList();
    }

    // ✅ Update a gallery image
    public void updateGalleryImage(Gallery updatedGallery) {
        em.merge(updatedGallery);
    }

    // ✅ Delete a gallery image by ID
    public boolean deleteGalleryImage(int id) {
        Gallery gallery = em.find(Gallery.class, id);
        if (gallery != null) {
            em.remove(gallery);
            return true;
        }
        return false;
    }

    // ✅ Get all gallery images for a specific photographer
    public List<Gallery> getGalleryImagesByPhotographer(Photographers photographer) {
        return em.createQuery("SELECT g FROM Gallery g WHERE g.photographerID = :photographerID", Gallery.class)
                 .setParameter("photographerID", photographer)
                 .getResultList();
    }
}

