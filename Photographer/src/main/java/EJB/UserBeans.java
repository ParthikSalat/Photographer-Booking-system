/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package EJB;

import Entity.Users;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author preet
 */
@Stateless
public class UserBeans {

    @PersistenceContext(unitName = "extphotopul")
    private EntityManager em;

    // ✅ Create a new user
    public void createUser(Users user) {
        em.persist(user);
    }

    // ✅ Delete a user by ID
    public boolean deleteUser(int userID) {
        Users user = em.find(Users.class, userID);
        if (user != null) {
            em.remove(user);
            return true;
        }
        return false;
    }

    // ✅ Get a user by ID
    public Users getUserById(int userID) {
        return em.find(Users.class, userID);
    }

    // ✅ Get all users using NamedQuery
    public List<Users> getAllUsers() {
        return em.createNamedQuery("Users.findAll", Users.class).getResultList();
    }

    // ✅ Update user details
    public void updateUser(Users updatedUser) {
        em.merge(updatedUser);
    }
}
