/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package EJB;

import Entity.Admins;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author preet
 */
@Stateless
public class AdminBeans {

    @PersistenceContext(unitName = "extphotopul") // Adjust if your persistence unit name is different
    private EntityManager em;

    // ✅ Create new admin
    public void createAdmin(Admins admin) {
        em.persist(admin);
    }

    // ✅ Find admin by ID
    public Admins getAdminById(int adminID) {
        return em.find(Admins.class, adminID);
    }

    // ✅ Update admin
    public void updateAdmin(Admins updatedAdmin) {
        em.merge(updatedAdmin);
    }

    // ✅ Delete admin by ID
    public boolean deleteAdmin(int adminID) {
        Admins admin = em.find(Admins.class, adminID);
        if (admin != null) {
            em.remove(admin);
            return true;
        }
        return false;
    }

    // ✅ Get all admins using named query
    public List<Admins> getAllAdmins() {
        return em.createNamedQuery("Admins.findAll", Admins.class).getResultList();
    }

    // ✅ Optional: Get admin by email (e.g., for login purposes)
    public Admins getAdminByEmail(String email) {
        try {
            return em.createNamedQuery("Admins.findByEmail", Admins.class)
                     .setParameter("email", email)
                     .getSingleResult();
        } catch (Exception e) {
            return null; // or handle more gracefully/log error
        }
    }
}
