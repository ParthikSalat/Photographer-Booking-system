/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author preet
 */
@Entity
@Table(name = "admins")
@NamedQueries({
    @NamedQuery(name = "Admins.findAll", query = "SELECT a FROM Admins a"),
    @NamedQuery(name = "Admins.findByAdminID", query = "SELECT a FROM Admins a WHERE a.adminID = :adminID"),
    @NamedQuery(name = "Admins.findByFullName", query = "SELECT a FROM Admins a WHERE a.fullName = :fullName"),
    @NamedQuery(name = "Admins.findByEmail", query = "SELECT a FROM Admins a WHERE a.email = :email"),
    @NamedQuery(name = "Admins.findByPasswordHash", query = "SELECT a FROM Admins a WHERE a.passwordHash = :passwordHash"),
    @NamedQuery(name = "Admins.findByRole", query = "SELECT a FROM Admins a WHERE a.role = :role")})
public class Admins implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "AdminID")
    private Integer adminID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "FullName")
    private String fullName;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "Email")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "PasswordHash")
    private String passwordHash;
    @Size(max = 11)
    @Column(name = "Role")
    private String role;

    public Admins() {
    }

    public Admins(Integer adminID) {
        this.adminID = adminID;
    }

    public Admins(Integer adminID, String fullName, String email, String passwordHash) {
        this.adminID = adminID;
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public Integer getAdminID() {
        return adminID;
    }

    public void setAdminID(Integer adminID) {
        this.adminID = adminID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (adminID != null ? adminID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Admins)) {
            return false;
        }
        Admins other = (Admins) object;
        if ((this.adminID == null && other.adminID != null) || (this.adminID != null && !this.adminID.equals(other.adminID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Admins[ adminID=" + adminID + " ]";
    }
    
}
