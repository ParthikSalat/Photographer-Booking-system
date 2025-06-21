/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author parthik
 */
@Entity
@Table(name = "photographers")
@NamedQueries({
    @NamedQuery(name = "Photographers.findAll", query = "SELECT p FROM Photographers p"),
    @NamedQuery(name = "Photographers.findByPhotographerID", query = "SELECT p FROM Photographers p WHERE p.photographerID = :photographerID"),
    @NamedQuery(name = "Photographers.findByFullName", query = "SELECT p FROM Photographers p WHERE p.fullName = :fullName"),
    @NamedQuery(name = "Photographers.findByEmail", query = "SELECT p FROM Photographers p WHERE p.email = :email"),
    @NamedQuery(name = "Photographers.findByPasswordHash", query = "SELECT p FROM Photographers p WHERE p.passwordHash = :passwordHash"),
    @NamedQuery(name = "Photographers.findByPhoneNumber", query = "SELECT p FROM Photographers p WHERE p.phoneNumber = :phoneNumber"),
    @NamedQuery(name = "Photographers.findByExperienceYears", query = "SELECT p FROM Photographers p WHERE p.experienceYears = :experienceYears"),
    @NamedQuery(name = "Photographers.findBySpecialization", query = "SELECT p FROM Photographers p WHERE p.specialization = :specialization"),
    @NamedQuery(name = "Photographers.findByPortfolioURL", query = "SELECT p FROM Photographers p WHERE p.portfolioURL = :portfolioURL")})
public class Photographers implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PhotographerID")
    private Integer photographerID;
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
    @Size(max = 20)
    @Column(name = "PhoneNumber")
    private String phoneNumber;
    @Lob
    @Size(max = 65535)
    @Column(name = "Address")
    private String address;
    @Column(name = "ExperienceYears")
    private Integer experienceYears;
    @Size(max = 255)
    @Column(name = "Specialization")
    private String specialization;
    @Size(max = 255)
    @Column(name = "PortfolioURL")
    private String portfolioURL;
    @OneToMany(mappedBy = "photographerID")
    private Collection<Reviews> reviewsCollection;
    @OneToMany(mappedBy = "photographerID")
    private Collection<Bookings> bookingsCollection;
    @OneToMany(mappedBy = "photographerID")
    private Collection<Gallery> galleryCollection;

    public Photographers() {
    }

    public Photographers(Integer photographerID) {
        this.photographerID = photographerID;
    }

    public Photographers(Integer photographerID, String fullName, String email, String passwordHash) {
        this.photographerID = photographerID;
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public Integer getPhotographerID() {
        return photographerID;
    }

    public void setPhotographerID(Integer photographerID) {
        this.photographerID = photographerID;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getPortfolioURL() {
        return portfolioURL;
    }

    public void setPortfolioURL(String portfolioURL) {
        this.portfolioURL = portfolioURL;
    }

    public Collection<Reviews> getReviewsCollection() {
        return reviewsCollection;
    }

    public void setReviewsCollection(Collection<Reviews> reviewsCollection) {
        this.reviewsCollection = reviewsCollection;
    }

    public Collection<Bookings> getBookingsCollection() {
        return bookingsCollection;
    }

    public void setBookingsCollection(Collection<Bookings> bookingsCollection) {
        this.bookingsCollection = bookingsCollection;
    }

    public Collection<Gallery> getGalleryCollection() {
        return galleryCollection;
    }

    public void setGalleryCollection(Collection<Gallery> galleryCollection) {
        this.galleryCollection = galleryCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (photographerID != null ? photographerID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Photographers)) {
            return false;
        }
        Photographers other = (Photographers) object;
        if ((this.photographerID == null && other.photographerID != null) || (this.photographerID != null && !this.photographerID.equals(other.photographerID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Photographers[ photographerID=" + photographerID + " ]";
    }
    
}
