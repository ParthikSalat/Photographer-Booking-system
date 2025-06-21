/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package EJB;

import Entity.Users;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
    public Users getUserById(int userId) {
    try {
        return em.createQuery("SELECT u FROM Users u WHERE u.userID = :userId", Users.class)
                            .setParameter("userId", userId)
                            .getSingleResult();
    } catch (NoResultException e) {
        return null;  // Return null if no user is found
    }
}
    
    
    
    public Users getUserByEmail(String email) {
    try {
        return em.createQuery("SELECT u FROM Users u WHERE u.email = :email", Users.class)
                 .setParameter("email", email)
                 .getSingleResult();
    } catch (NoResultException e) {
        return null; // No user found with this email
    }
}

    // ✅ Get all users using NamedQuery
    public List<Users> getAllUsers() {
        return em.createNamedQuery("Users.findAll", Users.class).getResultList();
    }

    // ✅ Update user details
    public void updateUser(Users updatedUser) {
        em.merge(updatedUser);
    }

   public Users validateUser(String email, String passwordHash) {
    try {
        List<Users> users = em.createQuery("SELECT u FROM Users u WHERE u.email = :email AND u.passwordHash = :passwordHash", Users.class)
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

    
}
