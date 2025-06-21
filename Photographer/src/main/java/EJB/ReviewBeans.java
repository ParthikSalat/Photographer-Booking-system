/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package EJB;

import Entity.Reviews;
import Entity.Users;
import Entity.Photographers;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * EJB Bean for managing Reviews with FK relationships to Users and Photographers
 */
@Stateless
public class ReviewBeans {

    @PersistenceContext(unitName = "extphotopul")
    private EntityManager em;

    // ✅ Create a new review with FK references
    public void createReview(Reviews review, int userId, int photographerId) {
        Users user = em.find(Users.class, userId);
        Photographers photographer = em.find(Photographers.class, photographerId);

        if (user == null || photographer == null) {
            throw new IllegalArgumentException("Invalid User or Photographer ID");
        }

        review.setUserID(user);
        review.setPhotographerID(photographer);
        em.persist(review);
    }

    // ✅ Find review by ID
    public Reviews getReviewById(int id) {
        return em.find(Reviews.class, id);
    }

    // ✅ Get all reviews
    public List<Reviews> getAllReviews() {
        return em.createNamedQuery("Reviews.findAll", Reviews.class).getResultList();
    }

    // ✅ Update a review
    public boolean updateReview(Reviews updatedReview, int userId, int photographerId) {
        Reviews existing = em.find(Reviews.class, updatedReview.getReviewID());
        if (existing == null) {
            return false;
        }

        Users user = em.find(Users.class, userId);
        Photographers photographer = em.find(Photographers.class, photographerId);

        if (user == null || photographer == null) {
            throw new IllegalArgumentException("Invalid User or Photographer ID");
        }

        existing.setRating(updatedReview.getRating());
        existing.setComment(updatedReview.getComment());
        existing.setUserID(user);
        existing.setPhotographerID(photographer);

        em.merge(existing);
        return true;
    }

    // ✅ Delete a review by ID
    public boolean deleteReview(int id) {
        Reviews review = em.find(Reviews.class, id);
        if (review != null) {
            em.remove(review);
            return true;
        }
        return false;
    }

    // ✅ Get reviews by Photographer ID
    public List<Reviews> getReviewsByPhotographerId(int photographerId) {
        return em.createQuery("SELECT r FROM Reviews r WHERE r.photographerID.photographerID = :photographerId", Reviews.class)
                .setParameter("photographerId", photographerId)
                .getResultList();
    }

    // ✅ Get reviews by User ID
    public List<Reviews> getReviewsByUserId(int userId) {
        return em.createQuery("SELECT r FROM Reviews r WHERE r.userID.userID = :userId", Reviews.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}

